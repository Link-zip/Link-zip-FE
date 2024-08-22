package umc.link.zip.data.dto.link.request

data class LinkAddRequest(
    val alert_date: String?,
    val memo: String,
    val text: String?,
    val title: String,
    val url: String,
    val zip_id: Int
)