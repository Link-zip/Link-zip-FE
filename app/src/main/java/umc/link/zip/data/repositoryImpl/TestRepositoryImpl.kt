package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.request.TestRequest
import umc.link.zip.data.dto.response.TestResponse
import umc.link.zip.data.service.TestService
import umc.link.zip.domain.model.TestModel
import umc.link.zip.domain.repository.TestRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class TestRepositoryImpl @Inject constructor(
    private val testService: TestService
) : TestRepository {
    override suspend fun fetchTest(request: TestRequest): NetworkResult<TestModel> {
        return handleApi({testService.fetchTest(request)}) {response: BaseResponse<TestResponse> -> response.data.toTestModel()} // mapper를 통해 api 결과를 TestModel로 매핑
    }
}