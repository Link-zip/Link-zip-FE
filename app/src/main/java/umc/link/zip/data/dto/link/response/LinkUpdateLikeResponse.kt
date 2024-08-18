package umc.link.zip.data.dto.link.response

import umc.link.zip.domain.model.link.LinkUpdateLikeModel

data class LinkUpdateLikeResponse(
    val like: Int,
    val link_id: Int
){
    fun toModel() = LinkUpdateLikeModel(
        like = this.like,
        link_id = this.link_id
    )
}