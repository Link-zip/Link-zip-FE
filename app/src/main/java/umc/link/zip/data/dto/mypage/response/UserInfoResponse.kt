package umc.link.zip.data.dto.mypage.response

import umc.link.zip.domain.model.mypage.UserInfoModel

data class UserInfoResponse(
    var userId: Int,
    var nickname: String,
    var kakaoId: Long,
    var createdAt: String
){
    fun toModel() = UserInfoModel (
        nickname = nickname
    )
}
