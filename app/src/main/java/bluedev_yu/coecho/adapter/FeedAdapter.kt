package bluedev_yu.coecho.adapter

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.FeedDetail
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.Fragment.FragmentMyPage
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth


class FeedAdapter(val feedList: ArrayList<Feeds>, val userList: ArrayList<userDTO>) : RecyclerView.Adapter<FeedAdapter.CustomViewHolder>(){

    var auth : FirebaseAuth? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        var iv_feed_share: ImageView
        iv_feed_share = view.findViewById(R.id.iv_feed_share)
        iv_feed_share.setOnClickListener {
            //하단 드로어
            val bottomSheetDialog = BottomSheetDialog(
                parent.context, R.style.BottomSheetDialogTheme)

            val bottomSheetView = LayoutInflater.from(parent.context).inflate(
                R.layout.layout_bottom_sheet, parent.findViewById(R.id.bottomSheet) as LinearLayout?
            )

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()
        }

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        auth = FirebaseAuth.getInstance()
        var feeduid = feedList.get(position).uid

        var userIterator = userList.iterator() //해당 유저 정보 찾기
        while(userIterator.hasNext())
        {
            var imsi = userIterator.next()
            Log.v("imsi",imsi.toString())
            if(imsi.uid.equals(feeduid)) //해당 유저
            {
                holder.strName.text = imsi.strName //유저 이름
                Glide.with(holder.itemView.context).load(imsi.imageUrl!!.toUri()).apply( 
                    RequestOptions().circleCrop()).into(holder.profileImgUrl) //유저 프로필 이미지
                when(imsi.title) //칭호
                {
                    0 -> holder.userTitle.setText(R.string.grade1)
                    1 -> holder.userTitle.setText(R.string.grade2)
                }
                break
            }
        }


        holder.strName.setOnClickListener(object: View.OnClickListener{
            //해당 유저의 마이페이지를 띄우기
            override fun onClick(v: View?) {
                var fragmentUserPage = FragmentMyPage()
                var bundle = Bundle()
                bundle.putString("uid", feeduid)
                fragmentUserPage.arguments = bundle

                val activity = v!!.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.snsLayout, fragmentUserPage)
                    .addToBackStack(null)
                    .commit()
            }
        })

        holder.timeStamp.text = feedList.get(position).timeStamp.toString()
        //애용.... timestamp 부탁해......

        holder.content.text = feedList.get(position).content
        holder.hashtag.text = feedList.get(position).hashtag
        //holder.feedImgUrl.setImageResource(feedList.get(position).feedImgUrl)
        holder.likeCnt.text = feedList.get(position).likeCnt.toString()
        holder.commentCnt.text = feedList.get(position).commentCnt.toString()
        holder.feedCardView.setOnClickListener {
            val intent = Intent(holder.itemView?.context, FeedDetail::class.java)
            ContextCompat.startActivity(holder.itemView?.context, intent, null)
        }
        holder.ivLike.setOnClickListener { 
            holder.ivLike.setImageResource(R.drawable.like)
            //좋아요수 + 1 하고 데이터에 넘기기
        }
    }

    override fun getItemCount(): Int {
        return feedList.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var profileImgUrl = itemView.findViewById<ImageView>(R.id.iv_profile) //프로필 이미지
        var userTitle = itemView.findViewById<TextView>(R.id.tv_user_title) //칭호
        var strName = itemView.findViewById<TextView>(R.id.tv_name) //이름
        var timeStamp = itemView.findViewById<TextView>(R.id.tv_timestamp) //타임스탬프
        var content = itemView.findViewById<TextView>(R.id.tv_content) //피드 글
        var hashtag = itemView.findViewById<TextView>(R.id.tv_hashtag) //해시태그
        //var feedImgUrl = itemView.findViewById<ImageView>(R.id.iv_image) //피드 이미지
        var likeCnt = itemView.findViewById<TextView>(R.id.tv_like) //좋아요 수
        var commentCnt = itemView.findViewById<TextView>(R.id.tv_comment) //댓글 수
        var feedCardView = itemView.findViewById<CardView>(R.id.feed_cardview) //피드 카드뷰
        var ivLike = itemView.findViewById<ImageView>(R.id.iv_like) //좋아요 하트


    }
}
