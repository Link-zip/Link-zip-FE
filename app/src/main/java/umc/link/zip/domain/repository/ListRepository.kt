package umc.link.zip.domain.repository

import umc.link.zip.data.dto.list.request.UnreadRequest
import umc.link.zip.data.dto.request.TestRequest
import umc.link.zip.domain.model.TestModel
import umc.link.zip.domain.model.list.Link
import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.util.network.NetworkResult

interface ListRepository {
    suspend fun getUnreadList(request: UnreadRequest): NetworkResult<UnreadModel>
    suspend fun getLikeList(request: UnreadRequest): NetworkResult<UnreadModel>
    suspend fun getRecentList(request: UnreadRequest): NetworkResult<UnreadModel>
}