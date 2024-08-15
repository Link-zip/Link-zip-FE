package umc.link.zip.data.service

import retrofit2.Call
import umc.link.zip.data.dto.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.request.SignupRequest
import umc.link.zip.data.dto.response.NameCheckResponse
import umc.link.zip.data.dto.response.SignupResponse

interface LoginService {
    @GET("/user")
    suspend fun nameCheck(@Query("nickname") nickname: String): Response<NameCheckResponse>

    @POST("/user")
    suspend fun signup(@Body request: SignupRequest): Response<BaseResponse<SignupResponse>>

    @POST("/user/token/test")
    fun login(): Call<BaseResponse<LoginResponse>>
}