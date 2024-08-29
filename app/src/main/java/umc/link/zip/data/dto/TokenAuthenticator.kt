package umc.link.zip.data.dto

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import dagger.Lazy
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import umc.link.zip.data.UserPreferences
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
class TokenAuthenticator @Inject constructor(
    private val tokenRefreshManager: Lazy<TokenRefreshManager>,
    private val context: Context
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        // 동기화된 접근 방식으로 이미 갱신을 시도한 경우를 체크
        synchronized(this) {
            if (responseCount(response) >= 3) {
                Log.d("TokenAuthenticator", "갱신 시도 횟수 초과: ${responseCount(response)}")
                restartApp(context)
                return null
            }

            val currentTime = System.currentTimeMillis() / 1000
            val accessTokenExpires = UserPreferences(context).getUserIdExpires()
                ?.let { getISODateToLong(it) }
            Log.d("TokenAuthenticator", "accessTokenExpires : $accessTokenExpires")
            Log.d("TokenAuthenticator", "currentTime : $currentTime")

            if (accessTokenExpires != null && currentTime < accessTokenExpires) {
                Log.d("TokenAuthenticator", "토큰 갱신 불필요")
                return response.request.newBuilder()
                    .header("Authorization", "Bearer ${UserPreferences(context).getUserId()}")
                    .build()
            }

            // 비동기적으로 토큰 갱신을 시도
            var newAccessToken: String? = null
            val latch = java.util.concurrent.CountDownLatch(1)

            GlobalScope.launch {
                newAccessToken = tokenRefreshManager.get().refreshToken()
                latch.countDown()
            }

            latch.await() // 메인 스레드를 기다리게 함
            if (newAccessToken != null) {
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            } else {
                Log.d("TokenAuthenticator", "토큰 갱신 실패")
                UserPreferences(context).deleteUserId()
                restartApp(context)
                return null
            }
        }
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        var priorResponse = response.priorResponse
        while (priorResponse != null) {
            result++
            priorResponse = priorResponse.priorResponse
        }
        return result
    }

    private fun restartApp(context: Context) {
        val packageManager = context.packageManager
        val intent = packageManager.getLaunchIntentForPackage(context.packageName)
        val mainIntent = Intent.makeRestartActivityTask(intent?.component)
        Log.d("TokenAuthenticator", "restart app")
        UserPreferences(context).deleteUserId()
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }

    private fun getISODateToLong(expires: String): Long? {
        return try {
            val formatter = DateTimeFormatter.ISO_DATE_TIME
            val dateTime = ZonedDateTime.parse(expires, formatter)
            dateTime.toEpochSecond()
        } catch (e: Exception) {
            Log.e("TokenAuthenticator", "날짜 파싱 실패: $expires", e)
            null
        }
    }
}
