package umc.link.zip.presentation.zip.adapter

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.R
import umc.link.zip.databinding.ItemSelectedZipBinding
import umc.link.zip.databinding.ItemZipBinding
import umc.link.zip.domain.model.ZipItem
import umc.link.zip.domain.model.zip.ZipGetItemModel
import umc.link.zip.presentation.zip.ZipFragment
import umc.link.zip.presentation.zip.ZipFragmentDirections

class OpenZipMoveDialogAdapter(private val onItemSelected: (ZipGetItemModel, Boolean) -> Unit) : RecyclerView.Adapter<OpenZipMoveDialogAdapter.ZipViewHolder>() {
    private var isEditMode: Boolean = false
    private var items: List<ZipGetItemModel> = emptyList()
    private var selectedItems: MutableSet<ZipGetItemModel> = mutableSetOf()
    private var zip_id : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZipViewHolder {
        val binding = ItemSelectedZipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ZipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ZipViewHolder, position: Int) {
        val zipItem = items[position]
        val isSelected = selectedItems.contains(zipItem)
        holder.bind(zipItem, isSelected, isEditMode, position)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<ZipGetItemModel>, excludeZipId: Int) {
        items = newItems.filter { it.zip_id != excludeZipId }
        notifyDataSetChanged()
    }

    // 현재 선택된 아이템들을 로그로 출력
    private fun logSelectedItems() {
        Log.d("ZipAdapter", "현재 선택된 아이템들: $selectedItems")
    }



    inner class ZipViewHolder(private val binding: ItemSelectedZipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(zipItem: ZipGetItemModel, isSelected: Boolean, isEditMode: Boolean, position: Int) {
            binding.itemTitle1.text = zipItem.title
            setBackgroundBasedOnColor(binding.itemZipFastSaveIv, zipItem.color)
            binding.itemLinkCount.text = zipItem.link_count.toString()+"개"
            binding.itemZipSaveTv.text = zipItem.title.take(5)
            zip_id = zipItem.zip_id

            // 선택 상태에 따른 배경색 설정
            binding.root.setBackgroundColor(if (isSelected) Color.parseColor("#F7F8F9") else Color.parseColor("#FFFFFF"))

            // 아이템 클릭 리스너 설정
            binding.root.setOnClickListener {
                if (selectedItems.contains(zipItem)) {
                    selectedItems.remove(zipItem)
                    Log.d("ZipAdapter", "deselectedItem : $selectedItems")
                } else {
                    selectedItems.add(zipItem)
                    Log.d("ZipAdapter", "selectedItem : $selectedItems")
                }
                onItemSelected(zipItem, selectedItems.contains(zipItem))
                notifyItemChanged(adapterPosition)  // 상태 변경 후 UI 업데이트
                logSelectedItems() // 아이템 선택 후 로그 출력
            }
        }

        //Zip color Binding을 위한 함수입니다 - 반희님이 필요하실 겁니당
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