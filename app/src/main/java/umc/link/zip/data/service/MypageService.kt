package umc.link.zip.data.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.list.request.UnreadRequest
import umc.link.zip.data.dto.list.response.UnreadResponseList
import umc.link.zip.data.dto.mypage.request.CheckNicknmRequest
import umc.link.zip.data.dto.mypage.response.CheckNicknmResponse
import umc.link.zip.data.dto.mypage.response.UserInfoResponse
import umc.link.zip.domain.model.mypage.CheckNicknmModel

interface MypageService {
    @GET("/user")
    suspend fun checkNickname(@Query("nickname") request: CheckNicknmRequest): Response<BaseResponse<CheckNicknmResponse>>

}