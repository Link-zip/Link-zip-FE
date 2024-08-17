package umc.link.zip.data.dto.link.response

import umc.link.zip.domain.model.link.LinkVisitModel

data class VisitLinkResponse(
    val link_id: Int,
    val visit: Int,
    val visit_date: String
){
    fun toModel() = LinkVisitModel(
        link_id = this.link_id,
        visit = this.visit,
        visit_date = this.visit_date
    )
}