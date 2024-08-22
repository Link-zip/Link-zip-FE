package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.login.TokenModel

data class SignupResponse(
    val accessToken: String,
    val accessTokenExpiresAt: String,
    val refreshToken: String,
    val refreshTokenExpiresAt: String,
) {
    fun toModel() = TokenModel(
        accessToken = this.accessToken,
        accessTokenExpiresAt = this.accessTokenExpiresAt,
        refreshToken = this.refreshToken,
        refreshTokenExpiresAt = this.refreshTokenExpiresAt
    )
}