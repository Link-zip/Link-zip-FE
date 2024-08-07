package umc.link.zip.domain.model

data class Link(
    var id: String,
    var title: String,
    var url: String,
    var text: String,
    var thumbnail: String,
    var likes: Int,
    var createdAt: String,
    var zip: Zip
)
