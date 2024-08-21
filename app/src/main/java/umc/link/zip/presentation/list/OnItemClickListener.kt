package umc.link.zip.presentation.list

import umc.link.zip.domain.model.list.Link

interface OnItemClickListener {
    fun onItemClicked(linkItem: Link)  // 필요한 데이터 타입을 인자로 사용
}