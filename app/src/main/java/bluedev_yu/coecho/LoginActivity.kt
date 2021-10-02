package bluedev_yu.coecho

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import bluedev_yu.coecho.databinding.ActivityLoginBinding
import bluedev_yu.coecho.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    var auth: FirebaseAuth? = null //권한 관리 클래스
    var authListener: FirebaseAuth.AuthStateListener?=null
    var googleSignInClient : GoogleSignInClient?= null //구글로그인 관리 클래스

    var GOOGLE_LOGIN_CODE = 9001 // Intent Request ID

    private var mBinding : ActivityLoginBinding?= null
    private val binding get() = mBinding!!



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance() //로그인 통합관리 Object

        //구글로그인 옵션
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //구글 로그인 클래스
        googleSignInClient = GoogleSignIn.getClient(this,gso)

        //구글 로그인 버튼 세팅
        binding.googleSignInButton.setOnClickListener{googleLogin()}


    }

    fun moveMainPage(user:FirebaseUser?){
        //로그인 되었을 시
        if(user!=null)
        {
            Toast.makeText(this,getString(R.string.signin_complete),Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun googleLogin(){
        val signinintent = googleSignInClient?.signInIntent
        startActivityForResult(signinintent, GOOGLE_LOGIN_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == GOOGLE_LOGIN_CODE && resultCode == Activity.RESULT_OK)
        {

            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if(result.isSuccess)
            {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            }
            else
            {

            }
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken,null)

        auth?.signInWithCredential(credential)
            ?.addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    moveMainPage(auth?.currentUser)
                }
            }
    }

    override fun onStart() {
        super.onStart()

        //자동 로그인 설정 -> 로그아웃 안됐는데 다시 로그인페이지로 넘어온 경우 패스하는 역할인듯듯
       moveMainPage(auth?.currentUser)
    }

}
