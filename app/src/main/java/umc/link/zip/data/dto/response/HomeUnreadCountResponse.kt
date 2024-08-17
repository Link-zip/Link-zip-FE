package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.home.HomeUnreadCountModel

data class HomeUnreadCountResponse(
    val unread_links_count : Int
) {
    fun toModel() = HomeUnreadCountModel(
        unread_links_count = this.unread_links_count
    )
}
