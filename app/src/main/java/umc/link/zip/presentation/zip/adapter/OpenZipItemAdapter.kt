package umc.link.zip.presentation.zip.adapter


import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import umc.link.zip.R
import umc.link.zip.databinding.ItemLinkBinding
import umc.link.zip.databinding.ItemZipBinding
import umc.link.zip.domain.model.alert.Link
import umc.link.zip.domain.model.link.LinkGetItemModel
import umc.link.zip.domain.model.zip.ZipGetItemModel
import umc.link.zip.presentation.home.HomeFragmentDirections
import umc.link.zip.presentation.zip.OpenZipFragmentDirections
import umc.link.zip.presentation.zip.ZipFragmentDirections

class OpenZipItemAdapter(
    private val onItemSelected: (LinkGetItemModel, Int) -> Unit,
    private val onSelectionCleared: () -> Unit,
    private val onLikeClicked: (LinkGetItemModel) -> Unit,
    private val onBackgroundChangeRequested: (Boolean) -> Unit,  // 배경 변경 요청 콜백
    private val onItemClicked: (LinkGetItemModel) -> Unit,
) : ListAdapter<LinkGetItemModel, OpenZipItemAdapter.LinkViewHolder>(DiffCallback()) {

    private var selectedItems: MutableSet<LinkGetItemModel> = mutableSetOf()
    private var items: List<LinkGetItemModel> = emptyList()
    private var isEditMode: Boolean = false
    private var linkId: Int = 0


    @SuppressLint("NotifyDataSetChanged")
    fun clearSelections() {
        selectedItems.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun selectAllItems() {
        selectedItems.addAll(currentList) // `currentList`는 ListAdapter에서 제공하는 현재 리스트를 반환

        // 선택된 각 아이템에 대해 콜백 호출
        currentList.forEach { item ->
            onItemSelected(item, selectedItems.count())
        }

        Log.d("OpenZipItemAdapter","All selected : $currentList")
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun toggleEditMode() {
        isEditMode = !isEditMode
        notifyDataSetChanged()
    }

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

    @SuppressLint("NotifyDataSetChanged")
    fun setEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode
        if (!isEditMode) {
            clearSelections()  // 일반 모드로 전환 시 선택 항목 초기화
        }
        notifyDataSetChanged()
    }


    @SuppressLint("NotifyDataSetChanged")
    fun resetSelectionAndNotify() {
        selectedItems.clear()
        currentList.forEach { item ->
            onItemSelected(item,0)
        }
        notifyDataSetChanged()
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
        holder.bind(linkItem, isSelected, isEditMode, position)
    }

    inner class LinkViewHolder(private val binding: ItemLinkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            linkItem: LinkGetItemModel,
            isSelected: Boolean,
            isEditMode: Boolean,
            position: Int
        ) {
            binding.root.setBackgroundColor(
                if (isSelected) Color.parseColor("#F5F4FD") else Color.parseColor("#FBFBFB")
            )
            linkId = linkItem.id

            binding.tvItemLinkLinkName.text = linkItem.title
            binding.tvItemLinkZipVisitCount.text = "${linkItem.visit} 회"
            binding.tvItemLinkLinkDate.text = linkItem.created_at.take(10).replace("-", ".")

            if (linkItem.tag == "text") {
                binding.ivItemLinkTypeText.visibility = View.VISIBLE
                binding.ivItemLinkTypeLink.visibility = View.GONE
            } else {
                binding.ivItemLinkTypeText.visibility = View.GONE
                binding.ivItemLinkTypeLink.visibility = View.VISIBLE
            }

            Glide.with(binding.ivItemLinkImgMain.context)
                .load(linkItem.thumb)
                .error(R.drawable.default_img) // 로드에 실패할 때 기본 이미지 설정
                .fallback(R.drawable.default_img) // thumb가 null일 때 기본 이미지 설정
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(binding.ivItemLinkImgMain)

            binding.ivItemLike.setImageResource(if (linkItem.like > 0) R.drawable.ic_heart_selected else R.drawable.ic_heart_unselected)
            if (isEditMode) {
                handleEditMode(linkItem, isSelected)
                binding.ivItemLike.isClickable = false // 편집 모드일 때 좋아요 버튼 클릭 비활성화
            } else {
                handleNormalMode(linkItem, position)
                setLike(linkItem, binding.ivItemLike) { updatedLink ->
                    onLikeClicked(updatedLink)
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
                    if (adapterPosition == 0) {
                        onBackgroundChangeRequested(false)
                    }
                    onItemSelected(linkItem, selectedItems.count())
                    notifyItemChanged(adapterPosition)
                    Log.d("OpenZipItemAdapter", "deselectedItem : $selectedItems")

                    if (selectedItems.isEmpty()) {
                        onSelectionCleared() // 선택된 아이템이 없을 때 콜백 호출
                        onBackgroundChangeRequested(false)
                    }
                } else {
                    selectedItems.add(linkItem)
                    onItemSelected(linkItem, selectedItems.count())
                    notifyItemChanged(adapterPosition)

                    if (adapterPosition == 0) {
                        onBackgroundChangeRequested(true)
                    }
                    Log.d("OpenZipItemAdapter", "selectedItem : $selectedItems")
                }
                logSelectedItems() // 아이템 선택 후 로그 출력
            }
        }

        private fun handleNormalMode(linkItem: LinkGetItemModel, position: Int) {
            resetUI()
            binding.root.setOnClickListener{
                onItemClicked(linkItem)
            }
        }

        private fun resetUI(){
            binding.root.setBackgroundColor(Color.parseColor("#FBFBFB"))
        }
    }

    fun setLike(linkItem: LinkGetItemModel, view: ImageView, onLikeChanged: (LinkGetItemModel) -> Unit) {
        // 초기 상태 설정
        if (linkItem.like == 1) {
            view.setImageResource(R.drawable.ic_heart_selected)
        } else {
            view.setImageResource(R.drawable.ic_heart_unselected)
        }
        // 클릭 리스너 설정
        view.setOnClickListener {
            linkItem.like = if (linkItem.like == 1) 0 else 1
            if (linkItem.like == 1) {
                view.setImageResource(R.drawable.ic_heart_selected)
            } else {
                view.setImageResource(R.drawable.ic_heart_unselected)
            }
            // 좋아요 상태가 변경되었음을 외부에 알림
            onLikeChanged(linkItem)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<LinkGetItemModel>() {
        override fun areItemsTheSame(oldItem: LinkGetItemModel, newItem: LinkGetItemModel) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: LinkGetItemModel, newItem: LinkGetItemModel) =
            oldItem == newItem
    }
}
