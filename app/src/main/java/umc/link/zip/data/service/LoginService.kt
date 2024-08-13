package umc.link.zip.data.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import umc.link.zip.data.dto.response.NameCheckResponse

interface LoginService {
    @GET("/user")
    suspend fun nameCheck(@Query("nickname") nickname: String): Response<NameCheckResponse>
}