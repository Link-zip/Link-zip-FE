package umc.link.zip.data.dto.request

data class LoginRequest(
    val accessToken: String,
    val accessTokenExpires: String,
    val refreshToken: String,
    val refreshTokenExpires: String
)
