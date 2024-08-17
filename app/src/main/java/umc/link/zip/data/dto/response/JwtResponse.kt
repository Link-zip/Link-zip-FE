package umc.link.zip.data.dto.response

import umc.link.zip.domain.model.login.JwtModel

data class JwtResponse(
    val userId: Int,
    val nickname: String,
    val kakaoId: Long,
    val createdAt: String
) {
    fun toModel() = JwtModel(
        userId = this.userId,
        nickname = this.nickname,
        kakaoId = this.kakaoId,
        createdAt = this.createdAt
    )
}
