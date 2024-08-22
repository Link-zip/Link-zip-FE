package umc.link.zip.data.dto.link.response

import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.domain.model.link.MoveLinkToNewZipModel

data class LinkGetResponse(
    val link_data: List<LinkGetItemResponse>
){
    fun toModel() = LinkGetModel(
        link_data = link_data.map { it.toModel() }
    )

}