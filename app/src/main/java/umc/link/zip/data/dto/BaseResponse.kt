package umc.link.zip.data.dto

data class BaseResponse<T>(
    val isSuccess: Boolean,
    val status: Int,
    val code: String,
    val message: String,
    val isSuccess : Boolean,
    val result : T
)

