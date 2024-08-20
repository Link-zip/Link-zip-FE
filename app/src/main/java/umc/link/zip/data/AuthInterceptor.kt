package umc.link.zip.data

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // login API 요청인 경우, 인증 헤더를 추가하지 않음
        if (originalRequest.url.encodedPath.endsWith("/user/token/test")) {
            return chain.proceed(originalRequest)
        }

        // SharedPreferences에서 JWT 토큰 가져오기
        val userId = UserPreferences(context).getUserId()

        // JWT 토큰이 있는 경우, Authorization 헤더 추가
        val authenticatedRequest = if (userId != null) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOjk5LCJuaWNrbmFtZSI6InRlc3Rfbmlja25hbWUiLCJrYWthb0lkIjo5OSwiY29ubmVjdGVkQXQiOiIyMDI0LTA3LTE1VDE1OjAwOjAwLjAwMFoiLCJpYXQiOjE3MjQxODEyMDMsImV4cCI6MTcyNDE4NDgwM30.K3BKVdg_JljzePvhlmWsaHWAuuG77l-tO_TuV81IZXU")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(authenticatedRequest)
    }
}
