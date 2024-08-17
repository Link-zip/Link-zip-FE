package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.home.HomeAlertExistsModel

data class HomeAlertExistsResponse (
    val uncomfirmed_alert: Boolean
) {
    fun toModel() = HomeAlertExistsModel(
        uncomfirmed_alert = this.uncomfirmed_alert
    )
}