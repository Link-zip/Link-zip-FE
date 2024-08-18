package umc.link.zip.domain.repository

import umc.link.zip.data.dto.link.request.LinkAddRequest
import umc.link.zip.data.dto.link.request.LinkExtractRequest
import umc.link.zip.data.dto.link.request.LinkModifyRequest
import umc.link.zip.data.dto.link.request.LinkSummaryRequest
import umc.link.zip.data.dto.link.response.MoveLinkToNewZipResponse
import umc.link.zip.data.dto.zip.request.ZipCreateRequest
import umc.link.zip.data.dto.zip.request.ZipEditRequest
import umc.link.zip.data.dto.zip.request.ZipRmRequest
import umc.link.zip.domain.model.link.LinkAddModel
import umc.link.zip.domain.model.link.LinkDeleteModel
import umc.link.zip.domain.model.link.LinkExtractModel
import umc.link.zip.domain.model.link.LinkGetByLinkIDModel
import umc.link.zip.domain.model.link.LinkGetModel
import umc.link.zip.domain.model.link.LinkModifyModel
import umc.link.zip.domain.model.link.LinkSummaryModel
import umc.link.zip.domain.model.link.LinkUpdateLikeModel
import umc.link.zip.domain.model.link.LinkVisitModel
import umc.link.zip.domain.model.link.MoveLinkToNewZipModel
import umc.link.zip.domain.model.zip.ZipCreateModel
import umc.link.zip.domain.model.zip.ZipEditModel
import umc.link.zip.domain.model.zip.ZipRmModel
import umc.link.zip.util.network.NetworkResult

interface LinkRepository {
    suspend fun getLinkData(zip_id: Int, tag: String): NetworkResult<LinkGetModel>
    suspend fun GetLinkByLinkID(link_id : Int): NetworkResult<LinkGetByLinkIDModel>
    suspend fun MoveLinkToNewZip(link_id : Int, new_zip_id : Int): NetworkResult<MoveLinkToNewZipModel>
    suspend fun ExtractLink(linkExtractRequest: LinkExtractRequest): NetworkResult<LinkExtractModel>
    suspend fun SummaryLink(linkSummaryRequest: LinkSummaryRequest): NetworkResult<LinkSummaryModel>
    suspend fun AddLink(linkAddRequest: LinkAddRequest): NetworkResult<LinkAddModel>
    suspend fun VisitLink(link_id : Int): NetworkResult<LinkVisitModel>
    suspend fun UpdateLikeLink(link_id : Int): NetworkResult<LinkUpdateLikeModel>
    suspend fun ModifyLink(link_id : Int, linkModifyRequest: LinkModifyRequest): NetworkResult<LinkModifyModel>
    suspend fun DeleteLink(link_id : Int): NetworkResult<LinkDeleteModel>

}