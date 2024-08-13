package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.login.SignupModel

data class SignupResponse(
    val accessToken: String
) {
    fun toModel() = SignupModel(
        accessToken = this.accessToken
    )
}