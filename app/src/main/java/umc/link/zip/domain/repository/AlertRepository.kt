package umc.link.zip.domain.repository

import umc.link.zip.data.dto.alert.request.AlertAddRequest
import umc.link.zip.data.dto.link.request.LinkExtractRequest
import umc.link.zip.domain.model.alert.AlertAddModel
import umc.link.zip.domain.model.alert.AlertConfirmModel
import umc.link.zip.domain.model.alert.AlertGetModel
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.util.network.NetworkResult

interface AlertRepository {
    suspend fun ConfirmAlert(alertId: Int): NetworkResult<AlertConfirmModel>
    suspend fun AddAlert(alertAddRequest: AlertAddRequest): NetworkResult<AlertAddModel>
    suspend fun GetAlert(): NetworkResult<AlertGetModel>
}