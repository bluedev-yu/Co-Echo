package bluedev_yu.coecho

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.FirebaseOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.ktx.initialize
import net.daum.mf.map.api.MapView
import java.security.AccessController.getContext

class DB_Place {
    val db = Firebase.firestore



    fun insert_data(pid:String,place_name:String,x:String,y:String) {
        val place_input = hashMapOf(
            "pName" to place_name,
            "pHashtag" to "#친환경",
            "pCategory" to "식당",
            "longitude" to x.toDouble(),
            "latitude" to y.toDouble(),
            "star" to 0.0
        )
        db.collection("Places").document(pid)
            .set(place_input)
            .addOnSuccessListener { Log.d("Firestore DB","DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("Firestore DB", "Error writing document", e) }
    }
    fun read_data()
    {

        db.collection("Places")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "getting documents: ${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }
}