package umc.link.zip.domain.model.alert

import umc.link.zip.domain.model.list.Link

data class AlertModel(
    val newAlerts: List<AlertGetModel>
)