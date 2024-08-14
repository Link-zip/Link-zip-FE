package umc.link.zip.presentation.home.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.databinding.ItemListBinding
import umc.link.zip.databinding.ItemSearchBinding
import umc.link.zip.domain.model.search.SearchRecent
import umc.link.zip.domain.model.search.SearchResult

class SearchRecentRVA (val recent: (Int) -> Unit) : ListAdapter<SearchRecent, SearchRecentRVA.SearchRecentViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchRecentViewHolder {
        return SearchRecentViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchRecentViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class SearchRecentViewHolder(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(recent: SearchRecent){
            with(binding){

            }
        }
    }


    class DiffCallback : DiffUtil.ItemCallback<SearchRecent>() {
        override fun areItemsTheSame(oldItem: SearchRecent, newItem: SearchRecent) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: SearchRecent, newItem: SearchRecent) =
            oldItem == newItem
    }
}