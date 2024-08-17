package umc.link.zip.presentation.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.databinding.ItemListBinding
import umc.link.zip.databinding.ItemNoticeBinding
import umc.link.zip.domain.model.notice.Notice

class NoticeRVA (val notice: (Int) -> Unit) : ListAdapter<Notice, NoticeRVA.NoticeViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        return NoticeViewHolder(
            ItemNoticeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class NoticeViewHolder(private val binding: ItemNoticeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notice: Notice){
            with(binding){
                tvItemNoticeTitle.text = notice.title
                tvItemNoticeDate.text = notice.createdAt
                root.setOnClickListener {
                    notice(notice.id.toInt())
                }
            }
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<Notice>() {
        override fun areItemsTheSame(oldItem: Notice, newItem: Notice) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Notice, newItem: Notice) =
            oldItem == newItem
    }
}