package umc.link.zip.data.service

import retrofit2.Response
import retrofit2.http.GET
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.response.HomeAlertCountResponse
import umc.link.zip.data.dto.response.HomeOldCountResponse
import umc.link.zip.data.dto.response.HomeRecentResponse
import umc.link.zip.data.dto.response.HomeTotalCountResponse
import umc.link.zip.data.dto.response.HomeUnreadCountResponse
import umc.link.zip.domain.model.home.Link

interface HomeService {
    @GET("/list/recent")
    suspend fun getRecentList(): Response<BaseResponse<HomeRecentResponse>>

    @GET("/links/alert-count")
    suspend fun getAlertCount(): Response<BaseResponse<HomeAlertCountResponse>>

    @GET("/links/unread-count")
    suspend fun getUnreadCount(): Response<BaseResponse<HomeUnreadCountResponse>>

    @GET("/links/old-count")
    suspend fun getOldCount(): Response<BaseResponse<HomeOldCountResponse>>

    @GET("/links/total-count")
    suspend fun getTotalCount(): Response<BaseResponse<HomeTotalCountResponse>>
}