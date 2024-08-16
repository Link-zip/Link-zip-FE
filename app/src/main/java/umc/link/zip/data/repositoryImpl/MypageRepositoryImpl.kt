package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.mypage.request.CheckNicknmRequest
import umc.link.zip.data.dto.mypage.response.CheckNicknmResponse
import umc.link.zip.data.dto.mypage.response.UserInfoResponse
import umc.link.zip.data.dto.request.TestRequest
import umc.link.zip.data.dto.response.TestResponse
import umc.link.zip.data.service.MypageService
import umc.link.zip.domain.model.TestModel
import umc.link.zip.domain.model.mypage.CheckNicknmModel
import umc.link.zip.domain.model.mypage.UserInfoModel
import umc.link.zip.domain.repository.MypageRepository
import umc.link.zip.domain.repository.TestRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class MypageRepositoryImpl @Inject constructor(
    private val mypageService: MypageService
) : MypageRepository {
    override suspend fun checkNicknm(request: CheckNicknmRequest): NetworkResult<CheckNicknmModel> {
        return handleApi({mypageService.checkNickname(request)}) {response: BaseResponse<CheckNicknmResponse> -> response.result.toNicknmModel()} // mapper를 통해 api 결과를 TestModel로 매핑
    }
    override suspend fun getUserInfo(): NetworkResult<UserInfoModel> {
        return handleApi({mypageService.getUserInfo()}) {response: BaseResponse<UserInfoResponse> -> response.result.toModel()}
    }
}