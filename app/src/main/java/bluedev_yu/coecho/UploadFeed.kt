package bluedev_yu.coecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class UploadFeed : AppCompatActivity() {
    lateinit var cancleWriting: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload_feed)

        cancleWriting.setOnClickListener {
            //프래그먼트와 액티비티의 생명주기??
        }
    }
}