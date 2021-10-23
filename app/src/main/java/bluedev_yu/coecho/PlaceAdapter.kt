package bluedev_yu.coecho

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaceAdapter (val placeList: ArrayList<Place>): RecyclerView.Adapter<PlaceAdapter.CustomViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate((R.layout.list_place), parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceAdapter.CustomViewHolder, position: Int) {
        if(placeList.get(position).placeHashtag!=null)
        {
            holder.placeHashtag.text = placeList.get(position).placeHashtag
        }
        holder.placeName.text = placeList.get(position).placeName
        holder.placeCategory.text = placeList.get(position).placeCategory
        holder.placeReviewCnt.text = placeList.get(position).placeReviewCnt.toString()
        holder.placeLocation.text = placeList.get(position).placeAdress
        holder.placeDistanceFromMyLocation.text = placeList.get(position).placeDistanceFromMyLocation
    }

    override fun getItemCount(): Int {
        return placeList.size
    }

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var placeHashtag = itemView.findViewById<TextView>(R.id.tv_placeHashtag) //장소 해시태그
        var placeName = itemView.findViewById<TextView>(R.id.tv_placeName) //장소 이름
        var placeCategory = itemView.findViewById<TextView>(R.id.tv_placeCategory) //장소 카테고리
        var placeReviewCnt = itemView.findViewById<TextView>(R.id.tv_placeReviewCnt) //장소 리뷰 개수
        var placeLocation = itemView.findViewById<TextView>(R.id.tv_placeLocation) //장소 주소
        var placeDistanceFromMyLocation = itemView.findViewById<TextView>(R.id.tv_placeDistanceFromMyLocation) //내 위치로부터 거리
    }
}