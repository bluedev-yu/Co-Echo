package bluedev_yu.coecho

import android.content.Intent
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import bluedev_yu.coecho.Model.SplashDTO
import bluedev_yu.coecho.databinding.ActivitySplashBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class splash : AppCompatActivity() {
    private var mBinding: ActivitySplashBinding? = null

    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var db = Firebase.firestore

        val random = Random()
        var num = random.nextInt(5)

        val res : Resources = resources
        var splashArray = res.getStringArray(R.array.splash)

        var text = splashArray[num].toString()

        binding.splashText.setText(text)

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish()
        },DURATION)
    }

    companion object{
        private const val DURATION : Long = 3000
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}