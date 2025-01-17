package umc.link.zip.data

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context
    ) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // login API 요청인 경우, 인증 헤더를 추가하지 않음
        if (originalRequest.url.encodedPath.endsWith("/user/login") or
            originalRequest.url.encodedPath.endsWith("/user/refresh")) {
            Log.d("AuthInterceptor", "login API라서 header 추가 안함")
            return chain.proceed(originalRequest)
        }

        // SharedPreferences에서 JWT 토큰 가져오기
        val userId = UserPreferences(context).getUserId()

        // JWT 토큰이 있는 경우, Authorization 헤더 추가
        val authenticatedRequest = if (userId != null) {
            Log.d("AuthInterceptor", "header에 userId 추가")
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $userId")
                .build()
        } else {
            originalRequest
        }

        return chain.proceed(authenticatedRequest)
    }
}
