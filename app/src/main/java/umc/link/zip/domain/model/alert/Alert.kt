package umc.link.zip.domain.model.alert

data class Alert(
        val id: Int,           // 링크 id
        val alarm_status: Int, // 0: 미확인, 1: 확인
        val alarm_type: String, // "original" 또는 "reminder"
        val alarm_date: String, // ISO 8601 형식 '2024-03-20T15:50:20Z'
        val alarm_title: String, // 제목
        val alarm_content: String // 내용
)
