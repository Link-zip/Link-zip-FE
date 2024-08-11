package umc.link.zip.data.dto.zip.response

import umc.link.zip.domain.model.zip.ZipInquiryItemModel

data class ZipInquiryItemResponse(
    val color: String,
    val link_count: Int,
    val title: String,
    val user_id: Int,
    val zip_id: Int
){
    fun toModel() = ZipInquiryItemModel(
        color = this.color,
        link_count = this.link_count,
        title = this.title,
        user_id = this.user_id,
        zip_id = this.zip_id
    )
}