package umc.link.zip.data.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.request.LoginRequest
import umc.link.zip.data.dto.response.JwtResponse
import umc.link.zip.data.dto.response.LoginResponse

interface LoginService {
    @POST("/user/login")
    fun login(@Body request: LoginRequest): Call<BaseResponse<LoginResponse>>

    @GET("/user/info")
    fun checkJwt(): Call<BaseResponse<JwtResponse>>
}