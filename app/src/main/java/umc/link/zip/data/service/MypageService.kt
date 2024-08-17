package umc.link.zip.data.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.mypage.request.CheckNicknmRequest
import umc.link.zip.data.dto.mypage.response.CheckNicknmResponse
import umc.link.zip.data.dto.mypage.response.GetNoticeResponse
import umc.link.zip.data.dto.mypage.response.UserInfoResponse

interface MypageService {
    @GET("/user")
    suspend fun checkNickname(@Query("nickname") request: CheckNicknmRequest): Response<BaseResponse<CheckNicknmResponse>>

    @GET("/user/info")
    suspend fun getUserInfo() : Response<BaseResponse<UserInfoResponse>>

    @GET("/notice")
    suspend fun getNotice() : Response<BaseResponse<GetNoticeResponse>>


}