package umc.link.zip.presentation.home.search.adapter

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
import umc.link.zip.domain.model.search.SearchLinkResult
import java.text.SimpleDateFormat
import java.util.Locale

class SearchResultRVA (private val onItemClicked: (SearchLinkResult) -> Unit,
                       private val onLikeClicked: (SearchLinkResult) -> Unit) : ListAdapter<SearchLinkResult, SearchResultRVA.SearchResultViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        val link = getItem(position)
        holder.bind(link)
    }

    inner class SearchResultViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(link: SearchLinkResult) {
            with(binding) {
                tvItemListLinkName.text = link.link.title
                tvItemListLinkDate.text = modifyDate(link.link.createdAt)
                tvItemListZipName.text = link.zip.title
                if (link.link.tag == "text") { // text 존재시 텍스트 요약
                    ivItemListTypeText.visibility = View.VISIBLE
                    ivItemListTypeLink.visibility = View.GONE
                }else{ // 링크 저장
                    ivItemListTypeText.visibility = View.GONE
                    ivItemListTypeLink.visibility = View.VISIBLE
                }
                if(link.link.thumbnail==null){
                    ivItemListImgMain.setImageResource(R.drawable.iv_link_thumbnail_default)
                }else {
                    Glide.with(ivItemListImgMain.context)
                        .load(link.link.thumbnail)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivItemListImgMain)
                }
                // zip 사진
                returnZipColor(link.zip.color, ivItemListZip)
                // 좋아요
                setLike(link, ivItemLike) { updatedLink ->
                    onLikeClicked(updatedLink) // 좋아요 상태 변경 시 ViewModel 호출
                }
                root.setOnClickListener {
                    onItemClicked(link)
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

    fun returnZipColor(color : String, view : ImageView){
        val drawable = when(color){
            "blue" -> {
                ContextCompat.getDrawable(view.context, R.drawable.ic_item_zip_blue)
            }
            "yellow" -> {
                ContextCompat.getDrawable(view.context, R.drawable.ic_item_zip_yellow)
            }
            "darkpurple" -> {
                ContextCompat.getDrawable(view.context, R.drawable.ic_item_zip_darkpurple)
            }
            "green" -> {
                ContextCompat.getDrawable(view.context, R.drawable.ic_item_zip_green)
            }
            "lightblue" -> {
                ContextCompat.getDrawable(view.context, R.drawable.ic_item_zip_lightblue)
            }
            "lightgreen" -> {
                ContextCompat.getDrawable(view.context, R.drawable.ic_item_zip_lightgreen)
            }
            "purple" -> {
                ContextCompat.getDrawable(view.context, R.drawable.ic_item_zip_purple)
            }
            "default" -> {
                ContextCompat.getDrawable(view.context, R.drawable.ic_item_zip_default)
            }
            // 추가
            else -> null
        }
        view.setImageDrawable(drawable)
    }

    fun setLike(link: SearchLinkResult, view: ImageView, onLikeChanged: (SearchLinkResult) -> Unit) {
        // 초기 상태 설정
        if (link.link.like == 1) {
            view.setImageResource(R.drawable.ic_heart_selected)
        } else {
            view.setImageResource(R.drawable.ic_heart_unselected)
        }

        // 클릭 리스너 설정
        view.setOnClickListener {
            link.link.like = if (link.link.like == 1) 0 else 1
            if (link.link.like == 1) {
                view.setImageResource(R.drawable.ic_heart_selected)
            } else {
                view.setImageResource(R.drawable.ic_heart_unselected)
            }
            // 좋아요 상태가 변경되었음을 외부에 알림
            onLikeChanged(link)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<SearchLinkResult>() {
        override fun areItemsTheSame(oldItem: SearchLinkResult, newItem: SearchLinkResult) =
            oldItem.link.id == newItem.link.id
        override fun areContentsTheSame(oldItem: SearchLinkResult, newItem: SearchLinkResult) =
            oldItem == newItem
    }
}