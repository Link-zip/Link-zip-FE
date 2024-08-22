package umc.link.zip.data.dto.mypage.response

import umc.link.zip.domain.model.mypage.WithdrawalModel


data class WithdrawalResponse(
    var isDeleted: Boolean
){
    fun toModel() = WithdrawalModel (
        isDeleted = isDeleted
    )
}