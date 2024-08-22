package umc.link.zip.data.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.mypage.request.EditNicknmRequest
import umc.link.zip.data.dto.mypage.response.CheckNicknmResponse
import umc.link.zip.data.dto.mypage.response.EditNicknmResponse
import umc.link.zip.data.dto.mypage.response.GetNoticeDetailResponse
import umc.link.zip.data.dto.mypage.response.GetNoticeResponse
import umc.link.zip.data.dto.mypage.response.UserInfoResponse
import umc.link.zip.data.dto.mypage.response.WithdrawalResponse

interface MypageService {
    @GET("/user")
    suspend fun checkNickname(@Query("nickname") nickname: String): Response<BaseResponse<CheckNicknmResponse>>

    @GET("/user/info")
    suspend fun getUserInfo() : Response<BaseResponse<UserInfoResponse>>

    @GET("/notice")
    suspend fun getNotice() : Response<BaseResponse<GetNoticeResponse>>

    @GET("/notice/{notice_id}")
    suspend fun getNoticeDetail(@Path("notice_id") noticeId: Int) : Response<BaseResponse<GetNoticeDetailResponse>>

    @PATCH("/user")
    suspend fun editNickname(@Body request: EditNicknmRequest) : Response<BaseResponse<EditNicknmResponse>>

    @DELETE("/user/delete")
    suspend fun deleteUser() : Response<BaseResponse<WithdrawalResponse>>
}