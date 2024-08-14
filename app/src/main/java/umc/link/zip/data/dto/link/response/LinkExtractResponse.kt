package umc.link.zip.data.dto.link.response

import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.domain.model.zip.ZipCreateModel

data class LinkExtractResponse(
    val thumb: String,
    val title: String
){
    fun toModel() = LinkExtractModel(
        thumb = this.thumb,
        title = this.title
    )
}