package umc.link.zip.presentation.zip.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.R
import umc.link.zip.databinding.ItemZipBinding
import umc.link.zip.domain.model.ZipItem
import umc.link.zip.domain.model.zip.ZipGetItemModel

class ZipAdapter(private val onItemSelected: (ZipGetItemModel, Boolean) -> Unit) : RecyclerView.Adapter<ZipAdapter.ZipViewHolder>() {

    private var items: List<ZipGetItemModel> = emptyList()
    private var selectedItems: MutableSet<ZipGetItemModel> = mutableSetOf()
    private lateinit var zip_id : String

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

    @SuppressLint("NotifyDataSetChanged")
    fun selectAllItems() {
        selectedItems.addAll(items)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelections() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    fun updateBackgroundColorOfItems(color: Int) {
        for (i in items.indices) {
            notifyItemChanged(i)
        }
    }

    fun submitList(newItems: List<ZipGetItemModel>) {
        items = newItems
        notifyDataSetChanged()
    }


    inner class ZipViewHolder(private val binding: ItemZipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(zipItem: ZipGetItemModel, isSelected: Boolean) {

            binding.itemTitle1.text = zipItem.title
            setBackgroundBasedOnColor(binding.itemZipFastSaveIv, zipItem.color)
            binding.itemZipSaveTv.text = zipItem.title
            binding.itemLinkCount.text = zipItem.link_count.toString()
            zip_id = zipItem.zip_id.toString()


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
            when (color.lowercase()) {
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
