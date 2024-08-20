package umc.link.zip.data.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.search.response.SearchLinkResponse

interface SearchService {
    @GET("/search")
    suspend fun searchLink(@Query("keyword") keyword: String): Response<BaseResponse<SearchLinkResponse>>
}