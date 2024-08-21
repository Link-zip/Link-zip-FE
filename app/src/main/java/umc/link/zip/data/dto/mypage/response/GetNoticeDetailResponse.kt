package umc.link.zip.data.dto.mypage.response

import umc.link.zip.domain.model.notice.Notice

data class GetNoticeDetailResponse (
    val id: Int,
    val title: String,
    val content: String,
    val created_at: String,
    val updated_at: String
){
    fun toModel() = Notice(id,title,content,created_at,updated_at)
}