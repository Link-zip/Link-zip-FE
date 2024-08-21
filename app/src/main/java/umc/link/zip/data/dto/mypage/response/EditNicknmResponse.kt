package umc.link.zip.data.dto.mypage.response

import umc.link.zip.domain.model.mypage.EditNicknmModel

data class EditNicknmResponse (
    val userId: Int,
    val nickname: String,
    val updatedAt: String
){
    fun toModel() = EditNicknmModel(userId, nickname, updatedAt)
}