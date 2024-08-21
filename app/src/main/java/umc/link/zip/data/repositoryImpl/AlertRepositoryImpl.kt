package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.alert.request.AlertAddRequest
import umc.link.zip.data.dto.alert.response.AlertAddResponse
import umc.link.zip.data.dto.alert.response.AlertConfirmResponse
import umc.link.zip.data.dto.alert.response.AlertGetResponse
import umc.link.zip.data.dto.link.request.LinkExtractRequest
import umc.link.zip.data.dto.link.response.LinkGetByLinkIDResponse
import umc.link.zip.data.service.AlertService
import umc.link.zip.domain.model.alert.AlertAddModel
import umc.link.zip.domain.model.alert.AlertConfirmModel
import umc.link.zip.domain.model.alert.AlertGetModel
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.domain.model.link.LinkGetByLinkIDModel
import umc.link.zip.domain.repository.AlertRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class AlertRepositoryImpl @Inject constructor(
    private val alertService: AlertService
) : AlertRepository {

    override suspend fun ConfirmAlert(
        alertId: Int
    ): NetworkResult<AlertConfirmModel> {
        return handleApi({ alertService.putConfirmAlert(alertId) }) { response: BaseResponse<AlertConfirmResponse> -> response.result.toModel() }
    }

    override suspend fun AddAlert(
        alertAddRequest: AlertAddRequest
    ): NetworkResult<AlertAddModel> {
        return handleApi({ alertService.postAddAlert(alertAddRequest) }) { response: BaseResponse<AlertAddResponse> -> response.result.toModel() }
    }

    override suspend fun GetAlert(
    ): NetworkResult<AlertGetModel> {
        return handleApi({ alertService.getAlert() }) { response: BaseResponse<AlertGetResponse> -> response.result.toModel() }
    }
}