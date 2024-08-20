package umc.link.zip.presentation.zip.adapter


import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import umc.link.zip.R
import umc.link.zip.databinding.ItemLinkBinding
import umc.link.zip.databinding.ItemZipBinding
import umc.link.zip.domain.model.link.LinkGetItemModel
import umc.link.zip.domain.model.zip.ZipGetItemModel
import umc.link.zip.presentation.zip.ZipFragmentDirections

class OpenZipItemAdapter(
    private val onItemSelected: (LinkGetItemModel, Boolean) -> Unit,
    private val onSelectionCleared: () -> Unit
) : ListAdapter<LinkGetItemModel, OpenZipItemAdapter.LinkViewHolder>(DiffCallback()) {

    private var selectedItems: MutableSet<LinkGetItemModel> = mutableSetOf()
    private var items: List<LinkGetItemModel> = emptyList()
    private var isEditMode: Boolean = false


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
    fun selectAllItems() {
        selectedItems.addAll(currentList) // `currentList`는 ListAdapter에서 제공하는 현재 리스트를 반환
        Log.d("OpenZipItemAdapter","All selected : $currentList")
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun toggleEditMode() {
        isEditMode = !isEditMode
        notifyDataSetChanged()
    }

   /* @SuppressLint("NotifyDataSetChanged")
    fun submitList(newItems: List<LinkGetItemModel>) {
        items = newItems
        notifyDataSetChanged()
    }
*/
    // 현재 선택된 아이템들을 로그로 출력
    fun logSelectedItems() {
        Log.d("OpenZipItemAdapter", "현재 선택된 아이템들: $selectedItems")
    }

    // 현재 선택된 아이템들을 로그로 출력
    fun logdeSelectedItems() {
        Log.d("OpenZipItemAdapter", "현재 취소된 아이템들: $selectedItems")
    }

    //For Delete API
    fun getSelectedLinkIds(): List<Int> {
        return selectedItems.map { it.id }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        return LinkViewHolder(
            ItemLinkBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        val linkItem = getItem(position) // ListAdapter에서 제공하는 getItem 사용
        val isSelected = selectedItems.contains(linkItem)
        holder.itemView.setBackgroundColor(
            if (isSelected) Color.parseColor("#F5F4FD") else Color.parseColor("#FBFBFB")
        )
        holder.bind(linkItem, isSelected, isEditMode, position, onItemSelected, onSelectionCleared)
    }

    inner class LinkViewHolder(private val binding: ItemLinkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            linkItem: LinkGetItemModel,
            isSelected: Boolean,
            isEditMode: Boolean,
            position: Int,
            onItemSelected: (LinkGetItemModel, Boolean) -> Unit,
            onSelectionCleared: () -> Unit
        ) {
            with(binding) {
                root.setBackgroundColor(
                    if (isSelected) Color.parseColor("#F5F4FD") else Color.parseColor("#FBFBFB")
                )

                tvItemLinkLinkName.text = linkItem.title
                tvItemLinkZipVisitCount.text = "${linkItem.like} 회"
                tvItemLinkLinkDate.text = linkItem.created_at

                if (linkItem.tag == "text") {
                    ivItemLinkTypeText.visibility = View.VISIBLE
                    ivItemLinkTypeLink.visibility = View.GONE
                } else {
                    ivItemLinkTypeText.visibility = View.GONE
                    ivItemLinkTypeLink.visibility = View.VISIBLE
                }

                // Load main thumbnail image with Glide
                Glide.with(ivItemLinkImgMain.context)
                    .load(linkItem.thumb)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivItemLinkImgMain)

                // Set the like icon based on the likes count
                ivItemLike.setImageResource(if (linkItem.like > 0) R.drawable.ic_heart_selected else R.drawable.ic_heart_unselected)

                if (isEditMode) {
                    handleEditMode(linkItem, isSelected)
                } else {
                    handleNormalMode(linkItem, position)
                }
            }
        }

        private fun handleEditMode(linkItem: LinkGetItemModel, isSelected: Boolean) {
            resetUI()

            binding.root.setBackgroundColor(
                if (isSelected) Color.parseColor("#F5F4FD") else Color.parseColor(
                    "#FBFBFB"
                )
            )

            // 아이템 클릭 리스너 설정
            binding.root.setOnClickListener {
                if (selectedItems.contains(linkItem)) {
                    selectedItems.remove(linkItem)
                    notifyItemChanged(adapterPosition)
                    Log.d("OpenZipItemAdapter", "deselectedItem : $selectedItems")
                    if (selectedItems.isEmpty()) {
                        onSelectionCleared() // 선택된 아이템이 없을 때 콜백 호출
                    }
                } else {
                    selectedItems.add(linkItem)
                    notifyItemChanged(adapterPosition)
                    Log.d("OpenZipItemAdapter", "selectedItem : $selectedItems")
                }
                onItemSelected(linkItem, selectedItems.contains(linkItem))
                notifyItemChanged(adapterPosition)  // 상태 변경 후 UI 업데이트
                logSelectedItems() // 아이템 선택 후 로그 출력
            }
        }

        private fun handleNormalMode(linkItem: LinkGetItemModel, position: Int) {
            resetUI()

            /*val action = ZipFragmentDirections.actionFragmentZipToFragmentOpenZip(linkItem.zip_id)
            binding.root.setOnClickListener {
                // Navigate to detail page or any specific action
                itemView.findNavController().navigate(action)
                Log.d("ZipAdapter", "MoveToOpenZip : ${linkItem.zip_id}")
            }*/

            Log.d("OpenZipItemAdapter", "Normal 모드에서 클릭됨")
        }

        private fun resetUI(){
            binding.root.setBackgroundColor(Color.parseColor("#FBFBFB"))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<LinkGetItemModel>() {
        override fun areItemsTheSame(oldItem: LinkGetItemModel, newItem: LinkGetItemModel) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: LinkGetItemModel, newItem: LinkGetItemModel) =
            oldItem == newItem
    }
}
