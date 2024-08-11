package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.data.dto.zip.request.ZipEditRequest
import umc.link.zip.data.dto.zip.request.ZipRmRequest
import umc.link.zip.data.dto.zip.response.ZipCreateResponse
import umc.link.zip.data.dto.zip.response.ZipEditResponse
import umc.link.zip.data.dto.zip.response.ZipInquiryResponse
import umc.link.zip.data.dto.zip.response.ZipRmResponse
import umc.link.zip.data.service.ZipService
import umc.link.zip.domain.model.zip.ZipCreateModel
import umc.link.zip.domain.model.zip.ZipEditModel
import umc.link.zip.domain.model.zip.ZipInquiryModel
import umc.link.zip.domain.model.zip.ZipRmModel
import umc.link.zip.domain.repository.ZipRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class ZipRepositoryImpl @Inject constructor(
    private val zipService: ZipService
) : ZipRepository {
    override suspend fun postCreateZip(createRequest: ZipCreateRequest): NetworkResult<ZipCreateModel> {
        return handleApi({ zipService.postCreteZip(createRequest) }) { response: BaseResponse<ZipCreateResponse> -> response.result.toModel() }
    }

    override suspend fun getInquiryZip(sort: String): NetworkResult<ZipInquiryModel> {
        return handleApi({ zipService.getInquiryZip(sort) }) { response: BaseResponse<ZipInquiryResponse> -> response.result.toModel() }
    }

    override suspend fun patchEditZip(editRequest: ZipEditRequest): NetworkResult<ZipEditModel> {
        return handleApi({ zipService.patchEditZip(editRequest) }) { response: BaseResponse<ZipEditResponse> -> response.result.toModel() }
    }

    override suspend fun deleteRmZip(rmRequest: ZipRmRequest): NetworkResult<ZipRmModel> {
        return handleApi({ zipService.deleteRmZip(rmRequest) }) { response: BaseResponse<ZipRmResponse> -> response.result.toModel() }
    }
}

