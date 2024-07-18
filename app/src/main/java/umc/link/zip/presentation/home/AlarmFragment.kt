package umc.link.zip.presentation.home

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.FragmentAlarmBinding
import umc.link.zip.presentation.base.BaseFragment

@AndroidEntryPoint
class AlarmFragment : BaseFragment<FragmentAlarmBinding>(R.layout.fragment_alarm){

    override fun initObserver() {
        // Observers can be added here if needed
    }

    override fun initView() {
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.profilePostRv
        recyclerView.layoutManager = LinearLayoutManager(context)

        // DividerItemDecoration 추가
        val dividerHeight = 2 // 구분선 높이 (픽셀 단위)
        val dividerColor = R.color.light_gray // 구분선 색상 (res/values/colors.xml에 정의된 색상 사용)

        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), dividerHeight, dividerColor))
    }

    // DividerItemDecoration 클래스
    private inner class DividerItemDecoration(context: Context, private val height: Int, colorResId: Int) : RecyclerView.ItemDecoration() {
        private val paint: Paint = Paint()

        init {
            paint.color = ContextCompat.getColor(context, colorResId)
            paint.strokeWidth = height.toFloat()
        }

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            outRect.bottom = height
        }

        override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
            val left = parent.paddingLeft
            val right = parent.width - parent.paddingRight

            val childCount = parent.childCount
            for (i in 0 until childCount - 1) {
                val child = parent.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val top = child.bottom + params.bottomMargin
                val bottom = top + height
                c.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), paint)
            }
        }
    }
}
