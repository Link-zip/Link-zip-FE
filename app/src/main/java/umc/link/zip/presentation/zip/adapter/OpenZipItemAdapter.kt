package umc.link.zip.presentation.zip.adapter.OpenZipItemAdapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.AndroidEntryPoint
import umc.link.zip.R
import umc.link.zip.databinding.ItemLinkBinding
import umc.link.zip.domain.model.ZipLinkItem
import umc.link.zip.domain.model.link.LinkGetItemModel
import umc.link.zip.domain.model.link.LinkGetModel

class OpenZipItemAdapter(private var links: List<LinkGetItemModel>) :
    RecyclerView.Adapter<OpenZipItemAdapter.LinkViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val binding = ItemLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        holder.bind(links[position])
    }

    override fun getItemCount(): Int = links.size

    fun updateData(newLinks: List<LinkGetItemModel>) {
        links = newLinks
        notifyDataSetChanged()
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
                ivItemLinkTypeText.visibility = if (link.text.isNotEmpty()) View.VISIBLE else View.GONE
                ivItemLinkTypeLink.visibility = if (link.text.isEmpty()) View.VISIBLE else View.GONE

                // Set the like icon based on the likes count
                ivItemLike.setImageResource(if (link.like > 0) R.drawable.ic_heart_selected else R.drawable.ic_heart_unselected)

                // Configure click listener if needed
                itemView.setOnClickListener {
                    // Implement onClick behavior here if required
                }
            }
        }
    }
}
