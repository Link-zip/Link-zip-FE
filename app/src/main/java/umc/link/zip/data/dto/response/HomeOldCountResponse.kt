package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.home.HomeAlertCountModel
import umc.link.zip.domain.model.home.HomeOldCountModel

data class HomeOldCountResponse(
    val oldLinksCount : Int
) {
    fun toModel() = HomeOldCountModel(
        oldLinksCount = this.oldLinksCount
    )
}
