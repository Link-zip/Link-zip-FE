package umc.link.zip.domain.repository

import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.data.dto.zip.request.ZipEditRequest
import umc.link.zip.domain.model.zip.ZipCreateModel
import umc.link.zip.domain.model.zip.ZipEditModel
import umc.link.zip.domain.model.zip.ZipInquiryModel
import umc.link.zip.util.network.NetworkResult

interface ZipRepository {
    suspend fun postCreateZip(createRequest: ZipCreateRequest): NetworkResult<ZipCreateModel>
    suspend fun getInquiryZip(sort: String): NetworkResult<ZipInquiryModel>
    suspend fun patchEditZip(editRequest: ZipEditRequest) : NetworkResult<ZipEditModel>
}