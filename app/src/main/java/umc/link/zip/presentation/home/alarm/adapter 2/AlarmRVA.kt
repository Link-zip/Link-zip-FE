import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import umc.link.zip.databinding.ItemAlarmBinding
import umc.link.zip.domain.model.alarm.Alarm

class AlarmRVA(private val onItemClick: (Alarm) -> Unit) : ListAdapter<Alarm, AlarmRVA.AlarmViewHolder>(AlarmDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = getItem(position)
        holder.bind(alarm)
    }

    inner class AlarmViewHolder(private val binding: ItemAlarmBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(alarm: Alarm) {
            binding.alarm = alarm
            binding.executePendingBindings()

            // 아이콘 visibility 설정
            setIconVisibility(alarm)

            // 아이템 클릭 리스너 설정
            binding.root.setOnClickListener {
                onItemClick(alarm)
            }
        }

        private fun setIconVisibility(alarm: Alarm) {
            when (alarm.alarm_type) {
                "original" -> {
                    if (alarm.alarm_status == 0) {
                        binding.ivItemAlarmOriginal0Icon.visibility = View.VISIBLE
                        binding.ivItemAlarmOriginal1Icon.visibility = View.INVISIBLE
                    } else {
                        binding.ivItemAlarmOriginal0Icon.visibility = View.INVISIBLE
                        binding.ivItemAlarmOriginal1Icon.visibility = View.VISIBLE
                    }
                    binding.ivItemAlarmRemind0Icon.visibility = View.INVISIBLE
                    binding.ivItemAlarmRemind1Icon.visibility = View.INVISIBLE

                    binding.tvItemAlarmTypeOriginal.visibility = View.VISIBLE
                    binding.tvItemAlarmTypeRemind.visibility = View.INVISIBLE
                }
                "reminder" -> {
                    if (alarm.alarm_status == 0) {
                        binding.ivItemAlarmRemind0Icon.visibility = View.VISIBLE
                        binding.ivItemAlarmRemind1Icon.visibility = View.INVISIBLE
                    } else {
                        binding.ivItemAlarmRemind0Icon.visibility = View.INVISIBLE
                        binding.ivItemAlarmRemind1Icon.visibility = View.VISIBLE
                    }
                    binding.ivItemAlarmOriginal0Icon.visibility = View.INVISIBLE
                    binding.ivItemAlarmOriginal1Icon.visibility = View.INVISIBLE

                    binding.tvItemAlarmTypeOriginal.visibility = View.INVISIBLE
                    binding.tvItemAlarmTypeRemind.visibility = View.VISIBLE
                }
            }
        }
    }

    class AlarmDiffCallback : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm): Boolean {
            return oldItem == newItem
        }
    }
}
