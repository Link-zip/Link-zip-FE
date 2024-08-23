package umc.link.zip.presentation.home.search

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import umc.link.zip.R
import umc.link.zip.databinding.FragmentSearchBinding
import umc.link.zip.domain.model.list.UnreadModel
import umc.link.zip.domain.model.notice.Notice
import umc.link.zip.domain.model.search.SearchLinkResult
import umc.link.zip.domain.model.search.SearchRecent
import umc.link.zip.domain.model.search.SearchResult
import umc.link.zip.domain.model.search.toEntity
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.home.HomeFragmentDirections
import umc.link.zip.presentation.home.search.adapter.SearchRecentRVA
import umc.link.zip.presentation.home.search.adapter.SearchResultRVA
import umc.link.zip.presentation.mypage.adapter.NoticeRVA
import umc.link.zip.util.extension.KeyboardUtil
import umc.link.zip.util.extension.repeatOnStarted
import umc.link.zip.util.network.UiState

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search){
    private val navigator by lazy { findNavController() }
    private val viewModel : SearchViewModel by viewModels()

    private var recentShow : Boolean = true

    override fun initObserver() {
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collectLatest { uiState ->
                    when (uiState) {
                        is UiState.Loading -> {
                            // 로딩 상태 처리
                            Log.d("SearchFragment", "Loading data")
                        }

                        is UiState.Success<*> -> {
                            val data = uiState.data as SearchResult
                            Log.d("SearchFragment", "Fetched data size: ${data.links}")
                            resultRVA.submitList(data.links) {
                                binding.tvSearchResultTitle.visibility = View.VISIBLE
                                binding.tvSearchResultCount.visibility = View.VISIBLE
                                binding.ivSearchNoneClip.visibility = View.INVISIBLE
                                binding.tvSearchNone.visibility = View.INVISIBLE
                                binding.tvSearchNone.text = "검색된 링크가 없어요"
                                binding.rvSearchResult.visibility = View.VISIBLE
                                binding.ivSearchBarDeleteAfterSearch.visibility = View.VISIBLE
                                fnCount(data.links.size)
                                binding.tvSearchRecentTitle.visibility = View.INVISIBLE
                                binding.tvSearchDeleteAll.visibility = View.INVISIBLE
                                binding.rvSearchRecent.visibility = View.INVISIBLE
                                binding.viewSearchBtn.visibility = View.INVISIBLE
                                binding.ivSearchBarDelete.visibility = View.INVISIBLE
                            }
                        }

                        is UiState.Error -> {
                            // 에러 상태 처리
                            Log.e("SearchFragment", "Error fetching data", uiState.error)
                            binding.rvSearchResult.visibility = View.INVISIBLE
                            binding.tvSearchResultTitle.visibility = View.INVISIBLE
                            binding.tvSearchResultCount.visibility = View.INVISIBLE
                            binding.ivSearchNoneClip.visibility= View.VISIBLE
                            binding.tvSearchNone.visibility= View.VISIBLE
                            binding.tvSearchNone.text="검색된 링크가 없어요"
                            binding.ivSearchBarDeleteAfterSearch.visibility= View.VISIBLE
                            binding.tvSearchRecentTitle.visibility = View.INVISIBLE
                            binding.tvSearchDeleteAll.visibility = View.INVISIBLE
                            binding.rvSearchRecent.visibility = View.INVISIBLE
                            binding.viewSearchBtn.visibility = View.INVISIBLE
                            binding.ivSearchBarDelete.visibility = View.INVISIBLE
                        }

                        UiState.Empty -> Log.d("SearchFragment", "isEmpty")
                    }
                }
            }
        }
        repeatOnStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.recentKeywords.collectLatest { recentKeywords ->
                    recentRVA.submitList(recentKeywords) {
                        Log.d("recentCountRVA", recentRVA.itemCount.toString())
                        if(recentShow) {
                            fnCheck()  // 리스트가 어댑터에 반영된 후에 UI 체크
                        }
                    }
                }
            }
        }
    }

    override fun initView() {
        recentShow = true
        binding.ivSearchShadow.setBackgroundResource(R.drawable.shadow_graybtn)
        viewModel.fetchRecentKeywords()
        Log.d("recentCount", recentRVA.itemCount.toString())
        initRecentRVAdapter()
        initResultRVAdapter()
        setShowKeyboard()
        setupClickListener()
    }

    private fun setupClickListener(){
        with (binding){
            viewSearchBtn.setOnClickListener{
                recentShow = false
                if (!etSearchBar.text.isNullOrEmpty()) {
                    search()
                    viewModel.addKeyword(binding.etSearchBar.text.toString())  // 검색어 저장
                } else {
                    Log.d("Search", "검색어가 비어있습니다.")
                }
            }
            ivSearchToolbarBack.setOnClickListener {
                navigator.navigateUp()
            }
            ivSearchBarDelete.setOnClickListener {
                etSearchBar.text.clear()
                ivSearchBarDelete.visibility=View.INVISIBLE
            }
            ivSearchBarDeleteAfterSearch.setOnClickListener {
                recentShow = true
                //이 버튼 클릭시 검색전으로 돌아감
                setSearchBefore(true)
                setSearchAfter(false)
                etSearchBar.text.clear()
                ivSearchBarDelete.visibility = View.INVISIBLE
                resultRVA.submitList(emptyList())
            }
            // 모든 검색어 삭제
            tvSearchDeleteAll.setOnClickListener {
                viewModel.clearAllKeywords()
                tvSearchRecentTitle.visibility = View.INVISIBLE
                tvSearchDeleteAll.visibility = View.INVISIBLE
                rvSearchRecent.visibility = View.INVISIBLE
                ivSearchNoneClip.visibility = View.VISIBLE
                tvSearchNone.text = "최근 검색어가 없어요"
                tvSearchNone.visibility = View.VISIBLE
            }

            etSearchBar.setOnEditorActionListener { _, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                    // 엔터키를 눌렀을 때 버튼 클릭 이벤트 발생
                    viewSearchBtn.performClick()
                    true // 기본 엔터 동작을 막고 우리가 정의한 동작만 실행
                } else {
                    false // 기본 엔터 동작이 실행되도록 허용
                }
            }
        }

    }

    // 검색 후 화면
    private fun search(){
        recentRVA.submitList(viewModel.recentKeywords.value) {
            // 리스트가 업데이트된 후에 API 호출
            setSearchAfter(true)
            setSearchBefore(false)
            viewModel.getSearchLink(keyword = binding.etSearchBar.text.toString())

            // 검색 후 UI 업데이트
            binding.ivSearchBarDeleteAfterSearch.visibility = View.VISIBLE
            Log.d("keyword", binding.etSearchBar.text.toString())

            with(binding) {
                tvSearchRecentTitle.visibility = View.INVISIBLE
                tvSearchDeleteAll.visibility = View.INVISIBLE
                rvSearchRecent.visibility = View.INVISIBLE
            }
        }
    }

    // 검색 창 탭하기 => 최근 검색어 보이기(함수), 다른 거 안 보이기(함수), 마진 없애기
    private fun setShowKeyboard(){
        //탭시 그림자 변경과 그림 마진 사라짐.
        with(binding) {
            KeyboardUtil.registerKeyboardVisibilityListenerPlus(
                clSearch,
                nsvSearch,
                viewSearchBarMg,
                viewSearchMg4,
                ivSearchShadow,
                R.drawable.shadow_bluebtn,
                R.drawable.shadow_graybtn
            )
            etSearchBar.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    viewSearchBtn.visibility=View.VISIBLE
                    ivSearchBarDelete.visibility=View.INVISIBLE
                    ivSearchBarDeleteAfterSearch.visibility = View.INVISIBLE
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    ivSearchBarDelete.visibility=View.VISIBLE
                    ivSearchBarDeleteAfterSearch.visibility = View.INVISIBLE
                }

                override fun afterTextChanged(s: Editable?) {
                    viewSearchBtn.visibility=View.VISIBLE
                    ivSearchBarDelete.visibility=View.VISIBLE
                    ivSearchBarDeleteAfterSearch.visibility = View.INVISIBLE
                }

            })
        }

    }

    private fun reset(){
        with (binding) {
            //검색이후
            ivSearchBarDeleteAfterSearch.visibility= View.INVISIBLE
            tvSearchResultCount.visibility= View.INVISIBLE
            tvSearchResultTitle.visibility= View.INVISIBLE // 얘는 gone 안돼!
            //검색 이후 존재시
            rvSearchResult.visibility= View.INVISIBLE
            //검색 이후 존재 안하면
            ivSearchNoneClip.visibility= View.INVISIBLE
            tvSearchNone.visibility= View.INVISIBLE
            tvSearchNone.text="검색된 링크가 없어요"
            //검색 전
            viewSearchBtn.visibility= View.INVISIBLE
            //키보드 탭하기 전 안 보임 -> 탭하면 보임
            ivSearchBarDelete.visibility= View.INVISIBLE
            //키보드 탭하기 전에 그레이 / 탭하면 블루
            ivSearchShadow.setBackgroundResource(R.drawable.shadow_graybtn)
            //검색 전-최근 검색어 있을 때
            tvSearchRecentTitle.visibility=View.INVISIBLE
            tvSearchDeleteAll.visibility=View.INVISIBLE
            rvSearchRecent.visibility= View.INVISIBLE
            //검색 전-최근 검색어 없을 때
            ivSearchNoneClip.visibility= View.INVISIBLE
            tvSearchNone.visibility= View.INVISIBLE
            tvSearchNone.text="최근 검색어가 없어요"
        }
    }

    private fun setSearchAfter(visibility: Boolean){
        with (binding) {
            //검색이후
            if(visibility){
                viewSearchBtn.visibility = View.INVISIBLE
                ivSearchBarDelete.visibility = View.INVISIBLE
                ivSearchBarDeleteAfterSearch.visibility= View.VISIBLE
            }else{
                ivSearchBarDelete.visibility = View.INVISIBLE
                viewSearchBtn.visibility = View.VISIBLE
                ivSearchBarDeleteAfterSearch.visibility= View.INVISIBLE
                tvSearchResultCount.visibility= View.INVISIBLE
                tvSearchResultTitle.visibility= View.INVISIBLE
                ivSearchNoneClip.visibility= View.INVISIBLE
                tvSearchNone.visibility= View.INVISIBLE
                rvSearchResult.visibility= View.INVISIBLE
            }
        }
    }

    private fun setSearchBefore(visibility: Boolean){
        with (binding) {
            //검색이전
            if(visibility){
                tvSearchRecentTitle.visibility=View.VISIBLE
                tvSearchDeleteAll.visibility=View.VISIBLE
                rvSearchRecent.visibility= View.VISIBLE
            }else{
                tvSearchRecentTitle.visibility=View.INVISIBLE
                tvSearchDeleteAll.visibility=View.INVISIBLE
                rvSearchRecent.visibility= View.INVISIBLE
            }
        }
    }

    private val recentRVA by lazy {
        SearchRecentRVA (
            recent = { id ->
                binding.etSearchBar.setText(id.toString())
            },
            onDeleteClick = { searchRecent ->
                viewModel.deleteKeyword(searchRecent) // ViewModel에서 삭제 메서드 호출
            },
            onClicked = { searchRecent ->
                viewModel.getSearchLink(searchRecent.keyword)
                binding.etSearchBar.setText(searchRecent.keyword)
                viewModel.addKeyword(searchRecent.keyword)
                viewModel.deleteKeyword(searchRecent)
            }
        )
    }

    private fun fnCheck(){
        with(binding) {
            if (recentRVA.itemCount == 0) {
                tvSearchRecentTitle.visibility = View.INVISIBLE
                tvSearchDeleteAll.visibility = View.INVISIBLE
                rvSearchRecent.visibility = View.INVISIBLE
                ivSearchNoneClip.visibility = View.VISIBLE
                tvSearchNone.text = "최근 검색어가 없어요"
                tvSearchNone.visibility = View.VISIBLE
            } else {
                tvSearchRecentTitle.visibility = View.VISIBLE
                tvSearchDeleteAll.visibility = View.VISIBLE
                binding.rvSearchRecent.visibility = View.VISIBLE
                ivSearchNoneClip.visibility = View.INVISIBLE
                tvSearchNone.text = "최근 검색어가 없어요"
                tvSearchNone.visibility = View.INVISIBLE
            }
        }
        Log.d("deleteCheck",recentRVA.itemCount.toString())
    }

    private val resultRVA by lazy {
        SearchResultRVA(
            onItemClicked = { link ->
                if(link.link.tag == "text"){
                    val action = HomeFragmentDirections.actionHomeFragmentToOpenTextFragment(link.link.id)
                    navigator.navigate(action)
                }else{
                    val action = SearchFragmentDirections.actionSearchFragmentToOpenLinkFragment(link.link.id)
                    navigator.navigate(action)
                }
            },
            onLikeClicked = { link ->
                viewModel.updateLikeStatusOnServer(link.link.id)
            }
        )
    }

    private fun fnCount(int: Int){
        binding.tvSearchResultCount.text = int.toString()
    }

    private fun initResultRVAdapter(){
        binding.rvSearchResult.adapter = resultRVA

    }

    private fun initRecentRVAdapter(){
        binding.rvSearchRecent.adapter = recentRVA
    }
}