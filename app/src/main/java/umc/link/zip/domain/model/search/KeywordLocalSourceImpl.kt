package umc.link.zip.domain.model.search

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class KeywordLocalSourceImpl @Inject constructor(
    private val searchKeywordDao: SearchKeywordDao
) : KeywordLocalSource {

    override suspend fun setKeyword(keyword: SearchKeywordEntity) {
        searchKeywordDao.insertKeyword(keyword)
    }

    override suspend fun deleteKeyword(keyword: SearchKeywordEntity) {
        searchKeywordDao.deleteKeyword(keyword)
    }

    override fun getKeywordList(): Flow<List<SearchKeywordEntity>> {
        return searchKeywordDao.getKeywordList()
    }

    override suspend fun deleteOldKeyword() {
        searchKeywordDao.deleteOldKeyword()
    }

    override suspend fun deleteKeywordList() {
        searchKeywordDao.deleteKeywordList()
    }
}
