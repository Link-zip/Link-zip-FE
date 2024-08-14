package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.home.HomeAlertCountModel

data class HomeAlertCountResponse(
    val recentAlertsCount : Int
) {
    fun toModel() = HomeAlertCountModel(
        recentAlertsCount = this.recentAlertsCount
    )
}
