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
import umc.link.zip.data.dto.link.request.LinkAddRequest
import umc.link.zip.data.dto.link.request.LinkExtractRequest
import umc.link.zip.data.dto.link.request.LinkModifyRequest
import umc.link.zip.data.dto.link.request.LinkSummaryRequest
import umc.link.zip.data.dto.link.response.LinkAddResponse
import umc.link.zip.data.dto.link.response.LinkDeleteResponse
import umc.link.zip.data.dto.link.response.LinkExtractResponse
import umc.link.zip.data.dto.link.response.LinkGetByLinkIDResponse
import umc.link.zip.data.dto.link.response.LinkGetResponse
import umc.link.zip.data.dto.link.response.LinkModifyResponse
import umc.link.zip.data.dto.link.response.LinkSummaryResponse
import umc.link.zip.data.dto.link.response.LinkUpdateLikeResponse
import umc.link.zip.data.dto.link.response.VisitLinkResponse
import umc.link.zip.data.dto.link.response.MoveLinkToNewZipResponse

interface LinkService {
    @GET("link/get_links/{zip_id}")
    suspend fun getLinkData(
        @Path ("zip_id") zip_id : Int,
        @Query ("tag") tag : String
    ) : Response<BaseResponse<LinkGetResponse>>

    @GET("link/get_links/{link_id}")
    suspend fun getLinkByLinkID(
        @Path ("link_id") link_id : Int
    ) : Response<BaseResponse<LinkGetByLinkIDResponse>>

    @PATCH("link/move/{link_id}/{new_zip_id}")
    suspend fun patchMoveLinkToNewZip(
        @Path ("link_id") link_id : Int,
        @Path ("new_zip_id") new_zip_id : Int
        ) : Response<BaseResponse<MoveLinkToNewZipResponse>>

    @POST("link/extract")
    suspend fun postExtractLink(
        @Body linkExtractRequest: LinkExtractRequest
    ) : Response<BaseResponse<LinkExtractResponse>>

    @POST("link/summary")
    suspend fun postSummaryLink(
        @Body linkSummaryRequest: LinkSummaryRequest
    ) : Response<BaseResponse<LinkSummaryResponse>>

    @POST("link/add")
    suspend fun postAddLink(
        @Body linkAddRequest: LinkAddRequest
    ) : Response<BaseResponse<LinkAddResponse>>

    @PATCH("link/visit/{link_id}")
    suspend fun patchVisitLink(
        @Path ("link_id") link_id : Int
    ) : Response<BaseResponse<VisitLinkResponse>>

    @PATCH("link/update_like/{link_id}")
    suspend fun patchUpdateLikeLink(
        @Path ("link_id") link_id : Int
    ) : Response<BaseResponse<LinkUpdateLikeResponse>>

    @PATCH("link/modify/{link_id}")
    suspend fun patchModifyLink(
        @Path ("link_id") link_id : Int,
        @Body linkModifyRequest: LinkModifyRequest
    ) : Response<BaseResponse<LinkModifyResponse>>

    @DELETE("link/delete/{link_id}")
    suspend fun deleteLink(
        @Path ("link_id") link_id : Int
    ) : Response<BaseResponse<LinkDeleteResponse>>
}

