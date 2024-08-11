package umc.link.zip.data.service

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.data.dto.zip.request.ZipEditRequest
import umc.link.zip.data.dto.zip.request.ZipRmRequest
import umc.link.zip.data.dto.zip.response.ZipCreateResponse
import umc.link.zip.data.dto.zip.response.ZipEditResponse
import umc.link.zip.data.dto.zip.response.ZipInquiryResponse
import umc.link.zip.data.dto.zip.response.ZipRmResponse

interface ZipService {
    @POST("zips")
    suspend fun postCreteZip(
        @Body zipCreateRequest: ZipCreateRequest
    ): Response<BaseResponse<ZipCreateResponse>>

    @GET("zips")
    suspend fun getInquiryZip(
        @Query ("sort") sort : String
    ) : Response<BaseResponse<ZipInquiryResponse>>

    @PATCH("zips")
    suspend fun patchEditZip(
        @Query ("body") zipEditRequest: ZipEditRequest
    ) : Response<BaseResponse<ZipEditResponse>>

    @DELETE("zips")
    suspend fun deleteRmZip(
        @Query ("body") zipRmRequest: ZipRmRequest
    ) : Response<BaseResponse<ZipRmResponse>>
}
