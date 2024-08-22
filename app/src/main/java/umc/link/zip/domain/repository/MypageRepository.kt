package umc.link.zip.domain.repository

import umc.link.zip.data.dto.mypage.request.EditNicknmRequest
import umc.link.zip.domain.model.mypage.CheckNicknmModel
import umc.link.zip.domain.model.mypage.EditNicknmModel
import umc.link.zip.domain.model.mypage.UserInfoModel
import umc.link.zip.domain.model.mypage.WithdrawalModel
import umc.link.zip.domain.model.notice.Notice
import umc.link.zip.domain.model.notice.NoticeList
import umc.link.zip.util.network.NetworkResult

interface MypageRepository {
    suspend fun checkNicknm(nickname: String): NetworkResult<CheckNicknmModel>
    suspend fun getUserInfo(): NetworkResult<UserInfoModel>
    suspend fun getNotice(): NetworkResult<NoticeList>
    suspend fun getNoticeDetail(noticeId: Int): NetworkResult<Notice>
    suspend fun editNicknm(request: EditNicknmRequest): NetworkResult<EditNicknmModel>
    suspend fun deleteUser(): NetworkResult<WithdrawalModel>
}