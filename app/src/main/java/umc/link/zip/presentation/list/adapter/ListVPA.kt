package umc.link.zip.presentation.list.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import umc.link.zip.presentation.list.ListUnreadFragment

class ListVPA(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> ListUnreadFragment()
            1 -> ListUnreadFragment()
            2 -> ListUnreadFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}