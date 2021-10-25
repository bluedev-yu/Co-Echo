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


    fun insert_data(tPlace: Place) {
        if (search_data(tPlace.placeName, tPlace.placeAdress) != null) {
            val place_input = hashMapOf(
                "placeName" to tPlace.placeName,
                "pCategory" to tPlace.placeCategory,
                "longitude" to tPlace.placeX,
                "latitude" to tPlace.placeY,
                "address" to tPlace.placeAdress
            )
            db.collection("Places").document()
                .set(place_input)
                .addOnSuccessListener {
                    Log.d(
                        "Firestore DB",
                        "DocumentSnapshot successfully written!"
                    )
                }
                .addOnFailureListener { e -> Log.w("Firestore DB", "Error writing document", e) }
        }
    }

    fun read_data() {

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

    fun search_data(name: String, address: String) {
        val query = db.collection("Places").whereEqualTo("address", address)

        query.whereEqualTo("pName", name).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d(TAG, "searching document: ${document.id} => ${document.data}")

                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error searching documents.", exception)
            }

    }
}