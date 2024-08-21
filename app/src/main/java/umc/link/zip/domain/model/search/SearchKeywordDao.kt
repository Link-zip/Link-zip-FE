package umc.link.zip.domain.model.search

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.*
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchKeywordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // 올바른 사용법
    suspend fun insertKeyword(keyword: SearchKeywordEntity)

    @Delete
    suspend fun deleteKeyword(keyword: SearchKeywordEntity)

    @Query("SELECT * FROM ${SearchKeywordEntity.TABLE_NAME} ORDER BY timestamp DESC LIMIT 10")
    fun getKeywordList(): Flow<List<SearchKeywordEntity>>

    @Query("DELETE FROM ${SearchKeywordEntity.TABLE_NAME} WHERE id NOT IN (SELECT id FROM ${SearchKeywordEntity.TABLE_NAME} ORDER BY timestamp DESC LIMIT 10)")
    suspend fun deleteOldKeyword(): Int

    @Query("DELETE FROM ${SearchKeywordEntity.TABLE_NAME}")
    suspend fun deleteKeywordList(): Int
}
