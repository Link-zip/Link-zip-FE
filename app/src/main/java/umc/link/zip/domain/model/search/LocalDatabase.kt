package umc.link.zip.domain.model.search

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [SearchKeywordEntity::class], version = 1, exportSchema = false)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun getSearchKeywordDao(): SearchKeywordDao
}