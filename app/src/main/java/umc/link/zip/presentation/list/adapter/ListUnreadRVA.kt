package umc.link.zip.presentation.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import umc.link.zip.R
import umc.link.zip.databinding.ItemListBinding
import umc.link.zip.domain.model.list.Link

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
                if (link.tag=="text") { // text 존재시 텍스트 요약
                    ivItemListTypeText.visibility = View.VISIBLE
                    ivItemListTypeLink.visibility = View.GONE
                }else{ // 링크 저장 tag =="link"
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
                setLike(link, ivItemLike) { updatedLink ->
                    // 서버에 변경 사항 반영
                    // updateLikeStatusOnServer(updatedLink)
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

    fun setLike(link: Link, view: ImageView, onLikeChanged: (Link) -> Unit) {
        // 초기 상태 설정
        if (link.like == 1) {
            view.setImageResource(R.drawable.ic_heart_selected)
        } else {
            view.setImageResource(R.drawable.ic_heart_unselected)
        }

        // 클릭 리스너 설정
        view.setOnClickListener {
            link.like = if (link.like == 1) 0 else 1
            if (link.like == 1) {
                view.setImageResource(R.drawable.ic_heart_selected)
            } else {
                view.setImageResource(R.drawable.ic_heart_unselected)
            }
            // 좋아요 상태가 변경되었음을 외부에 알림
            onLikeChanged(link)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Link>() {
        override fun areItemsTheSame(oldItem: Link, newItem: Link) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Link, newItem: Link) =
            oldItem == newItem
    }
}