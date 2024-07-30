package umc.link.zip.presentation.home

import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentHomeBinding
import umc.link.zip.domain.model.Link
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home){
    private var recentList = ArrayList<Link>()
    override fun initObserver() {

    }

    override fun initView() {
        setRecentList()
        val recentRVAdapter = RecentRVAdapter(recentList)
        binding.rvHomeRecent.adapter = recentRVAdapter
        binding.rvHomeRecent.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setRecentList() {
        recentList.add(
            Link(0,
                "펫 프렌들리 마케팅",
                "www.어딘가.com",
                "무언가 끄적임",
                R.drawable.img_home_recent,
                0,
                "2024-07-29",
            )
        )
        recentList.add(
            Link(1,
                "마이크로/나노 인플루언서 마케팅 전략",
                "www.어딘가.com",
                "무언가 끄적임",
                R.drawable.img_home_recent2,
                0,
                "2024-07-29",
            )
        )
        recentList.add(
            Link(2,
                "유튜브",
                "www.youtube.com",
                "무언가 끄적임",
                R.drawable.img_home_recent3,
                0,
                "2024-07-29",
            )
        )
        recentList.add(
            Link(3,
                "네이버",
                "www.naver.com",
                "무언가 끄적임",
                R.drawable.img_home_recent4,
                0,
                "2024-07-29",
            )
        )
    }
}