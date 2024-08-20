package umc.link.zip.domain.repository

import umc.link.zip.domain.model.search.SearchResult
import umc.link.zip.util.network.NetworkResult

interface SearchRepository {
    suspend fun searchLink(keyword: String): NetworkResult<SearchResult>
}