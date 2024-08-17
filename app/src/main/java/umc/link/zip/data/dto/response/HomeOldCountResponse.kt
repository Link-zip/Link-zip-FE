package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.home.HomeAlertCountModel
import umc.link.zip.domain.model.home.HomeOldCountModel

data class HomeOldCountResponse(
    val old_links_count : Int
) {
    fun toModel() = HomeOldCountModel(
        old_links_count = this.old_links_count
    )
}
