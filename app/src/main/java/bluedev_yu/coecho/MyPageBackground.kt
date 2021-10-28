package bluedev_yu.coecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import bluedev_yu.coecho.sticker.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_my_page_background.*

class MyPageBackground : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page_background)

        val stickerView: StickerView = findViewById(R.id.stickerView)
        //BitmapStickerIcon(아이콘이미지 drawable,아이콘 위치)
//아이콘위치중에 RIGHT_BOTOM은 만드신분이 오타를 수정안하신듯하다!
        val deleteIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(this, R.drawable.sticker_ic_close_white_18dp),
            BitmapStickerIcon.LEFT_TOP
        )
        val flipIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(this, R.drawable.sticker_ic_flip_white_18dp),
            BitmapStickerIcon.RIGHT_BOTOM
        )
        val scaleIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(this, R.drawable.sticker_ic_scale_white_18dp),
            BitmapStickerIcon.LEFT_BOTTOM
        )

        deleteIcon.setIconEvent(DeleteIconEvent())
        flipIcon.iconEvent = FlipHorizontallyEvent()
        scaleIcon.setIconEvent(ZoomIconEvent())

        //만든 아이콘을 리스트로 만들어주기
        val iconList = listOf(deleteIcon, flipIcon, scaleIcon)

        stickerView.setIcons(iconList)

        fun loadSticker(selectedStickerId: Int){
            val drawable=ContextCompat.getDrawable(this, selectedStickerId)
            val drawableSticker=DrawableSticker(drawable)
            stickerView.addSticker(drawableSticker)
        }

        BottomSheetBehavior.from(layout_background_bottomsheet).apply {
            peekHeight=200
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        //배열로 나중에 바꿀 것
        val stamp1: ImageView = findViewById(R.id.stamp1)
        val stamp2: ImageView = findViewById(R.id.stamp2)
        val stamp3: ImageView = findViewById(R.id.stamp3)
        val stamp4: ImageView = findViewById(R.id.stamp4)
        val stamp5: ImageView = findViewById(R.id.stamp5)
        val stamp6: ImageView = findViewById(R.id.stamp6)

        stamp1.setTag("stamp1")
        stamp2.setTag("stamp2")
        stamp3.setTag("stamp3")
        stamp4.setTag("stamp4")
        stamp5.setTag("stamp5")
        stamp6.setTag("stamp6")


        class StampListener: View.OnClickListener{
            override fun onClick(v: View?) {
                if (v != null) {
                    val img: ImageView = v as ImageView

                    val selectedStickerId = img.getResources().getIdentifier(img.getTag().toString(), "drawable", getPackageName())
//                    val tmp = R.drawable.testimg
                    loadSticker(selectedStickerId)
//                    Toast.makeText(v.context, "내가 구한 아이디: ${selectedStickerId.toString()}, 정답: $tmp", Toast.LENGTH_SHORT).show()
                }
            }

        }

        stamp1.setOnClickListener(StampListener())
        stamp2.setOnClickListener(StampListener())
        stamp3.setOnClickListener(StampListener())
        stamp4.setOnClickListener(StampListener())
        stamp5.setOnClickListener(StampListener())
        stamp6.setOnClickListener(StampListener())


        val cancle: TextView = findViewById(R.id.tv_cancle)
        cancle.setOnClickListener {
            this.finish()
        }
    }


}