package umc.link.zip.domain.model.create

data class Link(
    var id: Int,
    var zipId: Int,
    var userId: Int,
    var title: String,
    var url: String,
    var text: String,
    var memo: String,
    var tag: String,
    var alertDate: String,
    var thumb: String?,
    var like: Int,
    var visit: Int,
    var visitDate: String,
    var createdAt: String,
    var updatedAt: String
)


