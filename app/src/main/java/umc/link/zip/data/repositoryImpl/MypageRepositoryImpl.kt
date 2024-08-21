package umc.link.zip.data.repositoryImpl

import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.mypage.request.EditNicknmRequest
import umc.link.zip.data.dto.mypage.response.CheckNicknmResponse
import umc.link.zip.data.dto.mypage.response.EditNicknmResponse
import umc.link.zip.data.dto.mypage.response.GetNoticeDetailResponse
import umc.link.zip.data.dto.mypage.response.GetNoticeResponse
import umc.link.zip.data.dto.mypage.response.UserInfoResponse
import umc.link.zip.data.service.MypageService
import umc.link.zip.domain.model.mypage.CheckNicknmModel
import umc.link.zip.domain.model.mypage.EditNicknmModel
import umc.link.zip.domain.model.mypage.UserInfoModel
import umc.link.zip.domain.model.notice.Notice
import umc.link.zip.domain.model.notice.NoticeList
import umc.link.zip.domain.repository.MypageRepository
import umc.link.zip.util.network.NetworkResult
import umc.link.zip.util.network.handleApi
import javax.inject.Inject

class MypageRepositoryImpl @Inject constructor(
    private val mypageService: MypageService
) : MypageRepository {
    override suspend fun checkNicknm(nickname : String): NetworkResult<CheckNicknmModel> {
        return handleApi({mypageService.checkNickname(nickname)}) {response: BaseResponse<CheckNicknmResponse> -> response.result.toNicknmModel()} // mapper를 통해 api 결과를 TestModel로 매핑
    }
    override suspend fun getUserInfo(): NetworkResult<UserInfoModel> {
        return handleApi({mypageService.getUserInfo()}) {response: BaseResponse<UserInfoResponse> -> response.result.toModel()}
    }
    override suspend fun getNotice(): NetworkResult<NoticeList> {
        return handleApi({mypageService.getNotice()}) {response: BaseResponse<GetNoticeResponse> -> response.result.toListModel()}
    }
    override suspend fun getNoticeDetail(noticeId: Int): NetworkResult<Notice> {
        return handleApi({mypageService.getNoticeDetail(noticeId)}) {response: BaseResponse<GetNoticeDetailResponse> -> response.result.toModel()}
    }
    override suspend fun editNicknm(nickname : EditNicknmRequest): NetworkResult<EditNicknmModel> {
        return handleApi({mypageService.editNickname(nickname)}) {response: BaseResponse<EditNicknmResponse> -> response.result.toModel()} // mapper를 통해 api 결과를 TestModel로 매핑
    }
}