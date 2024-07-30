package umc.link.zip.util.extension

// 카테고리 -> 포지션 확장함수
fun String.toCategoryPosition(): Int{
    return when (this) {
        "미열람 링크" -> 0
        "좋아요 한 링크" -> 1
        "최근 저장한 링크" -> 2
        else -> 0
    }
}