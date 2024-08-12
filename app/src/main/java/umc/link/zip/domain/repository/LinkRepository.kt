package umc.link.zip.domain.repository

import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.data.dto.zip.request.ZipEditRequest
import umc.link.zip.data.dto.zip.request.ZipRmRequest
import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.domain.model.zip.ZipCreateModel
import umc.link.zip.domain.model.zip.ZipEditModel
import umc.link.zip.domain.model.zip.ZipInquiryModel
import umc.link.zip.domain.model.zip.ZipRmModel
import umc.link.zip.util.network.NetworkResult

interface LinkRepository {
    suspend fun getLinkData(zip_id: Int, tag: String): NetworkResult<LinkGetModel>
}