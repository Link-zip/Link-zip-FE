package umc.link.zip.domain.model.login

data class RefreshModel(
    val accessToken: String,
    val accessTokenExpiresAt: String
)
