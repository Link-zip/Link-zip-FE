package umc.link.zip.presentation.home

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import umc.link.zip.R
import umc.link.zip.data.dto.TokenRefreshManager
import umc.link.zip.databinding.FragmentHomeBinding
import umc.link.zip.domain.model.home.Link
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.network.NetworkResult
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.P)
@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
    private val navigator by lazy { findNavController() }
    private lateinit var sharedViewModel: SharedViewModel
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var recentRVAdapter: RecentRVAdapter

    //토큰 리프레시
    @Inject
    lateinit var tokenRefreshManager: TokenRefreshManager

    private fun refreshToken() {
        lifecycleScope.launch {
            val newToken = tokenRefreshManager.refreshToken()
            if (newToken != null) {
                Log.d("MyFragment", "New Token: $newToken")
            } else {
                Log.d("MyFragment", "Failed to refresh token")
            }
        }
    }

    override fun initObserver() {
        setHomeOldCountViewModel()
        setHomeTotalCountViewModel()
        setHomeAlertCountViewModel()
        setHomeUnreadCountViewModel()
        setHomeRecentViewModel()
        setHomeAlertExistsViewModel()
    }

    override fun initView() {
        refreshToken()
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        homeViewModel.getRecentLinks()
        homeViewModel.getOldCount()
        homeViewModel.getTotalCount()
        homeViewModel.getAlertExists()
        setClickListener()
        setScrollListener()
        setRVAdapter()
    }

    private fun setHomeAlertExistsViewModel() {
        homeViewModel.alertExists.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> { Log.d("home", "alert exists : ${result.exception}") }
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    if(result.data.uncomfirmedAlert) {
                        binding.ivHomeAlarmExist.visibility = View.VISIBLE
                        binding.ivHomeAlarmNothing.visibility = View.INVISIBLE
                    } else {
                        binding.ivHomeAlarmExist.visibility = View.INVISIBLE
                        binding.ivHomeAlarmNothing.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setHomeOldCountViewModel() {
        homeViewModel.oldCount.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> { Log.d("home", "old link : ${result.exception}") }
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    if(result.data.old_links_count == 0) {
                        binding.clHomeOldlink.visibility = View.GONE
                    } else {
                        val countText = "${result.data.old_links_count}개"
                        val fullText = "알림을 설정한 링크 중 최근\n한 달을 넘게 안 읽은 링크\n${countText}가 있어요."

                        val spannableString = SpannableString(fullText)

                        val startIdx = fullText.indexOf(countText)
                        val endIdx = startIdx + countText.length

                        val myTypeface = Typeface.create(
                            ResourcesCompat.getFont(requireContext(), R.font.pretendard_medium),
                            Typeface.BOLD
                        )
                        spannableString.setSpan(
                            TypefaceSpan(myTypeface),
                            startIdx,
                            endIdx,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                        )

                        binding.tvHomeOldlinkNotreadcnt.text = spannableString
                    }
                }
            }
        }
    }

    private fun setHomeTotalCountViewModel() {
        homeViewModel.totalCount.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> { Log.d("home", "total link : ${result.exception}") }
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    binding.tvHomeLinkAlarmcnt.text = "0개"
                    binding.tvHomeLinkNotreadcnt.text = "0개"
                    if(result.data.total_links_count == 0) {
                        binding.tvHomeLink1month.visibility = View.VISIBLE
                        binding.tvHomeLink1month.text = "가입을 환영해요!"
                        binding.tvHomeLinkWait.text = "첫 번째 링크를\n저장해 보세요!"
                    } else {
                        homeViewModel.getAlertCount()
                    }
                }
            }
        }
    }

    private fun setHomeAlertCountViewModel() {
        homeViewModel.alertCount.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> { Log.d("home", "alert link : ${result.exception}") }
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    if(result.data.recent_alerts_count == 0) {
                        binding.tvHomeLink1month.visibility = View.INVISIBLE
                        binding.tvHomeLinkWait.text = "링크를\n저장해보세요!"
                    } else {
                        binding.tvHomeLink1month.visibility = View.VISIBLE
                        binding.tvHomeLink1month.text = "최근 한 달 기준"
                        binding.tvHomeLinkAlarmcnt.text = "${result.data.recent_alerts_count}개"
                        homeViewModel.getUnreadCount()
                    }
                }
            }
        }
    }

    private fun setHomeUnreadCountViewModel() {
        homeViewModel.unreadCount.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> { Log.d("home", "unread link : ${result.exception}") }
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    binding.tvHomeLinkNotreadcnt.text = "${result.data.unread_links_count}개"
                    if(result.data.unread_links_count == 0) {
                        binding.tvHomeLinkWait.text = "알림 설정한 모든\n링크를 읽었어요!"
                    } else {
                        binding.tvHomeLinkWait.text = "${result.data.unread_links_count}개의 링크가\n기다리고 있어요!"
                    }
                }
            }
        }
    }

    private fun setHomeRecentViewModel() {
        homeViewModel.recentLinks.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> { Log.d("home", result.exception.toString()) }
                is NetworkResult.Fail -> { Log.d("home", "Network result fail") }
                is NetworkResult.Success -> {
                    if(result.data.links.isEmpty()) {
                        binding.clHomeRecent.visibility = View.GONE
                    } else {
                        recentRVAdapter.submitList(result.data.links)
                        binding.clHomeRecent.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun setScrollListener() {
        binding.svHome.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (scrollY > 10) {
                binding.layoutHomeToolbar.setBackgroundColor(Color.WHITE)
            } else {
                binding.layoutHomeToolbar.setBackgroundColor(Color.TRANSPARENT)
            }
        })
    }

    private fun setRVAdapter() {
        recentRVAdapter = RecentRVAdapter(requireContext(), object : RecentRVAdapter.OnItemClickListener {
            override fun onItemClick(link: Link) {
                if(link.tag == "text"){
                    val action = HomeFragmentDirections.actionHomeFragmentToOpenTextFragment(link.id)
                    navigator.navigate(action)
                }else{
                    val action = HomeFragmentDirections.actionHomeFragmentToOpenLinkFragment(link.id)
                    navigator.navigate(action)
                }
            }
        })
        binding.rvHomeRecent.adapter = recentRVAdapter
        binding.rvHomeRecent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun toListFragment() {
        homeViewModel.navigateToListFragment()
    }

    private fun setClickListener() {
        binding.clHomeRecommend1.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://kr.pinterest.com/")
            startActivity(intent)
        }

        binding.clHomeRecommend2.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.tistory.com/")
            startActivity(intent)
        }

        binding.clHomeRecommend3.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://newneek.co/")
            startActivity(intent)
        }

        binding.llHomeMore.setOnClickListener {
            sharedViewModel.selectItem("recent")
            toListFragment()
        }

        binding.ivHomeSearch.setOnClickListener {
            navigator.navigate(R.id.action_homeFragment_to_searchFragment)
        }

        binding.ivHomeAlarmExist.setOnClickListener {
            navigator.navigate(R.id.action_homeFragment_to_alertFragment)
        }

        binding.ivHomeAlarmNothing.setOnClickListener {
            navigator.navigate(R.id.action_homeFragment_to_alertFragment)
        }

        binding.clHomeLink.setOnClickListener {
            sharedViewModel.selectItem("wait")
            toListFragment()
        }

        binding.clHomeOldlink.setOnClickListener {
            sharedViewModel.selectItem("old")
            toListFragment()
        }

        binding.clHomeLikelink.setOnClickListener {
            sharedViewModel.selectItem("like")
            toListFragment()
        }
    }
}