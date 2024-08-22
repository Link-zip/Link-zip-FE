package umc.link.zip.presentation.home

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class HomeFragmentDirections private constructor() {
  public companion object {
    public fun actionHomeFragmentToListFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_listFragment)

    public fun actionHomeFragmentToAlertFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_homeFragment_to_alertFragment)
  }
}
