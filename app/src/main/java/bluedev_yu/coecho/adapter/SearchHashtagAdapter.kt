package bluedev_yu.coecho.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.Feeds

class SearchHashtagAdapter(val hashtagList: ArrayList<Feeds>): RecyclerView.Adapter<SearchHashtagAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_search_hashtag, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {
                val curPos: Int = adapterPosition
                val feed: Feeds = hashtagList.get(curPos)
                Toast.makeText(parent.context, "해시태그 : ${feed.hashtag}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.hashtag.text = hashtagList.get(position).hashtag
    }


    override fun getItemCount(): Int {
        return hashtagList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var hashtag = itemView.findViewById<TextView>(R.id.tv_search_hashtag) //해시태그
    }


}