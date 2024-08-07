package umc.link.zip.presentation.list.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import umc.link.zip.R
import umc.link.zip.databinding.ItemLinkBinding
import umc.link.zip.domain.model.List.ZipLink

class OpenZipItemAdapter(var links: List<ZipLink>) : RecyclerView.Adapter<OpenZipItemAdapter.LinkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val binding = ItemLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        holder.bind(links[position])
    }

    override fun getItemCount(): Int = links.size

    inner class LinkViewHolder(private val binding: ItemLinkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(link: ZipLink) {
            with(binding) {
                tvItemLinkLinkName.text = link.title
                tvItemLinkZipVisitCount.text = "${link.likes} 회"
                tvItemLinkLinkDate.text = link.createdAt

                // Load main thumbnail image with Glide
                Glide.with(ivItemLike.context)
                    .load(link.thumbnail)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(ivItemLike)

                // Adjust visibility based on whether the text field is empty or not
                ivItemLinkTypeText.visibility = if (link.text.isNotEmpty()) View.VISIBLE else View.GONE
                ivItemLinkTypeLink.visibility = if (link.text.isEmpty()) View.VISIBLE else View.GONE

                // Set the like icon based on the likes count
                ivItemLike.setImageResource(if (link.likes > 0) R.drawable.ic_heart_selected else R.drawable.ic_heart_unselected)

                // Configure click listener if needed
                itemView.setOnClickListener {
                    // Implement onClick behavior here if required
                }
            }
        }
    }
}
