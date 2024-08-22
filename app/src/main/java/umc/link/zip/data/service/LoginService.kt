package umc.link.zip.data.service

import retrofit2.Call
import umc.link.zip.data.dto.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.request.LoginRequest
import umc.link.zip.data.dto.request.RefreshRequest
import umc.link.zip.data.dto.response.JwtResponse
import umc.link.zip.data.dto.request.SignupRequest
import umc.link.zip.data.dto.response.NameCheckResponse
import umc.link.zip.data.dto.response.RefreshResponse
import umc.link.zip.data.dto.response.SignupResponse

interface LoginService {
    @POST("/user/login")
    fun login(@Body request: LoginRequest): Call<BaseResponse<LoginResponse>>

    @GET("/user/info")
    fun checkJwt(): Call<BaseResponse<JwtResponse>>

    @GET("/user")
    suspend fun nameCheck(@Query("nickname") nickname: String): Response<BaseResponse<NameCheckResponse>>

    @POST("/user")
    suspend fun signup(@Body request: SignupRequest): Response<BaseResponse<SignupResponse>>

    @POST("/user/refresh")
    suspend fun refresh(@Body request: RefreshRequest): Response<BaseResponse<RefreshResponse>>
}
