package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.login.NameCheckModel

data class NameCheckResponse(
    val isValid: Boolean
) {
    fun toModel() = NameCheckModel(
        isValid = this.isValid
    )
}