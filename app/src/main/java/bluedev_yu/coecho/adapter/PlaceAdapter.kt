package bluedev_yu.coecho.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.DB_Place
import bluedev_yu.coecho.DB_Review
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.Place
import bluedev_yu.coecho.fragment.PlaceDetailFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaceAdapter(val placeList: ArrayList<Place>) :
    RecyclerView.Adapter<PlaceAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate((R.layout.list_place), parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        var tempRes: Pair<String, String>?
        if (placeList.get(position).placeHashtag == null) {
            CoroutineScope(Dispatchers.Main).launch {
                var pid = DB_Place().search_data(
                    placeList.get(position).placeName,
                    placeList.get(position).placeAdress
                )
                if (pid != "false" && pid != "none") {
                    tempRes = DB_Review().getHashtag(pid)
                    if (tempRes != null) {
                        if (tempRes!!.first != "") {
                            Log.i("비져빌리티","a"+placeList.get(position).placeName+tempRes)
                            holder.placeHashtag.visibility = View.VISIBLE
                            holder.placeHashtag.text = tempRes!!.first
                        }
                    } else {
                        Log.i("비져빌리티","b"+placeList.get(position).placeName+tempRes)
                        holder.placeHashtag.visibility = View.INVISIBLE
                    }
                } else {
                    Log.i("비져빌리티","c"+placeList.get(position).placeName)
                    holder.placeHashtag.visibility = View.INVISIBLE
                }
            }
        }
        holder.placeName.text = placeList.get(position).placeName
        holder.placeCategory.text = placeList.get(position).placeCategory
        holder.placeReviewCnt.text = placeList.get(position).placeReviewCnt.toString()
        holder.placeLocation.text = placeList.get(position).placeAdress
        if (placeList.get(position).placeDistanceFromMyLocation != "") {
            holder.placeDistanceFromMyLocation.text =
                placeList.get(position).placeDistanceFromMyLocation + "m"
        }
        holder.placeCardView.setOnClickListener(object : View.OnClickListener {
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
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var placeCardView = itemView.findViewById<CardView>(R.id.place_cardview) //장소 카드뷰
        var placeHashtag = itemView.findViewById<TextView>(R.id.tv_placeHashtag) //장소 해시태그
        var placeName = itemView.findViewById<TextView>(R.id.tv_placeName) //장소 이름
        var placeCategory = itemView.findViewById<TextView>(R.id.tv_placeCategory) //장소 카테고리
        var placeReviewCnt = itemView.findViewById<TextView>(R.id.tv_placeReviewCnt) //장소 리뷰 개수
        var placeLocation = itemView.findViewById<TextView>(R.id.tv_placeLocation) //장소 주소
        var placeDistanceFromMyLocation =
            itemView.findViewById<TextView>(R.id.tv_placeDistanceFromMyLocation) //내 위치로부터 거리
    }
}