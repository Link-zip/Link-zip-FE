package umc.link.zip.presentation.home

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TypefaceSpan
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentHomeBinding
import umc.link.zip.domain.model.home.Link
import umc.link.zip.domain.model.home.Zip
import umc.link.zip.presentation.base.BaseFragment
import umc.link.zip.util.network.NetworkResult

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
    private var recentList = ArrayList<Link>()
    private val navigator by lazy { findNavController() }
    private lateinit var sharedViewModel: SharedViewModel
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var recentRVAdapter: RecentRVAdapter

    override fun initObserver() {
        setHomeRecentViewModel()
        setHomeOldCountViewModel()
        setHomeTotalCountViewModel()
    }

    override fun initView() {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        homeViewModel.getRecentLinks()
        setClickListener()
        setScrollListener()
        setRVAdapter()
    }

    private fun setHomeOldCountViewModel() {
        homeViewModel.oldCount.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> {}
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    if(result.data.oldLinksCount == 0) {
                        binding.clHomeOldlink.visibility = View.GONE
                    } else {
                        val countText = "${result.data.oldLinksCount}개"
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
                is NetworkResult.Error -> {}
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    binding.tvHomeLinkAlarmcnt.text = "0개"
                    binding.tvHomeLinkNotreadcnt.text = "0개"
                    if(result.data.totalLinksCount == 0) {
                        binding.tvHomeLink1month.visibility = View.VISIBLE
                        binding.tvHomeLink1month.text = "가입을 환영해요!"
                        binding.tvHomeLinkWait.text = "첫 번째 링크를\n저장해 보세요!"
                    } else {
                        setHomeAlertCountViewModel()
                    }
                }
            }
        }
    }

    private fun setHomeAlertCountViewModel() {
        homeViewModel.alertCount.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> {}
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    if(result.data.recentAlertsCount == 0) {
                        binding.tvHomeLink1month.visibility = View.INVISIBLE
                        binding.tvHomeLinkWait.text = "링크를\n저장해보세요!"
                    } else {
                        binding.tvHomeLink1month.visibility = View.VISIBLE
                        binding.tvHomeLink1month.text = "최근 한 달 기준"
                        binding.tvHomeLinkAlarmcnt.text = "${result.data.recentAlertsCount}개"
                        setHomeUnreadCountViewModel()
                    }
                }
            }
        }
    }

    private fun setHomeUnreadCountViewModel() {
        homeViewModel.unreadCount.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> {}
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    binding.tvHomeLinkNotreadcnt.text = "${result.data.unreadLinksCount}개"
                    if(result.data.unreadLinksCount == 0) {
                        binding.tvHomeLinkWait.text = "알림 설정한 모든\n링크를 읽었어요!"
                    } else {
                        binding.tvHomeLinkWait.text = "${result.data.unreadLinksCount}개의 링크가\n기다리고 있어요!"
                    }
                }
            }
        }
    }

    private fun setHomeRecentViewModel() {
        homeViewModel.recentLinks.observe(this) { result ->
            when(result) {
                is NetworkResult.Error -> {}
                is NetworkResult.Fail -> {}
                is NetworkResult.Success -> {
                    if(result.data.links.isEmpty()) {
                        binding.clHomeRecent.visibility = View.GONE
                    } else {
                        recentList.clear()
                        recentList.addAll(result.data.links)
                        recentRVAdapter.notifyDataSetChanged()
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
        recentRVAdapter = RecentRVAdapter(recentList, requireContext())
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

        }

        binding.ivHomeAlarmExist.setOnClickListener {
            navigator.navigate(R.id.action_homeFragment_to_alarmFragment)
        }

        binding.ivHomeAlarmNothing.setOnClickListener {
            navigator.navigate(R.id.action_homeFragment_to_alarmFragment)
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

    private fun setRecentList() {
        recentList.add(
            Link("0",
                "펫 프렌들리 마케팅",
                "www.어딘가.com",
                "무언가 끄적임",
                "https://s3.ap-northeast-2.amazonaws.com/univ-careet/FileData/Picture/202208/fa66c6ae-392e-4112-95ee-5b543c6e7181_770x426.png",
                0,
                "2024-07-29",
                Zip("0", "title1", "black")
            )
        )
        recentList.add(
            Link("1",
                "마이크로/나노 인플루언서 마케팅 전략",
                "www.어딘가.com",
                "무언가 끄적임",
                "https://openads-real.s3.amazonaws.com/openadsAdmin/images/contsThumb/%EC%8B%A4%ED%8C%A8%EB%9E%80%20%EC%97%86%EB%8A%94%20%20%EB%A7%88%EC%9D%B4%ED%81%AC%EB%A1%9C%20%EB%82%98%EB%85%B8%20%EC%9D%B8%ED%94%8C%EB%A3%A8%EC%96%B8%EC%84%9C%20%EB%A7%88%EC%BC%80%ED%8C%85%20%20%ED%95%84%EC%8A%B9%20%EC%A0%84%EB%9E%B5.png",
                0,
                "2024-07-29",
                Zip("0", "title1", "black")
            )
        )
        recentList.add(
            Link("2",
                "유튜브",
                "www.youtube.com",
                "무언가 끄적임",
                "https://www.youtube.com/img/desktop/yt_1200.png",
                0,
                "2024-07-29",
                Zip("0", "title1", "black")
            )
        )
        recentList.add(
            Link("3",
                "네이버",
                "www.naver.com",
                "무언가 끄적임",
                "https://s.pstatic.net/static/www/mobile/edit/2016/0705/mobile_212852414260.png",
                0,
                "2024-07-29",
                Zip("0", "title1", "black")
            )
        )
    }
}