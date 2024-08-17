package umc.link.zip.data.service

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import umc.link.zip.data.dto.BaseResponse
import umc.link.zip.data.dto.alert.request.AlertAddRequest
import umc.link.zip.data.dto.alert.response.AlertAddResponse
import umc.link.zip.data.dto.alert.response.AlertConfirmResponse
import umc.link.zip.data.dto.alert.response.AlertGetResponse
import umc.link.zip.data.dto.link.request.LinkExtractRequest
import umc.link.zip.data.dto.link.response.LinkGetByLinkIDResponse

interface AlertService {
    @PUT("alert/confirm/{alertId}")
    suspend fun putConfirmAlert(
        @Path("alertId") alertId : Int
    ) : Response<BaseResponse<AlertConfirmResponse>>

    @POST("alert/add")
    suspend fun postAddAlert(
        @Body alertAddRequest: AlertAddRequest
    ) : Response<BaseResponse<AlertAddResponse>>

    @GET("alert")
    suspend fun getAlert(
    ) : Response<BaseResponse<AlertGetResponse>>
}