package bluedev_yu.coecho

import android.content.ContentValues.TAG
import android.util.Log
import bluedev_yu.coecho.data.model.Place
import com.google.firebase.FirebaseException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class DB_Place {
    var db : FirebaseFirestore?=null


    suspend fun insert_data(tPlace: Place): Boolean {
        db= FirebaseFirestore.getInstance()
        try {
            CoroutineScope(Dispatchers.Main).launch {
                if (search_data(tPlace.placeName, tPlace.placeAdress) != "false") {
                    val place_input = hashMapOf(
                        "placeName" to tPlace.placeName,
                        "pCategory" to tPlace.placeCategory,
                        "longitude" to tPlace.placeX,
                        "latitude" to tPlace.placeY,
                        "address" to tPlace.placeAdress
                    )

                    val a = db!!.collection("Places").document()
                        .set(place_input)
                    a.await()
                }
            }
            return true
        } catch (e: FirebaseException) {
            Log.e("error:", "error:" + e.message.toString())
            return false
        }
    }

    fun read_data() {
        db= FirebaseFirestore.getInstance()
        db!!.collection("Places")
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

    suspend fun search_data(name: String, address: String): String {
        db= FirebaseFirestore.getInstance()
        val query = db!!.collection("Places").whereEqualTo("address", address)
        try {
            var a = query.whereEqualTo("placeName", name).get()
            a.await()
            if (a.result.isEmpty)
                return "none"
            else {
                Log.i("서치데이터", a.result.documents.size.toString() + a.result.documents[0].id)
                return a.result.documents[0].id
            }
        } catch (e: FirebaseException) {
            Log.e("error:", "error:" + e.message.toString())
            return "false"
        }
    }
    suspend fun placeAtrribute(pid:String):String
    {
        var searchJob= FirebaseFirestore.getInstance().collection("Places").document(pid).get()
        searchJob.await()
        return searchJob.result.data?.get("placeName").toString()
    }
}