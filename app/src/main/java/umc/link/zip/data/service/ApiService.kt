package umc.link.zip.data.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import umc.link.zip.data.dto.request.LoginRequest
import umc.link.zip.data.dto.response.LoginResponse

interface ApiService {
    @POST("/user/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}