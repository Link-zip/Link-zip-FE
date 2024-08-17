package umc.link.zip.domain.model.notice

import java.time.LocalDateTime

data class Notice(
    val id: Int,
    val title: String,
    val content: String,
    val createdAt: String,
    val updatedAt: String
)