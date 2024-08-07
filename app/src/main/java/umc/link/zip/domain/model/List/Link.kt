package umc.link.zip.domain.model.List

data class Link(
    val id: String,
    val title: String,
    val url: String,
    val text: String,
    val thumbnail: String,
    val likes: Int,
    val createdAt: String,
    val zip: Zip
)
