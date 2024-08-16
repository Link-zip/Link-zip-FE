package umc.link.zip.presentation.home.alert.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.databinding.ItemAlertBinding
import umc.link.zip.domain.model.alert.Alert

class AlertRVA(private val onItemClick: (Alert) -> Unit) : ListAdapter<Alert, AlertRVA.AlertViewHolder>(AlertDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlertViewHolder {
        val binding = ItemAlertBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlertViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlertViewHolder, position: Int) {
        val alert = getItem(position)
        holder.bind(alert)
    }

    inner class AlertViewHolder(private val binding: ItemAlertBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alert: Alert) {
            binding.alert = alert
            binding.executePendingBindings()

            // 아이콘 visibility 설정
            setIconVisibility(alert)

            // 아이템 클릭 리스너 설정
            binding.root.setOnClickListener {
                onItemClick(alert)
            }
        }

        private fun setIconVisibility(alert: Alert) {
            when (alert.alert_type) {
                "original" -> {
                    if (alert.alert_status == 0) {
                        binding.ivItemAlertOriginal0Icon.visibility = View.VISIBLE
                        binding.ivItemAlertOriginal1Icon.visibility = View.INVISIBLE
                    } else {
                        binding.ivItemAlertOriginal0Icon.visibility = View.INVISIBLE
                        binding.ivItemAlertOriginal1Icon.visibility = View.VISIBLE
                    }
                    binding.ivItemAlertRemind0Icon.visibility = View.INVISIBLE
                    binding.ivItemAlertRemind1Icon.visibility = View.INVISIBLE

                    binding.tvItemAlertTypeOriginal.visibility = View.VISIBLE
                    binding.tvItemAlertTypeRemind.visibility = View.INVISIBLE
                }
                "reminder" -> {
                    if (alert.alert_status == 0) {
                        binding.ivItemAlertRemind0Icon.visibility = View.VISIBLE
                        binding.ivItemAlertRemind1Icon.visibility = View.INVISIBLE
                    } else {
                        binding.ivItemAlertRemind0Icon.visibility = View.INVISIBLE
                        binding.ivItemAlertRemind1Icon.visibility = View.VISIBLE
                    }
                    binding.ivItemAlertOriginal0Icon.visibility = View.INVISIBLE
                    binding.ivItemAlertOriginal1Icon.visibility = View.INVISIBLE

                    binding.tvItemAlertTypeOriginal.visibility = View.INVISIBLE
                    binding.tvItemAlertTypeRemind.visibility = View.VISIBLE
                }
            }
        }
    }

    class AlertDiffCallback : DiffUtil.ItemCallback<Alert>() {
        override fun areItemsTheSame(oldItem: Alert, newItem: Alert): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Alert, newItem: Alert): Boolean {
            return oldItem == newItem
        }
    }
}
