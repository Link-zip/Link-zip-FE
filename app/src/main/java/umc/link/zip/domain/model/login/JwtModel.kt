package umc.link.zip.domain.model.login

data class JwtModel(
    val userId: Int,
    val nickname: String,
    val kakaoId: Long,
    val createdAt: String
)
