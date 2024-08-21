package umc.link.zip.data.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.list.request.UnreadRequest
import umc.link.zip.data.dto.list.response.UnreadResponseList


interface ListService {
    @GET("/list/unview")
    suspend fun getUnreadList( @Query("sort") sort: String,
                               @Query("filter") filter: String) : Response<BaseResponse<UnreadResponseList>>
    @GET("/list/like")
    suspend fun getLikeList( @Query("sort") sort: String,
                               @Query("filter") filter: String) : Response<BaseResponse<UnreadResponseList>>
    @GET("/list/recent")
    suspend fun getRecentList( @Query("sort") sort: String,
                             @Query("filter") filter: String) : Response<BaseResponse<UnreadResponseList>>
}