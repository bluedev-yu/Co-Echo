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
import android.widget.Toast
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage



class FeedAdapter(val feedList: ArrayList<Feeds>, val contentUidList : ArrayList<String>) : 
RecyclerView.Adapter<FeedAdapter.CustomViewHolder>(){

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore?= null //String 등 자료형 데이터베이스
    var firestorage : FirebaseStorage?= null //사진, GIF 등의 파일 데이터베이스

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        var iv_feed_share: ImageView

//        iv_feed_share = view.findViewById(R.id.feed_share)
//        iv_feed_share.setOnClickListener {
//            //하단 드로어
//            val bottomSheetDialog = BottomSheetDialog(
//                parent.context, R.style.BottomSheetDialogTheme
//            )
//
//            val bottomSheetView = LayoutInflater.from(parent.context).inflate(
//                R.layout.layout_bottom_sheet, parent.findViewById(R.id.bottomSheet) as LinearLayout?
//            )
//
//            bottomSheetDialog.setContentView(bottomSheetView)
//            bottomSheetDialog.show()
//        }

        return CustomViewHolder(view).apply {
            feedCardView.setOnClickListener {
                val curPos: Int = adapterPosition
                val feed: Feeds = feedList.get(curPos)

                val intent = Intent(parent.context, FeedDetail::class.java)
                intent.putExtra("content", feed.content)
                ContextCompat.startActivity(parent.context, intent, null)

            }
        }
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        var feeduid = feedList.get(position).uid

        holder.strName.text = feedList.get(position).strName //유저 이름
        Glide.with(holder.itemView.context).load(feedList.get(position).imageUrl!!.toUri()).apply(
            RequestOptions().circleCrop()).into(holder.profileImgUrl) //유저 프로필 이미지
        when(feedList.get(position).title) //칭호
        {
            0 -> holder.userTitle.setText(R.string.grade1)
            1 -> holder.userTitle.setText(R.string.grade2)
        }

        //holder.profileImgUrl.setImageResource(feedList.get(position).profileImgUrl)
        holder.strName.text = userList.get(position).strName
        holder.strName.setOnClickListener(object : View.OnClickListener {
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
            intent.putExtra("name", feedList.get(position).uid)
            intent.putExtra("content", feedList.get(position).content)
            intent.putExtra("hashtag", feedList.get(position).hashtag)
            intent.putExtra("likeCnt", feedList.get(position).likeCnt)
            ContextCompat.startActivity(holder.itemView?.context, intent, null)
        }

        //미리 하트가 비었는가 찼는가
        if(feedList[position].likes.containsKey(auth?.uid.toString())) //좋아요 눌렀을 경우
        {
            holder.ivLike.setImageResource(R.drawable.like)
        }
        else
        {
            holder.ivLike.setImageResource(R.drawable.blank_like) //안눌렀을 경우
        }

        holder.isLikeClicked.setOnClickListener {
            likeEvent(position)
        }
    }

    private fun likeEvent(position: Int)
    {
        var tsDoc = firestore?.collection("Feeds")?.document(contentUidList[position])
        firestore?.runTransaction{
            transaction ->

            val uid = auth?.uid.toString()
            val feedDTO = transaction.get(tsDoc!!).toObject(Feeds::class.java)

            if(feedDTO!!.likes.containsKey(uid)){ //이미 좋아요 한 경우 -> 좋아요 철회
                feedDTO?.likeCnt = feedDTO.likeCnt-1
                feedDTO?.likes.remove(uid)
            }
            else //좋아요 아직 안함 -> 좋아요 하기
            {
                feedDTO.likes[uid] = uid
                feedDTO.likeCnt = feedDTO.likeCnt+1
            }

            transaction.set(tsDoc,feedDTO)
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
        var isLikeClicked = itemView.findViewById<ImageView>(R.id.iv_like) //좋아요 하트
        var likeCnt = itemView.findViewById<TextView>(R.id.tv_like) //좋아요 수
        var commentCnt = itemView.findViewById<TextView>(R.id.tv_comment) //댓글 수
        var feedCardView = itemView.findViewById<CardView>(R.id.feed_cardview) //피드 카드뷰
        var ivLike = itemView.findViewById<ImageView>(R.id.iv_like) //좋아요 하트


    }
}
