package umc.link.zip.presentation.mypage

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import umc.link.zip.R

class CornerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val path = Path()
    private var radius = 0f

    init {
        context.withStyledAttributes(attrs, R.styleable.CornerView) {
            radius = getDimension(R.styleable.CornerView_radius, 0f)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val rect = RectF(0f, 0f, w.toFloat(), h.toFloat())
        path.reset()
        path.addRoundRect(rect, radius, radius, Path.Direction.CW)
        path.close()
    }

    override fun dispatchDraw(canvas: Canvas) {
        val save = canvas.save()
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }
}
