package bluedev_yu.coecho.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.Comments

class CommentAdapter(val commentList: ArrayList<Comments>): RecyclerView.Adapter<CommentAdapter.CustomViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_comment, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CustomViewHolder, position: Int) {
        holder.userId.text = commentList.get(position).userId
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