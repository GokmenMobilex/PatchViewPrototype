package co.hubx.testmagnifier

import android.view.MotionEvent
import kotlin.math.atan2

class RotationGestureDetector(
    private var listener: RotationListener? = null
) {

    private var mRotation: Float = 0f

    fun onTouchEvent(event: MotionEvent) {
        if (event.pointerCount != 2) return

        if (event.actionMasked == MotionEvent.ACTION_POINTER_DOWN) {
            mRotation = rotation(event)
        }

        val rotation: Float = rotation(event)
        val delta: Float = rotation - mRotation
        mRotation += delta

        if (listener != null) {
            listener?.onRotate(delta)
        }
    }

    private fun rotation(event: MotionEvent): Float {
        val deltaX: Double = (event.getX(0) - event.getX(1)).toDouble()
        val deltaY: Double = (event.getY(0) - event.getY(1)).toDouble()
        val radians: Double = atan2(deltaY, deltaX)
        return Math.toDegrees(radians).toFloat()
    }

    interface RotationListener {
        fun onRotate(deltaAngle: Float)
    }
}