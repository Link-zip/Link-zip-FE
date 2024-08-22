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
        val accessTokenExpires: String,
        val refreshToken: String,
        val refreshTokenExpires: String,
    ) {
        fun toTokenModel() = TokenModel(
            accessToken = this.accessToken,
            accessTokenExpires = this.accessTokenExpires,
            refreshToken = this.refreshToken,
            refreshTokenExpires = this.refreshTokenExpires
        )
    }
}
