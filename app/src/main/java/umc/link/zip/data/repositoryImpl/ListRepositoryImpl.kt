package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.list.request.UnreadRequest
import umc.link.zip.data.dto.list.response.UnreadResponseList
import umc.link.zip.data.service.ListService
import umc.link.zip.domain.model.list.Link
import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.domain.repository.ListRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class ListRepositoryImpl @Inject constructor(
    private val listService: ListService
) : ListRepository {
    override suspend fun getUnreadList(request: UnreadRequest): NetworkResult<UnreadModel> {
        return handleApi({ listService.getUnreadList(sort = request.sort,
            filter = request.filter) }) { response: BaseResponse<UnreadResponseList> -> response.result.toLinkModelList() // toLinkModelList()를 사용하여 List<Link>로 변환
        }
    }
    override suspend fun getLikeList(request: UnreadRequest): NetworkResult<UnreadModel> {
        return handleApi({ listService.getLikeList(sort = request.sort,
            filter = request.filter) }) { response: BaseResponse<UnreadResponseList> -> response.result.toLinkModelList() // toLinkModelList()를 사용하여 List<Link>로 변환
        }
    }
    override suspend fun getRecentList(request: UnreadRequest): NetworkResult<UnreadModel> {
        return handleApi({ listService.getRecentList(sort = request.sort,
            filter = request.filter) }) { response: BaseResponse<UnreadResponseList> -> response.result.toLinkModelList() // toLinkModelList()를 사용하여 List<Link>로 변환
        }
    }
}