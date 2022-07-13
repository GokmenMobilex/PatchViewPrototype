package co.hubx.testmagnifier

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.google.android.material.imageview.ShapeableImageView
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class StickerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr),
    CustomScaleGestureListener.ScaleListener,
    RotationGestureDetector.RotationListener,
    View.OnTouchListener {

    private lateinit var ivFirstCircle: ShapeableImageView
    private lateinit var ivSecondCircle: ShapeableImageView

    private var scaleGestureDetector: ScaleGestureDetector
    private var rotationGestureDetector: RotationGestureDetector

    private val paint = Paint()
    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var closerCircle: ImageView? = null

    init {
        setWillNotDraw(false)
        inflate(context, R.layout.sticker_view, this)
        initPaint()

        val customDetector = CustomScaleGestureListener(this)
        scaleGestureDetector = ScaleGestureDetector(context, customDetector)
        rotationGestureDetector = RotationGestureDetector(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        ivFirstCircle = findViewById(R.id.iv_first_circle)
        ivSecondCircle = findViewById(R.id.iv_second_circle)
    }

    override fun onScale(scaleFactor: Float) {
        ivFirstCircle.scale(scaleFactor)
        ivSecondCircle.scale(scaleFactor)
    }

    override fun onRotate(deltaAngle: Float) {
        ivFirstCircle.rotate(deltaAngle)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val angle = ivFirstCircle.calculateAngle(ivSecondCircle)

        canvas?.drawLine(
            (ivFirstCircle.centerX() + ivFirstCircle.width * cos(angle) / 2),
            (ivFirstCircle.centerY() + ivFirstCircle.width * sin(angle) / 2),
            (ivSecondCircle.centerX() - ivSecondCircle.width * cos(angle) / 2),
            (ivSecondCircle.centerY() - ivSecondCircle.width * sin(angle) / 2),
            paint
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        return onTouch(ivFirstCircle, event)
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        rotationGestureDetector.onTouchEvent(event)

        if (event.pointerCount > 1) return false

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                closerCircle = getCloserImageView(event)
                lastTouchX = event.x
                lastTouchY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                translateView(closerCircle, event)
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                closerCircle = null
            }
        }
        invalidate()

        return true
    }

    private fun initPaint() {
        paint.color = Color.CYAN
        paint.strokeWidth = 3f
    }

    private fun getCloserImageView(event: MotionEvent): ImageView {
        val distanceToFirstCircle = ivFirstCircle.calculateDistanceTo(event.x, event.y)
        val distanceToSecondCircle = ivSecondCircle.calculateDistanceTo(event.x, event.y)

        return if (distanceToFirstCircle <= distanceToSecondCircle) {
            ivFirstCircle
        } else {
            ivSecondCircle
        }
    }

    private fun translateView(view: View?, event: MotionEvent) {
        if (view == null) return

        if (!scaleGestureDetector.isInProgress) {
            val dX = event.x - lastTouchX
            val dY = event.y - lastTouchY

            view.x = (view.x + dX.coerceIn(-25f, 25f))
                .coerceIn(0f, (rootView.right - view.width).toFloat())
            view.y = (view.y + dY.coerceIn(-25f, 25f))
                .coerceIn(0f, (rootView.bottom - view.height).toFloat())

            lastTouchX = event.x
            lastTouchY = event.y
        }
    }
}

private fun ImageView.centerX(): Float {
    return x + (width / 2)
}

private fun ImageView.centerY(): Float {
    return y + (height / 2)
}

private fun ImageView.scale(scaleFactor: Float) {
    val width = (this.width * scaleFactor).coerceIn(30f, 500f).toInt()
    val height = (this.height * scaleFactor).coerceIn(30f, 500f).toInt()
    this.layoutParams = FrameLayout.LayoutParams(width, height)
}

private fun ImageView.rotate(deltaAngle: Float) {
    this.rotation += deltaAngle
}

private fun ImageView.calculateAngle(to: ImageView): Float {
    val deltaX = to.centerX() - this.centerX()
    val deltaY = to.centerY() - this.centerY()
    return atan2(deltaY, deltaX)
}

private fun ImageView.calculateDistanceTo(x: Float, y: Float): Float {
    val zSquare = abs(this.x - x).pow(2f) + abs(this.y - y).pow(2f)
    return sqrt(zSquare)
}