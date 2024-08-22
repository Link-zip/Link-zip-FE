package umc.link.zip.domain.model.alert

import umc.link.zip.data.dto.alert.response.Link

data class AlertGetModel(
    val alert_date: String?,
    val alert_id: Int,
    val alert_status: Int,
    val alert_type: String?,
    val link: Link
)