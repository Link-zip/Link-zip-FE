package umc.link.zip.presentation.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import umc.link.zip.databinding.ItemHomeRecentBinding
import umc.link.zip.domain.model.home.Link

class RecentRVAdapter(
    private val context: Context,
    private val clickListener: OnItemClickListener
) : ListAdapter<Link, RecentRVAdapter.ViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentRVAdapter.ViewHolder {
        val binding: ItemHomeRecentBinding = ItemHomeRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentRVAdapter.ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    interface OnItemClickListener {
        fun onItemClick(link : Link)
    }

    inner class ViewHolder(val binding: ItemHomeRecentBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(link: Link) {
            Glide.with(context)
                .load(link.thumbnail)
                .into(binding.ivItemHomeLinkImg)
            binding.tvItemHomeLinkTitle.text = link.title
            binding.root.setOnClickListener {
                clickListener.onItemClick(link)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Link>() {
        override fun areItemsTheSame(oldItem: Link, newItem: Link): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Link, newItem: Link): Boolean {
            return oldItem == newItem
        }
    }

}