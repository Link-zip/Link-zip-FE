package umc.link.zip.data.dto.link.response

import umc.link.zip.domain.model.link.LinkModifyModel

data class LinkModifyResponse(
    val alert_date: String,
    val memo: String,
    val message: String,
    val text: String,
    val title: String
){
    fun toModel() = LinkModifyModel(
        alert_date = this.alert_date,
        memo = this.memo,
        message = this.message,
        text = this.text,
        title = this.title
    )
}