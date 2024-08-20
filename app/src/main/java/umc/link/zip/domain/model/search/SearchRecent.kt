package umc.link.zip.domain.model.search

data class SearchRecent(
    val id: Int,
    var keyword: String
)
fun SearchRecent.toEntity(): SearchKeywordEntity {
    return SearchKeywordEntity(
        id = this.id,
        keyword = this.keyword,
        timestamp = System.currentTimeMillis() // 새로운 검색 시점으로 갱신
    )
}
