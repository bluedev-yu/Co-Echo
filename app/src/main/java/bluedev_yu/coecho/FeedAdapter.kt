package bluedev_yu.coecho

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeedAdapter(val feedList: ArrayList<Feeds>) : RecyclerView.Adapter<FeedAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedAdapter.CustomViewHolder, position: Int) {
        holder.gender.setImageResource(feedList.get(position).gender)
        holder.name.text = feedList.get(position).name
        holder.text.text = feedList.get(position).text
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val gender = itemView.findViewById<ImageView>(R.id.iv_profile) //성별
        val name = itemView.findViewById<TextView>(R.id.tv_name) //이름
        val text = itemView.findViewById<TextView>(R.id.tv_text) //피드 글
    }

}
