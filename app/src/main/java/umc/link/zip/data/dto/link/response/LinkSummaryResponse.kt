package umc.link.zip.data.dto.link.response

import umc.link.zip.domain.model.link.LinkSummaryModel

data class LinkSummaryResponse(
    val url_summary: String
){
    fun toModel() = LinkSummaryModel(
        summary = this.url_summary
    )
}