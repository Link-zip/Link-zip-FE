package umc.link.zip.presentation.home.search.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import umc.link.zip.R
import umc.link.zip.databinding.ItemListBinding
import umc.link.zip.domain.model.search.SearchResult
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SearchResultRVA (val result: (Int) -> Unit) : ListAdapter<SearchResult, SearchResultRVA.SearchResultViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        return SearchResultViewHolder(
            ItemListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class SearchResultViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(link: SearchResult) {
            with(binding) {
                tvItemListLinkName.text = link.title
                tvItemListLinkDate.text = modifyDate(link)
                tvItemListZipName.text = link.zipName
                if (link.tag == "text") { // text 존재시 텍스트 요약
                    ivItemListTypeText.visibility = View.VISIBLE
                    ivItemListTypeLink.visibility = View.GONE
                }else{ // 링크 저장
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
                    result(result.id.toInt())
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun modifyDate(data : SearchResult) : String{
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

        // 문자열을 LocalDateTime으로 변환
        val createdDateTime = LocalDateTime.parse(data.createdAt.toString(), formatter)
        val updatedDateTime = LocalDateTime.parse(data.updatedAt.toString(), formatter)

        // 더 나중의 시각 선택
        val laterDateTime = if (createdDateTime.isAfter(updatedDateTime)) {
            createdDateTime
        } else {
            updatedDateTime
        }

        // "T" 앞까지만 반환
        return laterDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
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




    class DiffCallback : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) =
            oldItem == newItem
    }
}