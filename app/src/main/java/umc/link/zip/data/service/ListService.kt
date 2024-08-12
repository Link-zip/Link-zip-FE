package umc.link.zip.data.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.list.request.UnreadRequest
import umc.link.zip.data.dto.list.response.UnreadResponseList


interface ListService {
    @GET("/list/unview")
    suspend fun getUnreadList(@Body request: UnreadRequest): Response<BaseResponse<UnreadResponseList>>
}