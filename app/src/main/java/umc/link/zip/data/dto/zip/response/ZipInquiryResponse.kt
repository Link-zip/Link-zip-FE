package umc.link.zip.data.dto.zip.response

import umc.link.zip.domain.model.zip.ZipCreateModel
import umc.link.zip.domain.model.zip.ZipInquiryModel
import umc.link.zip.domain.model.zip.ZipInquiryItemModel

class ZipInquiryResponse : ArrayList<ZipInquiryItemResponse>() {
    fun toModel() = ZipInquiryModel(
    )

}

