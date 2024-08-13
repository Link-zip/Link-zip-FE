package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.login.LoginModel

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val userId: Int? = null
) {
    fun toModel() = LoginModel(
        success = this.success,
        message = this.message,
        userId = this.userId
    )
}
