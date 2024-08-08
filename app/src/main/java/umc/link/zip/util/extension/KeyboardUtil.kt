package umc.link.zip.util.extension

import android.graphics.Rect
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import androidx.core.widget.NestedScrollView

object KeyboardUtil {
    private var globalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    fun registerKeyboardVisibilityListener(rootView: View, scrollView: NestedScrollView, targetView: View, noneView :View) {
        globalLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
            val rect = Rect()
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.rootView.height
            val keypadHeight = screenHeight - rect.bottom

            Log.d("rect bottom", rect.bottom.toString())
            Log.d("screenHeight", screenHeight.toString())
            Log.d("scrollView", scrollView.bottom.toString())
            Log.d("keypadHeight", keypadHeight.toString())

            // 키보드가 화면의 20% 이상 차지할 경우
            if (keypadHeight > screenHeight * 0.2) {
                scrollView.post {
                    scrollView.smoothScrollTo(0, targetView.bottom)
                    noneView.visibility = View.GONE
                }
            } else {
                // 키보드가 사라졌을 때의 처리 (원래 위치로 스크롤)
                scrollView.post {
                    scrollView.smoothScrollTo(0, 0)
                    noneView.visibility = View.VISIBLE
                }
            }
        }
        rootView.viewTreeObserver.addOnGlobalLayoutListener(globalLayoutListener)
    }

    fun unregisterKeyboardVisibilityListener(rootView: View) {
        globalLayoutListener?.let {
            rootView.viewTreeObserver.removeOnGlobalLayoutListener(it)
        }
    }
}