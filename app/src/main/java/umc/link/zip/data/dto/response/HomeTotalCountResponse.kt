package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.home.HomeTotalCountModel

data class HomeTotalCountResponse(
    val totalLinksCount: Int
) {
    fun toModel() = HomeTotalCountModel(
        totalLinksCount = this.totalLinksCount
    )
}
