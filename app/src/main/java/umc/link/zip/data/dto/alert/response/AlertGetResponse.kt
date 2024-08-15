package umc.link.zip.data.dto.alert.response

import umc.link.zip.domain.model.alert.AlertAddModel
import umc.link.zip.domain.model.alert.AlertGetModel

data class AlertGetResponse(
    val message: String
){
    fun toModel() = AlertGetModel(
        message = this.message
    )
}