package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.home.HomeAlertCountModel

data class HomeAlertCountResponse(
    val recent_alerts_count : Int
) {
    fun toModel() = HomeAlertCountModel(
        recent_alerts_count = this.recent_alerts_count
    )
}
