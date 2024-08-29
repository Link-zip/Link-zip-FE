package umc.link.zip.data.dto

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import umc.link.zip.data.UserPreferences
import umc.link.zip.data.dto.request.RefreshRequest
import umc.link.zip.data.service.LoginService
import javax.inject.Inject

class TokenRefreshManager @Inject constructor(
    private val loginService: LoginService,
    private val context: Context
) {
    suspend fun refreshToken(): String? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("TokenAuthenticator", "TokenRefreshManager로 넘어옴")
                val userPreferences = UserPreferences(context)
                val refreshToken = userPreferences.getUserIdRefresh()
                if (refreshToken != null) {
                    val response = loginService.refresh(RefreshRequest(refreshToken))
                    if (response.isSuccessful) {
                        val newAccessToken = response.body()?.result?.accessToken
                        val newAccessTokenExpires = response.body()?.result?.accessTokenExpiresAt
                        Log.d("TokenAuthenticator", "잘 받아옴 : $newAccessToken")
                        Log.d("TokenAuthenticator", "잘 받아옴 : $newAccessTokenExpires")

                        // 토큰 저장
                        userPreferences.apply {
                            saveAccessToken(newAccessToken ?: "")
                            saveAccessTokenExpires(newAccessTokenExpires ?: "")
                        }
                        newAccessToken
                    } else {
                        null
                    }
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.d("TokenAuthenticator", "TokenRefreshManager에서 에러 : $e")
                null
            }
        }
    }
}
