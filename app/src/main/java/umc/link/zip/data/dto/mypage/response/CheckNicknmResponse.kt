package umc.link.zip.data.dto.mypage.response

import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.domain.model.mypage.CheckNicknmModel

data class CheckNicknmResponse(
    var isValid : Boolean
) {
    fun toNicknmModel() = CheckNicknmModel(
        availablity = isValid
    )
}