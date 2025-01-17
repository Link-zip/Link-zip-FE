package umc.link.zip.data.dto.alert.response

import umc.link.zip.domain.model.alert.AlertConfirmModel

data class AlertConfirmResponse(
    val message: String
){
    fun toModel() = AlertConfirmModel(
        message = this.message
    )
}