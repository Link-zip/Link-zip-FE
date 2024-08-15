package umc.link.zip.data.repositoryImpl

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
import umc.link.zip.data.service.LinkService
import umc.link.zip.domain.model.link.LinkAddModel
import umc.link.zip.domain.model.link.LinkDeleteModel
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.domain.model.link.LinkGetByLinkIDModel
import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.domain.model.link.LinkModifyModel
import umc.link.zip.domain.model.link.LinkSummaryModel
import umc.link.zip.domain.model.link.LinkUpdateLikeModel
import umc.link.zip.domain.model.link.LinkVisitModel
import umc.link.zip.domain.model.link.MoveLinkToNewZipModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class LinkRepositoryImpl @Inject constructor(
    private val linkService: LinkService
) : LinkRepository {
    override suspend fun getLinkData(zip_id : Int, tag : String): NetworkResult<LinkGetModel> {
        return handleApi({ linkService.getLinkData(zip_id, tag) }) { response: BaseResponse<LinkGetResponse> -> response.result.toModel() }
    }

    override suspend fun GetLinkByLinkID(
        link_id: Int
    ): NetworkResult<LinkGetByLinkIDModel> {
        return handleApi({ linkService.getLinkByLinkID(link_id) }) { response: BaseResponse<LinkGetByLinkIDResponse> -> response.result.toModel() }
    }

    override suspend fun MoveLinkToNewZip(
        link_id: Int,
        new_zip_id: Int
    ): NetworkResult<MoveLinkToNewZipModel> {
        return handleApi({ linkService.patchMoveLinkToNewZip(link_id, new_zip_id) }) { response: BaseResponse<MoveLinkToNewZipResponse> -> response.result.toModel() }
    }

    override suspend fun ExtractLink(linkExtractRequest: LinkExtractRequest): NetworkResult<LinkExtractModel> {
        return handleApi({ linkService.postExtractLink(linkExtractRequest) }) { response: BaseResponse<LinkExtractResponse> -> response.result.toModel() }
    }

    override suspend fun SummaryLink(linkSummaryRequest: LinkSummaryRequest): NetworkResult<LinkSummaryModel> {
        return handleApi({ linkService.postSummaryLink(linkSummaryRequest) }) { response: BaseResponse<LinkSummaryResponse> -> response.result.toModel() }
    }

    override suspend fun AddLink(linkAddRequest: LinkAddRequest): NetworkResult<LinkAddModel> {
        return handleApi({ linkService.postAddLink(linkAddRequest) }) { response: BaseResponse<LinkAddResponse> -> response.result.toModel() }
    }

    override suspend fun VisitLink(
        link_id: Int
    ): NetworkResult<LinkVisitModel> {
        return handleApi({ linkService.patchVisitLink(link_id) }) { response: BaseResponse<VisitLinkResponse> -> response.result.toModel() }
    }

    override suspend fun UpdateLikeLink(
        link_id: Int
    ): NetworkResult<LinkUpdateLikeModel> {
        return handleApi({ linkService.patchUpdateLikeLink(link_id) }) { response: BaseResponse<LinkUpdateLikeResponse> -> response.result.toModel() }
    }

    override suspend fun ModifyLink(
        link_id: Int,
        linkModifyRequest: LinkModifyRequest
    ): NetworkResult<LinkModifyModel> {
        return handleApi({ linkService.patchModifyLink(link_id, linkModifyRequest ) }) { response: BaseResponse<LinkModifyResponse> -> response.result.toModel() }
    }

    override suspend fun DeleteLink(
        link_id: Int
    ): NetworkResult<LinkDeleteModel> {
        return handleApi({ linkService.deleteLink(link_id) }) { response: BaseResponse<LinkDeleteResponse> -> response.result.toModel() }
    }
}


