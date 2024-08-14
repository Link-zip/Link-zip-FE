package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.home.HomeUnreadCountModel

data class HomeUnreadCountResponse(
    val unreadLinksCount : Int
) {
    fun toModel() = HomeUnreadCountModel(
        unreadLinksCount = this.unreadLinksCount
    )
}
