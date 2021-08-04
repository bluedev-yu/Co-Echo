package bluedev_yu.coecho

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import bluedev_yu.coecho.Model.SplashDTO
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*

class splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var db = Firebase.firestore

        val random = Random()
        var num = random.nextInt(6)

        db.collection("Splash")?.document(num.toString())?.get().addOnCompleteListener{
            task->
            if(task.isSuccessful)
            {
                var splashDTO = task.result?.toObject(SplashDTO::class.java)
                if (splashDTO != null) {
                    spashText.setText(splashDTO.text)
                }
            }
        }

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        },DURATION)
    }

    companion object{
        private const val DURATION : Long = 5000
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}