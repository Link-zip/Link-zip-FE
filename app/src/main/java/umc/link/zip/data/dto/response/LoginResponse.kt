package umc.link.zip.data.dto.response

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val userId: Int? = null // Example response fields, adjust as needed
)
