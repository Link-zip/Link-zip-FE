package umc.link.zip.domain.model.Link

data class LinkGetItemModel(
    val alert_date: String,
    val created_at: String,
    val id: Int,
    val like: Int,
    val memo: String,
    val tag: String,
    val text: String,
    val thumb: String,
    val title: String,
    val updated_at: String,
    val url: String,
    val user_id: Int,
    val visit: Int,
    val visit_date: String,
    val zip_id: Int
)