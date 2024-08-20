package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.request.TestRequest
import umc.link.zip.data.dto.response.TestResponse
import umc.link.zip.data.dto.search.response.SearchLinkResponse
import umc.link.zip.data.service.SearchService
import umc.link.zip.data.service.TestService
import umc.link.zip.domain.model.TestModel
import umc.link.zip.domain.model.search.SearchResult
import umc.link.zip.domain.repository.SearchRepository
import umc.link.zip.domain.repository.TestRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val searchService: SearchService
) : SearchRepository {
    override suspend fun searchLink(keyword: String): NetworkResult<SearchResult> {
        return handleApi({searchService.searchLink(keyword)}) {response: BaseResponse<SearchLinkResponse> -> response.result.toModel()} // mapper를 통해 api 결과를 TestModel로 매핑
    }
}