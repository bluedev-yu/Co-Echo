package bluedev_yu.coecho.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.Place
import bluedev_yu.coecho.R
import bluedev_yu.coecho.fragment.FragmentMapDetail
import bluedev_yu.coecho.fragment.FragmentMapHashtag
import kotlinx.coroutines.NonDisposableHandle.parent

class MapPlacesAdapter(val placeList: ArrayList<Place>) :
    RecyclerView.Adapter<MapPlacesAdapter.MapViewHolder>() {

    class MapViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView = itemView.findViewById<TextView>(R.id.tv_PlaceName)
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

    private fun newInstant(): FragmentMapHashtag {
        val bundle = Bundle()
        val frag = FragmentMapHashtag()
        frag.arguments = bundle
        return frag
    }
}