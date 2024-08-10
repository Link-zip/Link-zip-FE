package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.data.dto.zip.response.ZipCreateResponse
import umc.link.zip.data.service.ZipService
import umc.link.zip.domain.model.zip.ZipCreateModel
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
}

