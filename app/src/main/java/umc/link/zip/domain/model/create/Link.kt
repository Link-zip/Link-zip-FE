package umc.link.zip.domain.model.create

data class Link(
        var zipId: Int,
        var title: String,
        var text: String,
        var url: String,
        var memo: String,
        var alertDate: String
)

