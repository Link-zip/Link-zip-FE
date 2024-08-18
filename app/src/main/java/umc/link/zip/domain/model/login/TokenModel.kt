package umc.link.zip.domain.model.login

data class TokenModel(
    val accessToken: String,
    val accessTokenExpiresIn: String,
)
