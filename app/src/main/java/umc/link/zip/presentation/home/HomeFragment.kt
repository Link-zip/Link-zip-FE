package umc.link.zip.presentation.home

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentHomeBinding
import umc.link.zip.domain.model.home.Link
import umc.link.zip.domain.model.home.Zip
import umc.link.zip.presentation.base.BaseFragment


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
    private var recentList = ArrayList<Link>()
    private val navigator by lazy { findNavController() }
    private lateinit var sharedViewModel: SharedViewModel
    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var recentRVAdapter: RecentRVAdapter
    private val viewModel: HomeActivityViewModel by activityViewModels()

    override fun initObserver() {
        homeViewModel.recentLinks.observe(viewLifecycleOwner, Observer { links->
            if(links.isEmpty()) {
                binding.clHomeRecent.visibility = View.GONE
            } else {
                recentList.clear()
                recentList.addAll(links)
                recentRVAdapter.notifyDataSetChanged()
                binding.clHomeRecent.visibility = View.VISIBLE
            }
        })

        homeViewModel.error.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        })
    }

    override fun initView() {
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        homeViewModel.fetchRecentLinks()
        setClickListener()
        setScrollListener()
        setRecentList()
        setRVAdapter()
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
        viewModel.navigateToListFragment()
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