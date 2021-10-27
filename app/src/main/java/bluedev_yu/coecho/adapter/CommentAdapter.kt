package bluedev_yu.coecho.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.Comments
import bluedev_yu.coecho.fragment.FragmentMyPage
import com.google.firebase.auth.FirebaseAuth

class CommentAdapter(val commentList: ArrayList<Comments>): RecyclerView.Adapter<CommentAdapter.CustomViewHolder>(){

    var auth : FirebaseAuth? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_comment, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CustomViewHolder, position: Int) {
        auth = FirebaseAuth.getInstance()

        holder.userId.text = commentList.get(position).strName
        holder.userId.setOnClickListener (object: View.OnClickListener{
            //해당 유저의 마이페이지를 띄우기
            override fun onClick(v: View?) {
                var fragmentUserPage = FragmentMyPage()
                var bundle = Bundle()
                bundle.putString("uid", auth?.uid.toString())
                fragmentUserPage.arguments = bundle

                val activity = v!!.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.layout_feed_detail, fragmentUserPage)
                    .addToBackStack(null)
                    .commit()
            }
        })
        holder.comment.text = commentList.get(position).comment
    }

    override fun getItemCount(): Int {
return commentList.size
    }

    class CustomViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        //var profileImgUrl: String? = null,
        var userId = itemView.findViewById<TextView>(R.id.tv_commentUserId) //댓글단 사람 아이디
        var comment = itemView.findViewById<TextView>(R.id.tv_commentText) //댓글 내용
    }

}