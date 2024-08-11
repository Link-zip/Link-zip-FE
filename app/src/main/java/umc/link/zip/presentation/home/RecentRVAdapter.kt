package umc.link.zip.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import umc.link.zip.databinding.ItemHomeRecentBinding
import umc.link.zip.domain.model.home.Link

class RecentRVAdapter(private val recentList: ArrayList<Link>, private val context: Context)
    : RecyclerView.Adapter<RecentRVAdapter.ViewHolder>() {
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
            Glide.with(context)
                .load(link.thumbnail)
                .into(binding.ivItemHomeLinkImg)
            binding.tvItemHomeLinkTitle.text = link.title
        }
    }
}