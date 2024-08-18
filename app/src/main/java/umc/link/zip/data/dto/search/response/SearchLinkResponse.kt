package umc.link.zip.data.dto.search.response


import umc.link.zip.domain.model.list.Link
import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.domain.model.list.Zip

data class SearchLinkResponse(
    val links: List<UnreadResponse>
) {
    data class UnreadResponse(
        val id: String,
        val title: String,
        val url: String,
        val tag: String,
        val thumbnail: String,
        val like: Int,
        val createdAt: String,
        val zip: ZipResponse
    ) {
        fun toLinkModel() = Link(
            id = id,
            title = title,
            url = url,
            tag = tag,
            thumbnail = thumbnail,
            like = like,
            createdAt = createdAt,
            zip = zip.toZipModel()
        )

        data class ZipResponse(
            val id: String,
            val title: String,
            val color: String
        ) {
            fun toZipModel() = Zip(
                id = id,
                title = title,
                color = color
            )
        }
    }
    fun toLinkModelList() = UnreadModel(links.map { it.toLinkModel() })
}