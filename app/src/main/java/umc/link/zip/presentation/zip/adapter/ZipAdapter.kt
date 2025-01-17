package umc.link.zip.presentation.zip.adapter

import android.annotation.SuppressLint
import android.content.ClipData.Item
import android.graphics.Color
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
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

    //여기부터
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

    //For Delete API
    fun getSelectedZipIds(): List<Int> {
        return selectedItems.map { it.zip_id }
    }

    //여기까지는 수정, 편집을 위한 거라 필요 없으실 겁니당
    //----------------------------------------------------------------------------------

    //recyclerView에 아이템들을 띄우기 위해서 필요한 부분입니다 => 반희님이 써야함
    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<ZipGetItemModel>) {
        items = newItems
        notifyDataSetChanged()
    }

    // 현재 선택된 아이템들을 로그로 출력
    private fun logSelectedItems() {
        Log.d("ZipAdapter", "현재 선택된 아이템들: $selectedItems")
    }

    inner class ZipViewHolder(private val binding: ItemZipBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(zipItem: ZipGetItemModel, isSelected: Boolean, isEditMode: Boolean, position: Int) {
            //Zip List에 바인딩해 주는 부분입니다. - 반희님이 필요하실 겁니당
            binding.itemTitle1.text = zipItem.title
            setBackgroundBasedOnColor(binding.itemZipFastSaveIv, zipItem.color)
            binding.itemLinkCount.text = zipItem.link_count.toString()+"개"
            binding.itemZipSaveTv.text = zipItem.title.take(5)
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

            binding.root.isClickable = false
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


            val action = zipItem.link_count?.let {
                ZipFragmentDirections.actionFragmentZipToFragmentOpenZip(zipItem.zip_id, zipItem.title, zipItem.color,
                    it
                )
            }
            binding.root.setOnClickListener {
                action?.let { it1 -> itemView.findNavController().navigate(it1) }
                Log.d("ZipAdapter","MoveToOpenZip : ${zipItem.zip_id}, ${zipItem.title}, ${zipItem.color}, ${zipItem.link_count}")
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