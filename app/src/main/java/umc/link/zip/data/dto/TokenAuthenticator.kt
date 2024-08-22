package umc.link.zip.data.dto

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import umc.link.zip.data.UserPreferences
import umc.link.zip.data.dto.request.RefreshRequest
import umc.link.zip.data.service.LoginService
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
    private val userPreferences: UserPreferences,
    private val loginService: LoginService,
    private val context: Context
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? {
        synchronized(this) {
            // 이미 갱신을 시도한 경우, 무한 루프를 방지하기 위해 null을 반환
            if (responseCount(response) >= 3) {
                restartApp(context)
                return null
            }

            val currentTime = System.currentTimeMillis() / 1000 // 초 단위로 변환
            val accessTokenExpires = userPreferences.getUserIdExpires()?.toLongOrNull()

            // 만료 시간이 현재 시간을 초과하면 토큰 갱신 필요 없음
            if (accessTokenExpires != null && currentTime < accessTokenExpires) {
                restartApp(context)
                return null
            }

            // 토큰 갱신을 동기적으로 처리
            val newTokenResponse = runBlocking {
                val refreshToken = userPreferences.getUserIdRefresh()
                if(refreshToken != null) {
                    loginService.refresh(RefreshRequest(refreshToken))
                } else {
                    restartApp(context)
                    null
                }
            }

            if (newTokenResponse?.isSuccessful == true) {
                val newAccessToken = newTokenResponse.body()?.result?.accessToken
                val newAccessTokenExpires = newTokenResponse.body()?.result?.accessTokenExpires

                // 새로 받은 토큰을 저장
                userPreferences.saveAccessToken(newAccessToken ?: "")
                userPreferences.saveAccessTokenExpires(newAccessTokenExpires ?: "")

                // 새로운 토큰으로 원래 요청을 다시 만듦
                return response.request.newBuilder()
                    .header("Authorization", "Bearer $newAccessToken")
                    .build()
            } else {
                restartApp(context)
                return null
            }
        }
    }

    // 응답 횟수 체크
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
        userPreferences.deleteUserId()
        context.startActivity(mainIntent)
        Runtime.getRuntime().exit(0)
    }
}
