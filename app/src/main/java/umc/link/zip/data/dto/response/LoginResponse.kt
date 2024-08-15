package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.login.LoginModel

data class LoginResponse(
    val accessToken : String
) {
    fun toModel() = LoginModel(
        accessToken = this.accessToken
    )
}
