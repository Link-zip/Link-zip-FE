package umc.link.zip.presentation.create

import androidx.navigation.ActionOnlyNavDirections
import androidx.navigation.NavDirections
import umc.link.zip.R

public class CustomtextZipFragmentDirections private constructor() {
  public companion object {
    public fun actionCustomtextZipFragmentToCreateFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_customtextZipFragment_to_createFragment)

    public fun actionCustomtextZipFragmentToCustomtextCustomFragment(): NavDirections =
        ActionOnlyNavDirections(R.id.action_customtextZipFragment_to_customtextCustomFragment)
  }
}
