package umc.link.zip.presentation.zip.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.R
import umc.link.zip.databinding.ItemZipBinding
import umc.link.zip.domain.model.ZipItem
import umc.link.zip.domain.model.zip.ZipGetItemModel
import umc.link.zip.presentation.zip.ZipFragment
import umc.link.zip.presentation.zip.ZipFragmentDirections

class ZipAdapter(private val onItemSelected: (ZipGetItemModel, Boolean) -> Unit) : RecyclerView.Adapter<ZipAdapter.ZipViewHolder>() {
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
        selectedItems.addAll(items)
        notifyDataSetChanged()
    }

    fun toggleEditMode() {
        isEditMode = !isEditMode
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
        fun bind(zipItem: ZipGetItemModel, isSelected: Boolean, isEditMode: Boolean, position: Int) {
            binding.itemTitle1.text = zipItem.title
            setBackgroundBasedOnColor(binding.itemZipFastSaveIv, zipItem.color)
            binding.itemZipSaveTv.text = zipItem.title
            binding.itemLinkCount.text = zipItem.link_count.toString()
            zip_id = zipItem.zip_id

            // Edit mode changes
            if (isEditMode) {
                if (position == 0) {
                    binding.root.setBackgroundColor(Color.parseColor("#DDFAFB"))
                    binding.itemSubtitle1.text = "삭제 불가능한 zip"
                    binding.itemSubtitle1.setTextColor(Color.parseColor("#1191AD"))
                    binding.itemSubtitle2.visibility = View.GONE
                    binding.itemLinkCount.visibility = View.GONE
                } else {
                    binding.root.setBackgroundColor(Color.parseColor("#FBFBFB"))
                    binding.itemSubtitle1.text = "링크 개수"
                    binding.itemSubtitle2.visibility = View.VISIBLE
                    binding.itemLinkCount.visibility = View.VISIBLE
                }

                binding.root.setOnClickListener {
                    val currentlySelected = selectedItems.contains(zipItem)
                    if (currentlySelected) {
                        Log.d("ZipAdapter","AlreadySelectedItem : $zipItem")
                    } else {
                        Log.d("ZipAdapter","SelectedItem : $zipItem")                    }
                    notifyItemChanged(adapterPosition)
                    onItemSelected(zipItem, !currentlySelected)
                }
            } else {
                // Normal mode actions
                binding.root.setBackgroundColor(Color.TRANSPARENT)
                val action = ZipFragmentDirections.actionFragmentZipToFragmentOpenZip(zipItem.zip_id)
                binding.root.setOnClickListener {
                    // Navigate to detail page or any specific action
                    itemView.findNavController().navigate(action)
                    Log.d("ZipAdapter","MoveToOpenZip : ${zipItem.zip_id}")
                }
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