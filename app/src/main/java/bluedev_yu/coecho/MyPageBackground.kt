package bluedev_yu.coecho

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.databinding.ActivityMyPageBackgroundBinding
import bluedev_yu.coecho.fragment.FragmentMyPage
import bluedev_yu.coecho.sticker.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_my_page_background.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.lang.Exception
import java.util.*
import java.util.Base64.getDecoder

class MyPageBackground : AppCompatActivity() {
    private lateinit var binding: ActivityMyPageBackgroundBinding
    var firestorage : FirebaseStorage? = null
    var auth : FirebaseAuth?= null
    var firestore : FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMyPageBackgroundBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()
        firestorage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val stickerView: StickerView = findViewById(R.id.stickerView)
        //BitmapStickerIcon(아이콘이미지 drawable,아이콘 위치)
        //아이콘위치중에 RIGHT_BOTOM은 만드신분이 오타를 수정안하신듯하다!
        val deleteIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(this, R.drawable.sticker_ic_close_green_30dp),
            BitmapStickerIcon.LEFT_TOP
        )
        val flipIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(this, R.drawable.sticker_ic_flip_green_30dp),
            BitmapStickerIcon.RIGHT_BOTOM
        )
        val scaleIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(this, R.drawable.sticker_ic_scale_green_30dp),
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
        val noSticker : TextView = findViewById(R.id.noSticker)

        //스탬프 보이기 안보이기
        firestore?.collection("User")?.document(auth?.uid.toString())?.get()?.addOnSuccessListener { task ->

            var doc = task?.toObject(userDTO::class.java)

            if (doc?.title!! < 60) {
                stamp6.visibility = View.INVISIBLE
            }
            if (doc?.title!! < 50) {
                stamp5.visibility = View.INVISIBLE
            }
            if (doc?.title!! < 40) {
                stamp4.visibility = View.INVISIBLE
            }
            if (doc?.title!! < 30) {
                stamp3.visibility = View.INVISIBLE
            }
            if (doc?.title!! < 20) {
                stamp2.visibility = View.INVISIBLE
            }
            if (doc?.title!! < 10) {
                stamp1.visibility = View.INVISIBLE
                noSticker.visibility = View.VISIBLE
                //textview visible로 부탁해~~~~~~
            }

            stamp1.setTag("stamp1")
            stamp2.setTag("stamp2")
            stamp3.setTag("stamp3")
            stamp4.setTag("stamp4")
            stamp5.setTag("stamp5")
            stamp6.setTag("stamp6")


            class StampListener : View.OnClickListener {
                override fun onClick(v: View?) {
                    if (v != null) {
                        val img: ImageView = v as ImageView

                        val selectedStickerId = img.getResources()
                            .getIdentifier(img.getTag().toString(), "drawable", getPackageName())
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


            val backgroundCancle: TextView = findViewById(R.id.background_cancle)
            backgroundCancle.setOnClickListener {
                this.finish()
            }


//        val testimg: ImageView = findViewById(R.id.testimg)
            val testlayout: ConstraintLayout = findViewById(R.id.layout_background)

            val backgroundSave: TextView = findViewById(R.id.background_save)
            backgroundSave.setOnClickListener {
//            Toast.makeText(this, "클릭됐음", Toast.LENGTH_SHORT).show()

                //save layout as an image
                val bmp: Bitmap = getBitmapFromView(testlayout) //저장해야할 비트맵 이미지
                CoroutineScope(Dispatchers.Main).launch {
                    doc.mypageBackground = funImageUpLoad(bmp) //이미지 저장

                    //배경사진 업로드
                    firestore?.collection("User")?.document(auth?.uid.toString())?.set(doc)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                makeToast(task.isSuccessful, "배경화면 설정 완료!")

                                val mypage = FragmentMyPage()
                                var fm: FragmentManager = supportFragmentManager
                                var ft: FragmentTransaction = fm.beginTransaction()
                                ft.replace(R.id.background_uploadpage, mypage).commit()
                            }
                        }
                }
//            val backgroundImgStr: String? = BitmapToString(bmp) //백그라운드 이미지
//            val backgroundImg = bmp as ImageView


            }
        }

    }

//    private fun BitmapToString(bitmap: Bitmap): String? {
////        Toast.makeText(this, "비트맵 스트링으로 바꾸는듕", Toast.LENGTH_SHORT).show()
//        val baos = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos)
//        val bytes = baos.toByteArray()
//        return Base64.encodeToString(bytes, Base64.DEFAULT)
//    }

    private fun getBitmapFromView(view: View): Bitmap {
//        Toast.makeText(this, "비트맵 이미지 저장", Toast.LENGTH_SHORT).show()

        //Define a bitmap with the same size as the view
        val returnedBitmap: Bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888)
        //Bind a canvas to it
        val canvas: Canvas = Canvas(returnedBitmap)
        //Get the view's background
//        val bgDrawable: Drawable =view.background
//        if (bgDrawable!=null)
//        //has background drawable, then draw it on the canvas
//            bgDrawable.draw(canvas)
//        else
//        //does not have background drawable, then draw white background on the canvas
//            canvas.drawColor(Color.WHITE)
//        // draw the view on the canvas
        view.draw(canvas)
        //return the bitmap
        return returnedBitmap
    }

    suspend fun funImageUpLoad(bitmap : Bitmap): String? {
        var timestamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var user = auth?.currentUser?.uid.toString()
        var imgFileName = "MypageBackground" + user + timestamp + ".png"
        var storageRef = firestorage?.reference?.child("MypageBackground")?.child(imgFileName)
        try {
            makeToast(true,"업로드중입니다. 잠시 기다려주세요")
            var stream : ByteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream)
            var data = stream.toByteArray()

            storageRef?.putBytes(data)?.await()
            val url = storageRef?.downloadUrl?.await().toString()
            return url
        } catch (e: Exception) {
            Log.e("error:", "error:" + e.message.toString())
            return null
        }
    }

    fun makeToast(success: Boolean,text : String){
        if (success){
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }
}