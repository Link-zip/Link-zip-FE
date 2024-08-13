package umc.link.zip.data.dto.zip.response

import umc.link.zip.domain.model.zip.ZipGetModel

data class ZipGetResponse(
    val zips: List<ZipGetItemResponse>
) {
    fun toModel() = ZipGetModel(
        zips = zips.map { it.toModel() }
    )
}
