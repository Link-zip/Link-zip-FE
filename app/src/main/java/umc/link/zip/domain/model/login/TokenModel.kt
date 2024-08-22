package umc.link.zip.domain.model.login

data class TokenModel(
    val accessToken: String,
    val accessTokenExpires: String,
    val refreshToken: String,
    val refreshTokenExpires: String,
)
