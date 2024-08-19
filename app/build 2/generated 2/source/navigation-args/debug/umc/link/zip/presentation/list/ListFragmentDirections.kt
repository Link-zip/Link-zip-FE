package umc.link.zip.presentation.list

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class ListFragmentDirections private constructor() {
  public companion object {
    public fun actionListFragmentToAlertFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_listFragment_to_alertFragment)
  }
}
