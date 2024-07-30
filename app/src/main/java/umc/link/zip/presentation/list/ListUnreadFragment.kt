package umc.link.zip.presentation.list

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import umc.link.zip.presentation.base.BaseFragment

import umc.link.zip.R
import umc.link.zip.databinding.FragmentListRvBinding
import umc.link.zip.domain.model.List.Link
import umc.link.zip.domain.model.List.Zip
import umc.link.zip.presentation.list.adapter.ListUnreadRVA

class ListUnreadFragment : BaseFragment<FragmentListRvBinding>(R.layout.fragment_list_rv) {
    private val navigator by lazy {
        findNavController()
    }
    private val viewModel: ListUnreadViewModel by viewModels({requireParentFragment()})
    private val listUnreadRVA by lazy {
        ListUnreadRVA{
            /* 링크 페이지 연결
            linkId ->
            val action =
                ListUnreadFragmentDirections.actionListUnreadFragmentToLinkFragment(linkId)
            navigator.navigate(action)
             */
        }
    }

    override fun initObserver() {
    }
    override fun initView() {
        initPostListRVAdapter()
    }
    private fun initPostListRVAdapter(){
        binding.rvList.adapter = listUnreadRVA
        val zip = Zip("1", "Zip Title", "blue")
        val list = listOf(
            Link("1", "테스트입니다1", "url", "텍스트", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff",1, "2024.7.28", zip),
            Link("2", "테스트입니다2", "url", "텍스트", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff",0, "2024.7.29", zip),
            Link("3", "테스트입니다3", "url", "텍스트", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff",1, "2024.7.30", zip),
            Link("4", "테스트입니다4", "url", "텍스트", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff",0, "2024.7.31", zip),
            Link("5", "테스트입니다5", "url", "텍스트", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff",1, "2024.7.28", zip),
            Link("6", "테스트입니다6", "url", "텍스트", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff",1, "2024.7.29", zip),
            Link("7", "테스트입니다7", "url", "텍스트", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff",1, "2024.7.30", zip),
            Link("8", "테스트입니다8", "url", "텍스트", "https://i.scdn.co/image/ab67616d0000b2734ed058b71650a6ca2c04adff",1, "2024.7.31", zip)
        )
        listUnreadRVA.submitList(list)
    }
}