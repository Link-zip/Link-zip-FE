package umc.link.zip.domain.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import umc.link.zip.domain.model.ZipItem

interface ApiService {
    @POST("/zips")
    suspend fun createZip(@Body zipItem: ZipItem): Response<ZipResponse>
}

data class ZipResponse(val isSuccess: Boolean, val code: Int, val message: String)
