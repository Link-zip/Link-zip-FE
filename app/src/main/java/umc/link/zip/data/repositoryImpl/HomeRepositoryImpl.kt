package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.response.HomeAlertCountResponse
import umc.link.zip.data.dto.response.HomeRecentResponse
import umc.link.zip.data.dto.response.HomeUnreadCountResponse
import umc.link.zip.data.service.HomeService
import umc.link.zip.domain.model.home.HomeAlertCountModel
import umc.link.zip.domain.model.home.HomeRecentModel
import umc.link.zip.domain.model.home.HomeUnreadCountModel
import umc.link.zip.domain.repository.HomeRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val homeService: HomeService
) : HomeRepository {
    override suspend fun getRecentLinks(): NetworkResult<HomeRecentModel> {
        return handleApi({ homeService.getRecentList() }) { response: BaseResponse<HomeRecentResponse> ->
            response.result.toLinkModelList()
        }
    }

    override suspend fun getAlertCount(): NetworkResult<HomeAlertCountModel> {
        return handleApi({ homeService.getAlertCount() }) { response: BaseResponse<HomeAlertCountResponse> ->
            response.result.toModel()
        }
    }

    override suspend fun getUnreadCount(): NetworkResult<HomeUnreadCountModel> {
        return handleApi({ homeService.getUnreadCount() }) { response: BaseResponse<HomeUnreadCountResponse> ->
            response.result.toModel()
        }
    }
}