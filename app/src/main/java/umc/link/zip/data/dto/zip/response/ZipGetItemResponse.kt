package umc.link.zip.data.dto.zip.response

import umc.link.zip.domain.model.zip.ZipGetItemModel

data class ZipGetItemResponse(
    val user_id: Int,
    val zip_id: Int,
    val title: String,
    val color: String,
    val link_count: Int
) {
    fun toModel() = ZipGetItemModel(
        user_id = this.user_id,
        zip_id = this.zip_id,
        title = this.title,
        color = this.color,
        link_count = this.link_count
    )
}