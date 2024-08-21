package umc.link.zip.domain.model.search

import kotlinx.coroutines.flow.Flow

interface KeywordLocalSource {
    suspend fun setKeyword(keyword: SearchKeywordEntity)
    suspend fun deleteKeyword(keyword: SearchKeywordEntity)
    fun getKeywordList(): Flow<List<SearchKeywordEntity>>
    suspend fun deleteOldKeyword()
    suspend fun deleteKeywordList()
}
