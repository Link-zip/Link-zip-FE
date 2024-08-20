package umc.link.zip.domain.model.search

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = SearchKeywordEntity.TABLE_NAME)
data class SearchKeywordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val keyword: String,
    val timestamp: Long
) {

    fun toSearchRecent(): SearchRecent {
        return SearchRecent(
            id = id,
            keyword = keyword,
        )
    }

    companion object {
        const val TABLE_NAME = "search_keyword"
    }

}
