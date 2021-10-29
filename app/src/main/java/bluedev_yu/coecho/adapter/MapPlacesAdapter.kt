package bluedev_yu.coecho.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.Place
import bluedev_yu.coecho.fragment.FragmentMapDetail
import bluedev_yu.coecho.fragment.FragmentMapHashtag
import bluedev_yu.coecho.fragment.PlaceDetailFragment
import kotlinx.coroutines.NonDisposableHandle.parent

class MapPlacesAdapter(val placeList: ArrayList<Place>) :
    RecyclerView.Adapter<MapPlacesAdapter.MapViewHolder>() {

    class MapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.tv_PlaceName)
        val layout=itemView.findViewById<ConstraintLayout>(R.id.search_place_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_search_places, parent, false)
        return MapViewHolder(view).apply {
            textView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    Toast.makeText(parent.context, "장소클릭됨", Toast.LENGTH_SHORT).show()
                    val activity = v!!.context as AppCompatActivity
                    activity.supportFragmentManager.beginTransaction()
                        .replace(R.id.MapDrawerLayout, FragmentMapDetail())
                        .addToBackStack(null)
                        .commit()

                }


            })
//            textView.setOnClickListener {
//                Toast.makeText(parent.context, "장소클릭됨", Toast.LENGTH_SHORT).show()
//                val activity = view.context as AppCompatActivity
//                activity.supportFragmentManager.beginTransaction()
//                    .replace(R.id.MapDrawerLayout, FragmentMapDetail())
//                    .addToBackStack(null)
//                    .commit()
//            }
        }
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {

        holder.textView.text = placeList.get(position).placeName
        holder.layout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var fragmentPlaceDetail = PlaceDetailFragment()
                val bundle = Bundle()
                var temp = placeList.get(position)
                bundle.putString("name", temp.placeName)
                bundle.putString("address", temp.placeAdress)
                bundle.putString("category", temp.placeCategory)
                bundle.putString("phone", temp.placePhone)
                bundle.putString("url", temp.placeURL)
                bundle.putDouble("x", temp.placeX)
                bundle.putDouble("y", temp.placeY)
                fragmentPlaceDetail.arguments = bundle
                val activity = v!!.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.frameLayout, fragmentPlaceDetail)
                    .addToBackStack(null)
                    .commit()
            }

        })

//////        holder.textView.setOnClickListener(object: View.OnClickListener{
//////            override fun onClick(v: View?) {
//////                val activity = v!!.context as AppCompatActivity
//////                activity.supportFragmentManager.beginTransaction()
//////                    .replace(R.id.MapDrawerLayout, FragmentMapDetail())
//////                    .addToBackStack(null)
//////                    .commit()
////
////
////            }
//        })

    }

    override fun getItemCount(): Int {
        return placeList.size
    }

}