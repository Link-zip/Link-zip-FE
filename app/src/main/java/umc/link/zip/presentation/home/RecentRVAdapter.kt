package umc.link.zip.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.databinding.ItemHomeRecentBinding
import umc.link.zip.domain.model.Link

class RecentRVAdapter(private val recentList: ArrayList<Link>): RecyclerView.Adapter<RecentRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentRVAdapter.ViewHolder {
        val binding: ItemHomeRecentBinding = ItemHomeRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentRVAdapter.ViewHolder, position: Int) {
        holder.bind(recentList[position])
    }

    override fun getItemCount(): Int = recentList.size

    inner class ViewHolder(val binding: ItemHomeRecentBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(link: Link) {
            binding.ivItemHomeLinkImg.setImageResource(link.thumbnail)
            binding.tvItemHomeLinkTitle.text = link.title
        }
    }
}