package umc.link.zip.presentation.zip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.databinding.ItemZipBinding
import umc.link.zip.domain.model.ZipItem

// ZipAdapter.kt

class ZipAdapter : ListAdapter<ZipItem, ZipAdapter.ZipViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZipViewHolder {
        val binding = ItemZipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ZipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ZipViewHolder, position: Int) {
        val zipItem = getItem(position)
        holder.bind(zipItem)
    }

    class ZipViewHolder(private val binding: ItemZipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(zipItem: ZipItem) {
            binding.zipItem = zipItem
            binding.executePendingBindings()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ZipItem>() {
        override fun areItemsTheSame(oldItem: ZipItem, newItem: ZipItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ZipItem, newItem: ZipItem): Boolean {
            return oldItem == newItem
        }
    }
}
