package umc.link.zip.data.dto

data class BaseResponse<T>(
    val status: Int,
    val code: String,
    val message: String,
    val isSuccess : Boolean,
    val result : T //result로 통일 필요
)