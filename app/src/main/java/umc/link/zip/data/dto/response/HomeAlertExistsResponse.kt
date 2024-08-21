package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.home.HomeAlertExistsModel

data class HomeAlertExistsResponse (
    val uncomfirmedAlert: Boolean
) {
    fun toModel() = HomeAlertExistsModel(
        uncomfirmedAlert = this.uncomfirmedAlert
    )
}