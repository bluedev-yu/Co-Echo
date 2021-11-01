package bluedev_yu.coecho

import android.util.Log
import bluedev_yu.coecho.data.model.ReviewDTO
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.HashMap

class DB_Review {
    val db = Firebase.firestore
    suspend fun getHashtag(pid: String): Pair<String, String>? {
        val a = db.collection("Reviews").whereEqualTo("pid", pid).get()
        a.await()
        if (a.result.isEmpty)
            return null
        else {
            var map = HashMap<String, Int>()
            var doc = a.result.documents
            for (i in 0 until doc.size) {
                var a: String = doc[i].get("hashtag").toString()
                if (a == "")
                    continue
                if (map.contains(a)) {
                    map.replace(a, map.get(a)!! + 1)
                } else {
                    map.set(a, 1)
                }
            }
            var res = map.toList().sortedBy { it.second }
            var a = ""
            var b = ""
            if (res.size >= 2) {
                a = res.get(0).first
                b = res.get(1).first
            } else if (res.size == 1) {
                a = res.get(0).first
            }
            Log.i("해시태그 검색 결과", a + " " + b)
            return Pair<String, String>(a, b)
        }
    }

    suspend fun getReviewMeta(placeName: String, placeAddress: String): Pair<Int, Float> {
        var cnt: Int = 0
        var starRes: Float = 0.0F
        CoroutineScope(Dispatchers.Default).launch {
            val pid = DB_Place().search_data(placeName, placeAddress)
            Log.d("메타 pid ", pid)
            if (pid != "none" && pid != "false") {
                val a = db.collection("Reviews").whereEqualTo("pid", pid).get()
                a.await()
                cnt = a.result.size()
                for (doc in a.result.documents) {
                    starRes += doc.data!!.get("star").toString().toFloat()
                }
            }
        }.join()
        if (cnt == 0) {
            return Pair(0, 0.0F)
        }
        Log.d("메타 cnt ", cnt.toString() + " " + starRes.toString())
        return Pair(cnt, starRes / cnt.toFloat())
    }

}