package umc.link.zip.presentation.zip.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.R
import umc.link.zip.databinding.FragmentCustomtextZipBinding
import umc.link.zip.databinding.ItemSaveZipBinding
import umc.link.zip.databinding.ItemZipBinding
import umc.link.zip.domain.model.ZipItem
import umc.link.zip.domain.model.zip.ZipGetItemModel

class CustomtextZipItemAdapter(private val onItemSelected: (ZipGetItemModel, Boolean) -> Unit) : RecyclerView.Adapter<CustomtextZipItemAdapter.ZipViewHolder>() {

    private var items: List<ZipGetItemModel> = emptyList()
    private var selectedItem: ZipGetItemModel? = null // 단일 선택을 위한 변수

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZipViewHolder {
        val binding = ItemSaveZipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ZipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ZipViewHolder, position: Int) {
        val zipItem = items[position]
        val isSelected = selectedItem == zipItem
        holder.bind(zipItem, isSelected)
    }

    override fun getItemCount(): Int = items.size

    fun submitList(newItems: List<ZipGetItemModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    fun clearSelections() {
        selectedItem = null
        notifyDataSetChanged()
    }

    inner class ZipViewHolder(private val binding: ItemSaveZipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(zipItem: ZipGetItemModel, isSelected: Boolean) {
            binding.itemTitle1.text = zipItem.title
            setBackgroundBasedOnColor(binding.itemImage1, zipItem.color)

            // 선택된 항목에 대한 배경색을 변경합니다.
            binding.root.setBackgroundColor(if (isSelected) Color.parseColor("F7F8F9") else Color.TRANSPARENT)

            binding.root.setOnClickListener {
                // 선택된 항목이 이미 있는 경우 선택을 해제
                if (selectedItem == zipItem) {
                    selectedItem = null
                    onItemSelected(zipItem, false)
                } else {
                    // 다른 항목이 선택되면 기존 선택을 해제하고 새 항목을 선택
                    selectedItem = zipItem
                    onItemSelected(zipItem, true)
                }
                notifyDataSetChanged() // 선택 상태 업데이트
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

