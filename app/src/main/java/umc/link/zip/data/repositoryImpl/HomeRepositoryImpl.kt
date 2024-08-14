package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.response.HomeRecentResponse
import umc.link.zip.data.service.HomeService
import umc.link.zip.domain.model.home.HomeRecentModel
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
}