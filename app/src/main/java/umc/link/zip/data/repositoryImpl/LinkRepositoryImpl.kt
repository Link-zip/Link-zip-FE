package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.link.response.LinkGetResponse
import umc.link.zip.data.dto.link.response.MoveLinkToNewZipResponse
import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.data.dto.zip.request.ZipEditRequest
import umc.link.zip.data.dto.zip.request.ZipRmRequest
import umc.link.zip.data.dto.zip.response.ZipCreateResponse
import umc.link.zip.data.dto.zip.response.ZipEditResponse
import umc.link.zip.data.dto.zip.response.ZipRmResponse
import umc.link.zip.data.service.LinkService
import umc.link.zip.data.service.ZipService
import umc.link.zip.domain.model.link.LinkGetModel
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
}


