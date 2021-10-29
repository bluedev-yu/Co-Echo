package bluedev_yu.coecho

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.HashMap

class DB_Review {
    val db = Firebase.firestore
    suspend fun getHashtag(pid:String):Pair<String,String>?
    {
        val a=db.collection("Reviews").whereEqualTo("pid",pid).get()
        a.await()
        if(a.result.isEmpty)
            return null
        else
        {
            var map= HashMap<String,Int>()
            var doc=a.result.documents
            for(i in 0 until doc.size)
            {
                var a:String=doc[i].get("hashtag").toString()
                if(a=="")
                    continue
                if(map.contains(a))
                {
                    map.replace(a,map.get(a)!!+1)
                }
                else
                {
                    map.set(a,1)
                }
            }
            var res = map.toList().sortedBy{it.second}
            var a=""
            var b=""
            if(res.size>=2)
            {
                a=res.get(0).first
                b=res.get(1).first
            }
            else if(res.size==1)
            {
                a=res.get(0).first
            }
            Log.i("해시태그 검색 결과",a+" "+b)
            return Pair<String,String>(a,b)
        }
    }
}