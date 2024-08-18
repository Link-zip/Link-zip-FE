package umc.link.zip.presentation.home.search

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import umc.link.zip.R
import umc.link.zip.databinding.FragmentSearchBinding
import umc.link.zip.domain.model.notice.Notice
import umc.link.zip.domain.model.search.SearchRecent
import umc.link.zip.domain.model.search.SearchResult
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.presentation.home.search.adapter.SearchRecentRVA
import umc.link.zip.presentation.home.search.adapter.SearchResultRVA
import umc.link.zip.presentation.mypage.adapter.NoticeRVA
import umc.link.zip.util.extension.KeyboardUtil

class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search){
    private val navigator by lazy { findNavController() }
    private val viewModel : SearchViewModel by viewModels()
    override fun initObserver() {

    }

    override fun initView() {
        binding.ivSearchShadow.setBackgroundResource(R.drawable.shadow_graybtn)
        initRecentRVAdapter()
        initResultRVAdapter()
        initSearchRecent()
        setShowKeyboard()
        setupClickListener()
    }

    private fun setupClickListener(){
        with (binding){
            viewSearchBtn.setOnClickListener{
                search()
            }
            ivSearchToolbarBack.setOnClickListener {
                navigator.navigateUp()
            }
            ivSearchBarDelete.setOnClickListener {
                etSearchBar.text.clear()
                ivSearchBarDelete.visibility=View.INVISIBLE
            }
            ivSearchBarDeleteAfterSearch.setOnClickListener {
                //이 버튼 클릭시 검색전으로 돌아감
                setSearchBefore(true)
                setSearchAfter(false)
                etSearchBar.text.clear()
                ivSearchBarDelete.visibility = View.INVISIBLE
            }
        }

    }
    // 최근 검색어 보여주기  => 없으면 이미지 보여주기(함수)
    private fun initSearchRecent(){
        setSearchAfter(false)
        setSearchBefore(true)
    }

    // 검색 후 화면
    private fun search(){
        setSearchAfter(true)
        setSearchBefore(false)
        //api 연결. +  initObserver
    }

    // 검색 창 탭하기 => 최근 검색어 보이기(함수), 다른 거 안 보이기(함수), 마진 없애기
    private fun setShowKeyboard(){
        //탭시 그림자 변경과 그림 마진 사라짐.
        with(binding) {
            KeyboardUtil.registerKeyboardVisibilityListenerWithoutScrollView(
                clSearch,
                nsvSearch,
                viewSearchBarMg,
                viewSearchMg4,
                ivSearchShadow,
                R.drawable.shadow_bluebtn,
                R.drawable.shadow_graybtn,
                ivSearchBarDeleteAfterSearch
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
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    ivSearchBarDelete.visibility=View.VISIBLE
                }

                override fun afterTextChanged(s: Editable?) {
                    viewSearchBtn.visibility=View.VISIBLE
                    ivSearchBarDelete.visibility=View.VISIBLE
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
                tvSearchResultCount.visibility= View.VISIBLE
                tvSearchResultTitle.visibility= View.VISIBLE
                rvSearchResult.visibility= View.VISIBLE //존재
                if(resultRVA.itemCount<0){
                    ivSearchNoneClip.visibility= View.VISIBLE
                    tvSearchNone.visibility= View.VISIBLE
                    tvSearchNone.text="검색된 링크가 없어요"
                }
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
                //검색 전-최근 검색어 없을 때
                if(recentRVA.itemCount<0) {
                    ivSearchNoneClip.visibility = View.VISIBLE
                    tvSearchNone.visibility = View.VISIBLE
                    tvSearchNone.text = "최근 검색어가 없어요"
                }
            }else{
                tvSearchRecentTitle.visibility=View.INVISIBLE
                tvSearchDeleteAll.visibility=View.INVISIBLE
                rvSearchRecent.visibility= View.INVISIBLE
                //검색 전-최근 검색어 없을 때
                ivSearchNoneClip.visibility= View.INVISIBLE
                tvSearchNone.visibility= View.INVISIBLE
            }
        }
    }

    private val recentRVA by lazy {
        SearchRecentRVA{}
    }

    private val resultRVA by lazy {
        SearchResultRVA{}
    }

    private fun initResultRVAdapter(){
        binding.rvSearchResult.adapter = resultRVA

    }

    private fun initRecentRVAdapter(){
        binding.rvSearchRecent.adapter = recentRVA
        val list = listOf(
            SearchRecent(1, "인사이트"),
            SearchRecent(2, "link"),
        )
        recentRVA.submitList(list)
    }
}