package umc.link.zip.domain.model

data class Link(
    var id: Int = 0,
    var title: String = "",
    var url: String = "",
    var text: String = "",
    var thumbnail: Int = 0,
    var likes: Int = 0,
    var createdAt: String = "",
)
