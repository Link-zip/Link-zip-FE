package umc.link.zip.presentation.mypage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.databinding.ItemListBinding
import umc.link.zip.databinding.ItemNoticeBinding
import umc.link.zip.domain.model.notice.Notice
import java.text.SimpleDateFormat
import java.util.Locale

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
                tvItemNoticeDate.text = modifyDate(notice.createdAt)
                root.setOnClickListener {
                    notice(notice.id)
                }
            }
        }
    }

    private fun modifyDate(data : String) : String{
        return try {
            val date = data.substringBefore("T")
            // 입력 형식: 밀리초와 'Z'를 포함
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            // 출력 형식
            val outputFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())

            // 문자열을 Date 객체로 파싱
            val parsedDate = inputFormat.parse(date)

            // Date 객체를 문자열로 포맷팅
            parsedDate?.let { outputFormat.format(it) } ?: "Invalid Date"
        } catch (e: Exception) {
            "Invalid Date"
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<Notice>() {
        override fun areItemsTheSame(oldItem: Notice, newItem: Notice) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Notice, newItem: Notice) =
            oldItem == newItem
    }
}