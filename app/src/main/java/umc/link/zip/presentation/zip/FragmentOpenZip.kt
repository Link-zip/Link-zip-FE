package umc.link.zip.presentation.zip

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentOpenzipBinding
import umc.link.zip.presentation.base.BaseFragment
import kotlin.math.max

@AndroidEntryPoint
class FragmentOpenZip : BaseFragment<FragmentOpenzipBinding>(R.layout.fragment_openzip) {
    override fun initObserver() {}
    override fun initView() {
        setupScrollListener()
    }

    private fun setupScrollListener() {
        binding.fragmentOpenzipItemLinkRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                adjustHeaderVisibility(dy)
            }
        })
    }

    fun adjustHeaderVisibility(dy: Int) {
        val layoutParams = binding.fragmentOpenzipIconCl.layoutParams as ConstraintLayout.LayoutParams
        val newTopMargin = layoutParams.topMargin - dy
        layoutParams.topMargin = max(0, newTopMargin) // 마진이 0 아래로 내려가지 않도록 보장
        binding.fragmentOpenzipIconCl.layoutParams = layoutParams

        if (newTopMargin <= 0) {
            binding.fragmentOpenzipIconCl.visibility = View.GONE
        } else {
            binding.fragmentOpenzipIconCl.visibility = View.VISIBLE
        }
    }
}
