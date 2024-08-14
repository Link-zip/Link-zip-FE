package umc.link.zip.domain.model.search

import java.time.LocalDateTime

data class SearchResult (
    val id: Int,
    val zipId: Int,
    val userId: Int,
    val title: String,
    val url: String,
    val text: String,
    val memo: String,
    val tag: String,
    val alertDate: LocalDateTime?,
    val thumb: String,
    val like: Int,
    val visit: Int,
    val visitDate: LocalDateTime?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)