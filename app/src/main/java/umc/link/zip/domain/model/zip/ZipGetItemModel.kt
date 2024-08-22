package umc.link.zip.domain.model.zip

data class ZipGetItemModel(
    val color: String,
    val link_count: Int?,
    val title: String,
    val user_id: Int,
    val zip_id: Int
)