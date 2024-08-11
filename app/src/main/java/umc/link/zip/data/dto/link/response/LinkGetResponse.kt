package umc.link.zip.data.dto.link.response

import LinkGetModel
import umc.link.zip.domain.model.zip.ZipInquiryModel

class LinkGetResponse : ArrayList<LinkGetResponseItem>(){
    fun toModel() = LinkGetModel(
    )
}