package umc.link.zip.presentation.zip.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.databinding.ItemZipBinding
import umc.link.zip.domain.model.ZipItem

class ZipAdapter(private val onItemSelected: (ZipItem, Boolean) -> Unit) : RecyclerView.Adapter<ZipAdapter.ZipViewHolder>() {

    private var zipItems: List<ZipItem> = listOf()
    private var selectedItems: MutableSet<ZipItem> = mutableSetOf()
    private var itemBackgroundColor = Color.parseColor("#FBFBFB")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZipViewHolder {
        val binding = ItemZipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ZipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ZipViewHolder, position: Int) {
        val zipItem = zipItems[position]
        holder.bind(zipItem, selectedItems.contains(zipItem))
    }

    override fun getItemCount(): Int = zipItems.size

    fun submitList(items: List<ZipItem>) {
        zipItems = items
        notifyDataSetChanged()
    }

    fun clearSelections() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    fun selectAllItems() {
        selectedItems.clear()
        selectedItems.addAll(zipItems)
        notifyDataSetChanged()
    }

    fun updateBackgroundColorOfItems(color: Int) {
        itemBackgroundColor = color
        notifyDataSetChanged()
    }

    inner class ZipViewHolder(private val binding: ItemZipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(zipItem: ZipItem, isSelected: Boolean) {
            binding.zipItem = zipItem
            binding.root.setOnClickListener {
                if (selectedItems.contains(zipItem)) {
                    selectedItems.remove(zipItem)
                } else {
                    selectedItems.add(zipItem)
                }
                onItemSelected(zipItem, selectedItems.isNotEmpty())
                notifyItemChanged(adapterPosition)
            }
            binding.root.setBackgroundColor(if (isSelected) Color.parseColor("#F4F5F6") else itemBackgroundColor)
        }
    }
}