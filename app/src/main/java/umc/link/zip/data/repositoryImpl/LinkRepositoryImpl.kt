package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.link.request.LinkAddRequest
import umc.link.zip.data.dto.link.request.LinkExtractRequest
import umc.link.zip.data.dto.link.request.LinkSummaryRequest
import umc.link.zip.data.dto.link.response.LinkAddResponse
import umc.link.zip.data.dto.link.response.LinkExtractResponse
import umc.link.zip.data.dto.link.response.LinkGetResponse
import umc.link.zip.data.dto.link.response.LinkSummaryResponse
import umc.link.zip.data.dto.link.response.MoveLinkToNewZipResponse
import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.data.dto.zip.request.ZipEditRequest
import umc.link.zip.data.dto.zip.request.ZipRmRequest
import umc.link.zip.data.dto.zip.response.ZipCreateResponse
import umc.link.zip.data.dto.zip.response.ZipEditResponse
import umc.link.zip.data.dto.zip.response.ZipGetResponse
import umc.link.zip.data.dto.zip.response.ZipRmResponse
import umc.link.zip.data.service.LinkService
import umc.link.zip.data.service.ZipService
import umc.link.zip.domain.model.link.LinkAddModel
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.domain.model.link.LinkSummaryModel
import umc.link.zip.domain.model.link.MoveLinkToNewZipModel
import umc.link.zip.domain.model.zip.ZipCreateModel
import umc.link.zip.domain.model.zip.ZipEditModel
import umc.link.zip.domain.model.zip.ZipRmModel
import umc.link.zip.domain.repository.LinkRepository
import umc.link.zip.domain.repository.ZipRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class LinkRepositoryImpl @Inject constructor(
    private val linkService: LinkService
) : LinkRepository {
    override suspend fun getLinkData(zip_id : Int, tag : String): NetworkResult<LinkGetModel> {
        return handleApi({ linkService.getLinkData(zip_id, tag) }) { response: BaseResponse<LinkGetResponse> -> response.result.toModel() }
    }

    override suspend fun MoveLinkToNewZip(
        link_id: Int,
        new_zip_id: Int
    ): NetworkResult<MoveLinkToNewZipModel> {
        return handleApi({ linkService.patchMoveLinkToNewZip(link_id, new_zip_id) }) { response: BaseResponse<MoveLinkToNewZipResponse> -> response.result.toModel() }
    }

    override suspend fun ExtractLink(linkExtractRequest: LinkExtractRequest): NetworkResult<LinkExtractModel> {
        return handleApi({ linkService.postLinkExtract(linkExtractRequest) }) { response: BaseResponse<LinkExtractResponse> -> response.result.toModel() }
    }

    override suspend fun SummaryLink(linkSummaryRequest: LinkSummaryRequest): NetworkResult<LinkSummaryModel> {
        return handleApi({ linkService.postLinkSummary(linkSummaryRequest) }) { response: BaseResponse<LinkSummaryResponse> -> response.result.toModel() }
    }

    override suspend fun AddLink(linkAddRequest: LinkAddRequest): NetworkResult<LinkAddModel> {
        return handleApi({ linkService.postLinkAdd(linkAddRequest) }) { response: BaseResponse<LinkAddResponse> -> response.result.toModel() }
    }
}


