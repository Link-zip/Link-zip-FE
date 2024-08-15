package umc.link.zip.util.extension

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("imageResource")
fun setImageResource(imageView: ImageView, resourceName: String?) {
    if (resourceName != null) {
        val context = imageView.context
        val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
        if (resourceId != 0) {
            imageView.setImageResource(resourceId)
        }
    }
}
