package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.home.HomeRecentModel
import umc.link.zip.domain.model.home.Link
import umc.link.zip.domain.model.home.Zip

data class HomeRecentResponse(
    val links: List<RecentResponse>
) {
    data class RecentResponse(
        val id: Int,
        val title: String,
        val url: String,
        val tag: String,
        val thumbnail: String?,
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
            val id: Int,
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
    fun toLinkModelList() = HomeRecentModel(links.map { it.toLinkModel() })
}
