package umc.link.zip.data.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import umc.link.zip.data.dto.BaseResponse

interface TestService {
    // api 명세서의 api 주소 기입
    @GET("주소")
    suspend fun fetchTest(@Body request: TestRequest): Response<BaseResponse<TestResponse>>
}