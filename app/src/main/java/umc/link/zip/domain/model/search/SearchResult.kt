package umc.link.zip.domain.model.search

import java.time.LocalDateTime

data class SearchResult (
    val id: Int,
    val linkId: Int,
    val zipId: Int,
    val zipColor: String,
    val zipName: String,
    val title: String,
    val tag: String,
    val thumb: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val like: Int
)