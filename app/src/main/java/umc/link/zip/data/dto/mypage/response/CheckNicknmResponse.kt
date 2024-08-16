package umc.link.zip.data.dto.mypage.response

import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.domain.model.mypage.CheckNicknmModel

data class CheckNicknmResponse(
    var availablity : Boolean
) {
    fun toNicknmModel() = CheckNicknmModel(
        availablity = availablity
    )
}