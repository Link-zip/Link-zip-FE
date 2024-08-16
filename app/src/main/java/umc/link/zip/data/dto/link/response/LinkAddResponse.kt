package umc.link.zip.data.dto.link.response

import umc.link.zip.domain.model.link.LinkAddModel

data class LinkAddResponse(
    val link_id: Int
){
    fun toModel() = LinkAddModel(
        link_id = this.link_id
    )
}