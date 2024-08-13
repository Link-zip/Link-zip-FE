package umc.link.zip.data.dto.link.response

import umc.link.zip.domain.model.link.LinkGetModel

class LinkGetResponse : ArrayList<LinkGetItemResponse>(){
    fun toModel() = LinkGetModel(
    )
}