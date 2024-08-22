package umc.link.zip.data.dto.alert.response

import umc.link.zip.domain.model.alert.AlertGetModel

data class AlertGetResponse(
    val alert_date: String,
    val alert_id: Int,
    val alert_status: Int,
    val alert_type: String,
    val link: Link
){
    fun toModel() = AlertGetModel(
        alert_date = this.alert_date,
        alert_id = this.alert_id,
        alert_status = this.alert_status,
        alert_type = this.alert_type,
        link = this.link
    )
}