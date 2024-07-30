package umc.link.zip.presentation.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import umc.link.zip.R
import umc.link.zip.databinding.ItemListBinding
import umc.link.zip.domain.model.List.Link

class ListUnreadRVA(val unreadLink: (Int) -> Unit) : ListAdapter<Link, ListUnreadRVA.ListViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class ListViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(link: Link){
            with(binding){
                tvItemListLinkName.text = link.title
                tvItemListLinkDate.text = link.createdAt
                tvItemListZipName.text = link.zip.title
                if (link.text.isNotEmpty()) { // text 존재시 텍스트 요약
                    ivItemListTypeText.visibility = View.VISIBLE
                    ivItemListTypeLink.visibility = View.GONE
                }
                if (link.text.isEmpty()) { // 링크 저장
                    ivItemListTypeText.visibility = View.GONE
                    ivItemListTypeLink.visibility = View.VISIBLE
                }
                // 메인 이미지
                Glide.with(ivItemListImgMain.context)
                    .load(link.thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivItemListImgMain)
                // zip 사진
                returnZipColor(link.zip.color, ivItemListZip)
                // 좋아요
                if(link.likes == 1){
                    ivItemLike.setImageResource(R.drawable.ic_heart_selected)
                }else {
                    ivItemLike.setImageResource(R.drawable.ic_heart_unselected)
                }
                root.setOnClickListener {
                    unreadLink(link.id.toInt())
                }
            }
        }
    }

    fun returnZipColor(color : String, view : ImageView){
        val drawable = when(color){
            "blue" -> {
                ContextCompat.getDrawable(view.context, R.drawable.ic_item_zip_blue)
            }
            "yellow" -> {
                ContextCompat.getDrawable(view.context, R.drawable.ic_item_zip_yellow)
            }
            // 추가
            else -> null
        }
        view.setImageDrawable(drawable)
    }

    class DiffCallback : DiffUtil.ItemCallback<Link>() {
        override fun areItemsTheSame(oldItem: Link, newItem: Link) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Link, newItem: Link) =
            oldItem == newItem
    }
}