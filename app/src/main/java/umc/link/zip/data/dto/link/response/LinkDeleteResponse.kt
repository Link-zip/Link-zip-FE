package umc.link.zip.data.dto.link.response

import umc.link.zip.domain.model.link.LinkDeleteModel

data class LinkDeleteResponse(
    val message: String
){
    fun toModel() = LinkDeleteModel(
        message = this.message
    )
}