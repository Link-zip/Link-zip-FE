package umc.link.zip.data.dto.zip.response

import umc.link.zip.domain.model.zip.ZipCreateModel
import umc.link.zip.domain.model.zip.ZipRmModel

data class ZipRmResponse(
    val message: String
){
    fun toModel() = ZipRmModel(
        message = this.message
    )

}