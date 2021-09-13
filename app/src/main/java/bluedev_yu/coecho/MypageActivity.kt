package bluedev_yu.coecho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import bluedev_yu.coecho.databinding.ActivityLoginBinding
import bluedev_yu.coecho.databinding.ActivityMypageBinding
import com.google.firebase.FirebaseApp

class MypageActivity : AppCompatActivity() {

    private var mBinding : ActivityMypageBinding?= null
    private val binding get() = mBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        mBinding = ActivityMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}