package umc.link.zip.data.dto.zip.response

import umc.link.zip.domain.model.zip.ZipCreateModel
import umc.link.zip.domain.model.zip.ZipEditModel

data class ZipEditResponse(
    val message: String
){
    fun toModel() = ZipEditModel(
        message = this.message
    )

}