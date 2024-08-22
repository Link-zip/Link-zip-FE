package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.home.HomeTotalCountModel

data class HomeTotalCountResponse(
    val total_links_count: Int
) {
    fun toModel() = HomeTotalCountModel(
        total_links_count = this.total_links_count
    )
}
