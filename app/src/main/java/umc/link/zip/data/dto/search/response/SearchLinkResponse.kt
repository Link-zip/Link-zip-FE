package umc.link.zip.data.dto.search.response


import umc.link.zip.domain.model.search.Link
import umc.link.zip.domain.model.search.SearchLinkResult
import umc.link.zip.domain.model.search.SearchResult
import umc.link.zip.domain.model.search.Zip

data class SearchLinkResponse(
    val links: List<SearchResponse>
) {
    data class SearchResponse(
        val link: LinkResponse,
        val zip: ZipResponse
    ) {
        data class LinkResponse(
            val id: Int,
            val title: String,
            val url: String,
            val tag: String,
            val thumbnail: String,
            val like: Int,
            val createdAt: String,
        ) {
            fun toLinkModel() = Link(
                id = id,
                title = title,
                url = url,
                tag = tag,
                thumbnail = thumbnail,
                like = like,
                createdAt = createdAt
            )
        }

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
        fun toLinkModelList() = SearchLinkResult(link.toLinkModel(),zip.toZipModel())
    }
    fun toModel() = SearchResult(links.map { it.toLinkModelList() })
}