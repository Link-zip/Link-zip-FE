package umc.link.zip.data.dto

data class BaseResponse<T>(
    val status: Int,
    val code: String,
    val message: String,
    val result: T,
)