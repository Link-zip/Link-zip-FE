package umc.link.zip.data.dto.link.response

import umc.link.zip.domain.model.link.LinkSummaryModel

data class LinkSummaryResponse(
    val summary: String
){
    fun toModel() = LinkSummaryModel(
        summary = this.summary
    )
}