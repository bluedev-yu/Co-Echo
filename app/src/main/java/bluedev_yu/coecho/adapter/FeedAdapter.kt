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


class FeedAdapter(val feedList: ArrayList<Feeds>) : RecyclerView.Adapter<FeedAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        //holder.profileImgUrl.setImageResource(feedList.get(position).profileImgUrl)
        //holder.userId.text = feedList.get(position).userId
        holder.content.text = feedList.get(position).content
        holder.hashtag.text = feedList.get(position).hashtag
        //holder.feedImgUrl.setImageResource(feedList.get(position).feedImgUrl)
        holder.likeCnt.text = feedList.get(position).likeCnt.toString()
        holder.commentCnt.text = feedList.get(position).commentCnt.toString()
        holder.feedCardView.setOnClickListener {
            val intent = Intent(holder.itemView?.context, FeedDetail::class.java)
            ContextCompat.startActivity(holder.itemView?.context, intent, null)
        }
    }


    override fun getItemCount(): Int {
        return feedList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //var profileImgUrl = itemView.findViewById<ImageView>(R.id.iv_profile) //프로필 이미지
        //var userId = itemView.findViewById<TextView>(R.id.tv_name) //이름
        var content = itemView.findViewById<TextView>(R.id.tv_content) //피드 글
        var hashtag = itemView.findViewById<TextView>(R.id.tv_hashtag) //해시태그
        //var feedImgUrl = itemView.findViewById<ImageView>(R.id.iv_image) //피드 이미지
        var likeCnt = itemView.findViewById<TextView>(R.id.tv_like) //좋아요 수
        var commentCnt = itemView.findViewById<TextView>(R.id.tv_comment) //댓글 수

        var feedCardView = itemView.findViewById<CardView>(R.id.feed_cardview) //피드 카드뷰
    }


}
