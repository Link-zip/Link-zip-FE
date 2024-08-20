package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.login.NameCheckModel

data class NameCheckResponse(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Boolean
) {
    fun toModel() = NameCheckModel(
        code = this.code,
        isSuccess = this.isSuccess,
        message = this.message,
        result = this.result
    )
}