package umc.link.zip.data.dto.link.response

import umc.link.zip.domain.model.link.LinkGetByLinkIDModel

data class LinkGetByLinkIDResponse(
    val alert_date: String,
    val created_at: String,
    val id: Int,
    val like: Int,
    val memo: String,
    val tag: String,
    val text: String,
    val thumb: String,
    val title: String,
    val updated_at: String,
    val url: String,
    val user_id: Int,
    val visit: Int,
    val visit_date: String,
    val zip_color: String,
    val zip_id: Int,
    val zip_title: String
){
    fun toModel() = LinkGetByLinkIDModel(
        alert_date = this.alert_date,
        created_at = this.created_at,
        id = this.id,
        like = this.like,
        memo = this.memo,
        tag = this.tag,
        text = this.text,
        thumb = this.thumb,
        title = this.title,
        updated_at = this.updated_at,
        url = this.url,
        user_id = this.user_id,
        visit = this.visit,
        visit_date = this.visit_date,
        zip_id = this.zip_id,
        zip_color = this.zip_color,
        zip_title = this.zip_title
    )
}