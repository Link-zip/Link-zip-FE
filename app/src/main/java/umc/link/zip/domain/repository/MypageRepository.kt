package umc.link.zip.domain.repository

import umc.link.zip.data.dto.mypage.request.CheckNicknmRequest
import umc.link.zip.data.dto.request.TestRequest
import umc.link.zip.domain.model.TestModel
import umc.link.zip.domain.model.mypage.CheckNicknmModel
import umc.link.zip.domain.model.mypage.UserInfoModel
import umc.link.zip.domain.model.notice.Notice
import umc.link.zip.domain.model.notice.NoticeList
import umc.link.zip.util.network.NetworkResult

interface MypageRepository {
    suspend fun checkNicknm(request: CheckNicknmRequest): NetworkResult<CheckNicknmModel>
    suspend fun getUserInfo(): NetworkResult<UserInfoModel>
    suspend fun getNotice(): NetworkResult<NoticeList>
    suspend fun getNoticeDetail(noticeId: Int): NetworkResult<Notice>
}