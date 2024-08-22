package umc.link.zip.data.dto.alert.response

import umc.link.zip.domain.model.alert.AlertGetModel
import umc.link.zip.domain.model.alert.AlertModel
import umc.link.zip.domain.model.alert.Link

data class AlertGetResponse(
    val newAlerts: List<AlertGetLink>
) {
    data class AlertGetLink(
        val alert_date: String,
        val alert_id: Int,
        val alert_status: Int,
        val alert_type: String,
        val link: LinkResponse
    ) {
        fun toModel() = AlertGetModel(
            alert_date = this.alert_date,
            alert_id = this.alert_id,
            alert_status = this.alert_status,
            alert_type = this.alert_type,
            link = link.toLinkModel()
        )

        data class LinkResponse(
            val id: Int,
            val memo: String,
            val title: String
        ) {
            fun toLinkModel() = Link(
                id = id,
                memo = memo,
                title = title
            )
        }
    }
    fun toAlertGetModelList() = AlertModel(newAlerts.map { it.toModel() })
}