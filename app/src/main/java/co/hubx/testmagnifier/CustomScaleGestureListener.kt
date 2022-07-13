package co.hubx.testmagnifier

import android.view.ScaleGestureDetector

class CustomScaleGestureListener(
    private var listener: ScaleListener? = null
) : ScaleGestureDetector.SimpleOnScaleGestureListener() {

    private var scaleFactor = 1f

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        scaleFactor *= detector.scaleFactor
        scaleFactor = scaleFactor.coerceIn(0.85f, 1.15f)
        listener?.onScale(scaleFactor)

        return true
    }

    interface ScaleListener {
        fun onScale(scaleFactor: Float)
    }
}