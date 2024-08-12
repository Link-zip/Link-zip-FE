package umc.link.zip.domain.model.list

data class Link(
    val id: String,
    var title: String,
    var url: String,
    var tag: String,
    var thumbnail: String,
    var like: Int,
    var createdAt: String,
    var zip: Zip
)
