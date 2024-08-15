package umc.link.zip.data.dto.link.response

import umc.link.zip.domain.model.link.MoveLinkToNewZipModel

data class MoveLinkToNewZipResponse(
    val link_id: Int,
    val new_zip_id: Int
){
    fun toModel() = MoveLinkToNewZipModel(
        link_id = this.link_id,
        new_zip_id = this.new_zip_id
    )

}
