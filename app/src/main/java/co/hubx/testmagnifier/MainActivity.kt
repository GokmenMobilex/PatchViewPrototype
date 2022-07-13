package co.hubx.testmagnifier

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var stickerView: StickerView
    //private lateinit var touchSurface: ImageView

    @SuppressLint("ClickableViewAccessibility", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.iv_image)
        stickerView = findViewById(R.id.iv_sticker)


        //touchSurface = findViewById(R.id.iv_touch_surface)

        /*touchSurface.setOnTouchListener { _, event ->
            stickerView.onTouchEvent(event)
            true
        }*/
    }
}

//private lateinit var magnifier: Magnifier

/* onCreate
magnifier = Magnifier.Builder(imageView)
    .setCornerRadius(150.0f)
    .setClippingEnabled(true)
    .setInitialZoom(1.0f)
    .setSize(300, 300)
    .build()
*/


/* onTouchListener
val viewPosition = IntArray(2)
v.getLocationOnScreen(viewPosition)
magnifier.show(
    (event.rawX - viewPosition[0]),
    (event.rawY - viewPosition[1]),
    ((event.rawX - viewPosition[0]) - 150),
    ((event.rawY - viewPosition[1]) - 150)
)
*/