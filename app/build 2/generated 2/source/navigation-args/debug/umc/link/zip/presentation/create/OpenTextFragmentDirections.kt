package umc.link.zip.presentation.create

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class OpenTextFragmentDirections private constructor() {
  public companion object {
    public fun actionOpenTextFragmentToCustomTextCustomFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_openTextFragment_to_customTextCustomFragment)
  }
}
