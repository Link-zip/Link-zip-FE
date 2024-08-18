package umc.link.zip.domain.model.login

data class NameCheckModel(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Boolean
)
