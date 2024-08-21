package umc.link.zip.data.dto.alert.request

data class AlertAddRequest(
    val alert_date: String,
    val alert_type: String,
    val linkId: Int
)