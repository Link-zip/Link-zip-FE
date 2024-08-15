package umc.link.zip.data.dto.alert.response

import umc.link.zip.domain.model.alert.AlertAddModel
import umc.link.zip.domain.model.alert.AlertConfirmModel

data class AlertAddResponse(
    val message: String
){
    fun toModel() = AlertAddModel(
        message = this.message
    )
}