package umc.link.zip.domain.repository

import umc.link.zip.domain.model.home.HomeAlertCountModel
import umc.link.zip.domain.model.home.HomeRecentModel
import umc.link.zip.domain.model.home.HomeUnreadCountModel
import umc.link.zip.util.network.NetworkResult

interface HomeRepository {
    suspend fun getRecentLinks(): NetworkResult<HomeRecentModel>

    suspend fun getAlertCount(): NetworkResult<HomeAlertCountModel>

    suspend fun getUnreadCount(): NetworkResult<HomeUnreadCountModel>
}