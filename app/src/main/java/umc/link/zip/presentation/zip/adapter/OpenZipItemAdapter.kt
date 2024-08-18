package umc.link.zip.presentation.zip.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import umc.link.zip.R
import umc.link.zip.databinding.ItemLinkBinding
import umc.link.zip.domain.model.link.LinkGetItemModel

class OpenZipItemAdapter(private var link: (Int) -> Unit) : ListAdapter<LinkGetItemModel, OpenZipItemAdapter.LinkViewHolder>(DiffCallback()) {

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
        holder.bind(currentList[position])
    }

    inner class LinkViewHolder(private val binding: ItemLinkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(link: LinkGetItemModel) {
            with(binding) {
                tvItemLinkLinkName.text = link.title
                tvItemLinkZipVisitCount.text = "${link.like} 회"
                tvItemLinkLinkDate.text = link.created_at

                // Load main thumbnail image with Glide
                Glide.with(ivItemLinkImgMain.context)
                    .load(link.thumb)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivItemLinkImgMain)

                // Adjust visibility based on whether the text field is empty or not
                ivItemLinkTypeText.visibility = if (link.text.toString().isNotEmpty()) View.VISIBLE else View.GONE
                ivItemLinkTypeLink.visibility = if (link.text.toString().isEmpty()) View.VISIBLE else View.GONE

                // Set the like icon based on the likes count
                ivItemLike.setImageResource(if (link.like > 0) R.drawable.ic_heart_selected else R.drawable.ic_heart_unselected)

                // Configure click listener if needed
                itemView.setOnClickListener {
                    // Implement onClick behavior here if required
                }
                root.setOnClickListener {
                    link(link.id)
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<LinkGetItemModel>() {
        override fun areItemsTheSame(oldItem: LinkGetItemModel, newItem: LinkGetItemModel) =
            oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: LinkGetItemModel, newItem: LinkGetItemModel) =
            oldItem == newItem
    }
}
