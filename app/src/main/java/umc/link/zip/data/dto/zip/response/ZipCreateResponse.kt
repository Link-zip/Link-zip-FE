package umc.link.zip.data.dto.zip.response

import umc.link.zip.domain.model.zip.ZipCreateModel

data class ZipCreateResponse(
    val message : String
){
    fun toModel() = ZipCreateModel(
        message = this.message
    )
}