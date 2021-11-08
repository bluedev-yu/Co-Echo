package bluedev_yu.coecho

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.Signature
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_dev_menu.*
import java.security.MessageDigest

class DevMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dev_menu)
        printReleaseKey()
    }
    fun printReleaseKey()
    {
        var packageInfo: PackageInfo? = null
        try{
            packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            packageInfo.signatures
            for(signature: Signature in packageInfo.signatures) {
                var md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                var smt =String(Base64.encode(md.digest(),0))
                tv_dev_release_key.text="YES"+smt
            }
        }catch (e:Exception)
        {
            tv_dev_release_key.text=e.toString()
            Log.e("name not found",e.toString())
        }
    }
}