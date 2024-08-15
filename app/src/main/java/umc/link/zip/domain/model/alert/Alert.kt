package umc.link.zip.domain.model.alert

data class Alert(
        val id: Int,           // 링크 id
        val alert_status: Int, // 0: 미확인, 1: 확인
        val alert_type: String, // "original" 또는 "reminder"
        val alert_date: String, // ISO 8601 형식 '2024-03-20T15:50:20Z'
        val alert_title: String, // 제목
        val alert_content: String // 내용
)
