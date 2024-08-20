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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.R
import umc.link.zip.databinding.ItemZipBinding
import umc.link.zip.domain.model.ZipItem
import umc.link.zip.domain.model.zip.ZipGetItemModel
import umc.link.zip.presentation.zip.ZipFragment
import umc.link.zip.presentation.zip.ZipFragmentDirections

class ZipAdapter(private val onItemSelected: (ZipGetItemModel, Boolean) -> Unit, private val onSelectionCleared: () -> Unit) : RecyclerView.Adapter<ZipAdapter.ZipViewHolder>() {
    private var isEditMode: Boolean = false
    private var items: List<ZipGetItemModel> = emptyList()
    private var selectedItems: MutableSet<ZipGetItemModel> = mutableSetOf()
    private var zip_id : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZipViewHolder {
        val binding = ItemZipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ZipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ZipViewHolder, position: Int) {
        val zipItem = items[position]
        val isSelected = selectedItems.contains(zipItem)
        holder.bind(zipItem, isSelected, isEditMode, position)
    }

    override fun getItemCount(): Int = items.size

    @SuppressLint("NotifyDataSetChanged")
    fun selectAllItems() {
        selectedItems.addAll(items.filter { it.zip_id != 0 }) // 빠른 저장 zip 제외
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun toggleEditMode() {
        isEditMode = !isEditMode
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearSelections() {
        val previousSelectedItems = selectedItems.toList()
        selectedItems.clear()
        previousSelectedItems.forEach { item ->
            val position = items.indexOf(item)
            if (position >= 0) {
                notifyItemChanged(position)  // 선택 해제된 항목만 업데이트
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<ZipGetItemModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    // 현재 선택된 아이템들을 로그로 출력
    private fun logSelectedItems() {
        Log.d("ZipAdapter", "현재 선택된 아이템들: $selectedItems")
    }


    //For Delete API
    fun getSelectedZipIds(): List<Int> {
        return selectedItems.map { it.zip_id }
    }

    inner class ZipViewHolder(private val binding: ItemZipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(zipItem: ZipGetItemModel, isSelected: Boolean, isEditMode: Boolean, position: Int) {
            binding.itemTitle1.text = zipItem.title
            setBackgroundBasedOnColor(binding.itemZipFastSaveIv, zipItem.color)
            binding.itemZipSaveTv.text = zipItem.title
            binding.itemLinkCount.text = zipItem.link_count.toString()
            zip_id = zipItem.zip_id

            if (isEditMode) {
                if (position == 0) {
                    // 빠른 저장 Zip의 특수 처리
                    handleFastSaveZip()
                } else {
                    handleEditMode(zipItem, isSelected)
                }
            } else {
                handleNormalMode(zipItem, position)
            }
        }

        private fun handleFastSaveZip() {
            // 편집 모드에서 빠른 저장 Zip의 특수 UI 설정
            binding.root.setBackgroundColor(Color.parseColor("#DDFAFB"))
            binding.itemSubtitle1.text = "삭제 불가능한 zip"
            binding.itemSubtitle1.setTextColor(Color.parseColor("#1191AD"))
            binding.itemSubtitle2.visibility = View.GONE
            binding.itemLinkCount.visibility = View.GONE

            // 클릭 리스너 설정 - OpenZip으로 이동하지 않고 토스트 메시지 출력
            binding.root.setOnClickListener {
                Toast.makeText(binding.root.context, "삭제할 수 없는 Zip입니다.", Toast.LENGTH_SHORT).show()
            }
        }

        private fun handleEditMode(zipItem: ZipGetItemModel, isSelected: Boolean) {
            // ViewHolder 상태 초기화
            resetUI()

            // 선택 상태에 따른 배경색 설정
            binding.root.setBackgroundColor(if (isSelected) Color.parseColor("#F4F5F6") else Color.parseColor("#FBFBFB"))

            // 아이템 클릭 리스너 설정
            binding.root.setOnClickListener {
                if (selectedItems.contains(zipItem)) {
                    selectedItems.remove(zipItem)
                    Log.d("ZipAdapter", "deselectedItem : $selectedItems")
                    if (selectedItems.isEmpty()) {
                        onSelectionCleared() // 선택된 아이템이 없을 때 콜백 호출
                    }
                } else {
                    selectedItems.add(zipItem)
                    Log.d("ZipAdapter", "selectedItem : $selectedItems")
                }
                onItemSelected(zipItem, selectedItems.contains(zipItem))
                notifyItemChanged(adapterPosition)  // 상태 변경 후 UI 업데이트
                logSelectedItems() // 아이템 선택 후 로그 출력
            }
        }

        private fun handleNormalMode(zipItem: ZipGetItemModel, position: Int) {
            // ViewHolder 상태 초기화
            resetUI()

            // Fast Save Zip (position 0)일 때 디자인을 복원
            if (position == 0) {
                binding.root.setBackgroundColor(Color.parseColor("#FBFBFB"))
                binding.itemSubtitle1.text = "링크 개수"
                binding.itemSubtitle1.setTextColor(Color.parseColor("#666666"))
                binding.itemSubtitle2.visibility = View.VISIBLE
                binding.itemLinkCount.visibility = View.VISIBLE
            } else {
                binding.root.setBackgroundColor(Color.TRANSPARENT)
            }

            val action = ZipFragmentDirections.actionFragmentZipToFragmentOpenZip(zipItem.zip_id)
            binding.root.setOnClickListener {
                // Navigate to detail page or any specific action
                itemView.findNavController().navigate(action)
                Log.d("ZipAdapter","MoveToOpenZip : ${zipItem.zip_id}")
            }
        }

        private fun resetUI() {
            // ViewHolder 상태 초기화
            binding.itemSubtitle1.text = "링크 개수"
            binding.itemSubtitle1.setTextColor(Color.parseColor("#666666"))
            binding.itemSubtitle2.visibility = View.VISIBLE
            binding.itemLinkCount.visibility = View.VISIBLE
            binding.root.setBackgroundColor(Color.TRANSPARENT)
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