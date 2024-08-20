package umc.link.zip.domain.model.search

data class Link(
    val id: Int,
    var title: String,
    var url: String,
    var tag: String,
    var thumbnail: String?,
    var like: Int,
    var createdAt: String,
)