package umc.link.zip.domain.model.link

data class LinkGetByLinkIDModel(
    val alert_date: String,
    val created_at: String,
    val id: Int,
    var like: Int,
    val memo: String,
    val tag: String,
    val text: String,
    val thumb: String?,
    val title: String,
    var updated_at: String?,
    val url: String,
    val user_id: Int,
    val visit: Int,
    var visit_date: String?,
    val zip_color: String,
    val zip_id: Int,
    val zip_title: String
)