package bluedev_yu.coecho.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.FeedDetail
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.Feeds

class SearchPeopleAdapter(val userlist: ArrayList<Feeds>) : RecyclerView.Adapter<SearchPeopleAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_search_user, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        //holder.profileImgUrl.setImageResource(feedList.get(position).profileImgUrl)
        holder.userId.text = userlist.get(position).userId
    }


    override fun getItemCount(): Int {
        return userlist.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //var profileImgUrl = itemView.findViewById<ImageView>(R.id.iv_profile) //프로필 이미지
        var userId = itemView.findViewById<TextView>(R.id.tv_search_name) //이름
    }


}