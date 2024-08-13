package umc.link.zip.domain.model.login

data class LoginModel(
    val success: Boolean,
    val message: String,
    val userId: Int? = null
)
