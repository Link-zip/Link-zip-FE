package umc.link.zip.domain.repository

import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.domain.model.zip.ZipCreateModel
import umc.link.zip.util.network.NetworkResult

interface ZipRepository {
    suspend fun postCreateZip(createRequest: ZipCreateRequest): NetworkResult<ZipCreateModel>
}
