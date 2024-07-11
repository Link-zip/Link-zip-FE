package umc.link.zip.domain.repository

import umc.link.zip.data.dto.request.TestRequest
import umc.link.zip.domain.model.TestModel
import umc.link.zip.util.network.NetworkResult

interface TestRepository {
    suspend fun fetchTest(request: TestRequest): NetworkResult<TestModel>
}