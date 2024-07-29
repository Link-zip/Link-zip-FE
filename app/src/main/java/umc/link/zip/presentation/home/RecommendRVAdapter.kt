package umc.link.zip.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.databinding.ItemHomeRecentBinding
import umc.link.zip.databinding.ItemHomeRecommendBinding
import umc.link.zip.domain.model.Link

class RecommendRVAdapter(private val recommendList: ArrayList<Link>): RecyclerView.Adapter<RecommendRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendRVAdapter.ViewHolder {
        val binding: ItemHomeRecommendBinding = ItemHomeRecommendBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendRVAdapter.ViewHolder, position: Int) {
        holder.bind(recommendList[position])
    }

    override fun getItemCount(): Int = recommendList.size

    inner class ViewHolder(val binding: ItemHomeRecommendBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(link: Link) {
            binding.ivItemHomeRecommend.setImageResource(link.thumbnail)
            binding.tvItemHomeRecommendTitle.text = link.title
        }
    }
}