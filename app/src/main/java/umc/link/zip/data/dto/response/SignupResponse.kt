package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.login.TokenModel

data class SignupResponse(
    val accessToken: String,
    val accessTokenExpires: String,
    val refreshToken: String,
    val refreshTokenExpires: String,
) {
    fun toModel() = TokenModel(
        accessToken = this.accessToken,
        accessTokenExpires = this.accessTokenExpires,
        refreshToken = this.refreshToken,
        refreshTokenExpires = this.refreshTokenExpires
    )
}