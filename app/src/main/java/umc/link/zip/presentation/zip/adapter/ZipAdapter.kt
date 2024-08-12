package umc.link.zip.presentation.zip.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.R
import umc.link.zip.databinding.ItemZipBinding
import umc.link.zip.domain.model.ZipItem

class ZipAdapter(private val onItemSelected: (ZipItem, Boolean) -> Unit) : RecyclerView.Adapter<ZipAdapter.ZipViewHolder>() {

    private var items: List<ZipItem> = emptyList()
    private var selectedItems: MutableSet<ZipItem> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZipViewHolder {
        val binding = ItemZipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ZipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ZipViewHolder, position: Int) {
        val zipItem = items[position]
        val isSelected = selectedItems.contains(zipItem)
        holder.bind(zipItem, isSelected)
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<ZipItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun selectAllItems() {
        selectedItems.addAll(items)
        notifyDataSetChanged()
    }

    fun clearSelections() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    fun updateBackgroundColorOfItems(color: Int) {
        for (i in items.indices) {
            notifyItemChanged(i)
        }
    }

    inner class ZipViewHolder(private val binding: ItemZipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(zipItem: ZipItem, isSelected: Boolean) {
            binding.itemTitle1.text = zipItem.title
            setBackgroundBasedOnColor(binding.itemZipFastSaveIv, zipItem.color)

            // 선택된 항목에 대한 배경색을 변경합니다.
            binding.root.setBackgroundColor(if (isSelected) Color.LTGRAY else Color.TRANSPARENT)

            binding.root.setOnClickListener {
                val currentlySelected = selectedItems.contains(zipItem)
                if (currentlySelected) {
                    selectedItems.remove(zipItem)
                } else {
                    selectedItems.add(zipItem)
                }
                notifyItemChanged(adapterPosition)
                onItemSelected(zipItem, !currentlySelected)
            }
        }

        private fun setBackgroundBasedOnColor(imageView: ImageView, color: String) {
            when (color.toLowerCase()) {
                "yellow" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_1)
                "lightgreen" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_2)
                "green" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_3)
                "lightblue" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_4)
                "blue" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_5)
                "darkpurple" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_6)
                "purple" -> imageView.setBackgroundResource(R.drawable.ic_zip_shadow_7)
                else -> imageView.setBackgroundResource(R.drawable.ic_zip_clip_shadow) // default
            }
        }
    }
}
