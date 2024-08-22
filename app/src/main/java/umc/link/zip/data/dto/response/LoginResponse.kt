package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.login.LoginModel
import umc.link.zip.domain.model.login.TokenModel

data class LoginResponse(
    val isExists: Boolean,
    val key: String?,
    val tokenResponse: TokenResponse?
) {
    fun toModel() = LoginModel(
        isExists = this.isExists,
        key = this.key,
        tokenResponse = tokenResponse?.toTokenModel()
    )

    data class TokenResponse (
        val accessToken: String,
        val accessTokenExpiresAt: String,
        val refreshToken: String,
        val refreshTokenExpiresAt: String,
    ) {
        fun toTokenModel() = TokenModel(
            accessToken = this.accessToken,
            accessTokenExpiresAt = this.accessTokenExpiresAt,
            refreshToken = this.refreshToken,
            refreshTokenExpiresAt = this.refreshTokenExpiresAt
        )
    }
}
