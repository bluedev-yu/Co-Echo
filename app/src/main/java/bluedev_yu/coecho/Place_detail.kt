package bluedev_yu.coecho

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_place_detail.*
import kotlinx.android.synthetic.main.mapmain.*

class Place_detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        left_arrow.setOnClickListener {
            finish()
        }

    }

}