package umc.link.zip.domain.model.login

data class LoginModel(
    val isExists: Boolean,
    val key: String?,
    val tokenResponse: TokenModel?
)
