package umc.link.zip.data.dto.mypage.response

import umc.link.zip.domain.model.notice.Notice
import umc.link.zip.domain.model.notice.NoticeList

data class GetNoticeResponse (
    val notice_data: List<NoticeResponse>
)
{
    data class NoticeResponse(
        val id: Int,
        val title: String,
        val content: String,
        val created_at: String,
        val updated_at: String
    ) {
        fun toModel() = Notice(
            id = id,
            title = title,
            content = content,
            createdAt = created_at,
            updatedAt = updated_at
        )
    }
    fun toListModel() = NoticeList(notice_data.map { it.toModel() })
}