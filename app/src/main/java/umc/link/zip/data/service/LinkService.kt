package umc.link.zip.data.service

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.link.response.LinkGetResponse
import umc.link.zip.data.dto.link.response.MoveLinkToNewZipResponse
import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.data.dto.zip.request.ZipEditRequest
import umc.link.zip.data.dto.zip.request.ZipRmRequest
import umc.link.zip.data.dto.zip.response.ZipCreateResponse
import umc.link.zip.data.dto.zip.response.ZipEditResponse
import umc.link.zip.data.dto.zip.response.ZipInquiryResponse
import umc.link.zip.data.dto.zip.response.ZipRmResponse

interface LinkService {
    @GET("link/get_links/{zip_id}")
    suspend fun getLinkData(
        @Path ("zip_id") zip_id : Int,
        @Query ("tag") tag : String
    ) : Response<BaseResponse<LinkGetResponse>>

    @PATCH("link/move/{link_id}/{new_zip_id}")
    suspend fun patchMoveLinkToNewZip(
        @Path ("link_id") link_id : Int,
        @Path ("new_zip_id") new_zip_id : Int
        ) : Response<BaseResponse<MoveLinkToNewZipResponse>>
}

