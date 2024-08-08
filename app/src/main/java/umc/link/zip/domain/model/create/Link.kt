package umc.link.zip.domain.model.create

data class Link(
        val id: String,
        var title: String,
        var url: String,
        var text: String,
        var thumbnail: String,
        var likes: Int,
        var createdAt: String,
        var zip: Zip
)

