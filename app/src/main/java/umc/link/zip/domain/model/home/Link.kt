package umc.link.zip.domain.model.home

data class Link(
    val id: Int,
    val title: String,
    val url: String,
    val tag: String,
    val thumbnail: String,
    val like: Int,
    val createdAt: String,
    val zip: Zip
)
