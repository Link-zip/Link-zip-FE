package umc.link.zip.data.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.request.LoginRequest
import umc.link.zip.data.dto.response.LoginResponse

interface LoginService {
    @POST("/user/login")
    fun login(@Body loginRequest: LoginRequest): Response<BaseResponse<LoginResponse>>
}