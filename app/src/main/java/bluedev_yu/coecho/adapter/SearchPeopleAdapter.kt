package bluedev_yu.coecho.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.fragment.FragmentMyPage
import bluedev_yu.coecho.data.model.FollowDTO
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SearchPeopleAdapter(val userlist: ArrayList<userDTO>) : RecyclerView.Adapter<SearchPeopleAdapter.CustomViewHolder>(){

    var auth : FirebaseAuth? = null
    var firestore : FirebaseFirestore? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_search_user, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        //닉네임
        holder.strName.text = userlist.get(position).strName

        when(userlist.get(position).title) //칭호
        {
            0 -> holder.title.setText(R.string.grade1)
            1 -> holder.title.setText(R.string.grade2)
        }
        //프로필사진
        if(userlist.get(position).imageUrl == null) //기본 이미지
            Glide.with(holder.itemView.context).load(R.drawable.default_profilephoto).apply(
                RequestOptions().circleCrop()).into(holder.profileImgUrl) //유저 프로필 이미지
        else
        {
            Glide.with(holder.itemView.context).load(userlist.get(position).imageUrl!!.toUri()).apply(
                RequestOptions().circleCrop()).into(holder.profileImgUrl) //유저 프로필 이미지
        }
        //검색한 사람을 팔로우하고 있는가? -> 팔로우버튼 텍스트
        firestore?.collection("Follow")?.document(auth?.uid.toString())?.addSnapshotListener{
                documentSnapshot, firebaseFirestoreException ->
            var followDTO = documentSnapshot?.toObject(FollowDTO::class.java)
            if(followDTO?.followings!!.containsKey(userlist.get(position).uid)) //follow하고 있을 경우
            {
                holder.followButton.setText("팔로우 취소")
            }
            else
            {
                holder.followButton.setText("팔로우")
            }
        }

        var uid = userlist.get(position).uid

        holder.profileImgUrl.setOnClickListener(object: View.OnClickListener{
            //해당 유저의 마이페이지를 띄우기
            override fun onClick(v: View?) {
                var fragmentUserPage = FragmentMyPage()
                var bundle = Bundle()
                bundle.putString("uid", uid)
                fragmentUserPage.arguments = bundle

                val activity = v!!.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.snsLayout, fragmentUserPage)
                    .addToBackStack(null)
                    .commit()
            }
        })


        holder.followButton.setOnClickListener{
            var tsDocFollowing = firestore?.collection("Follow")?.document(auth?.uid.toString())
            firestore?.runTransaction { //내 팔로우 데이터 가져오기
                    transaction ->
                var followDTO = transaction.get(tsDocFollowing!!).toObject(FollowDTO::class.java)
                if (followDTO == null) //empty
                {
                    followDTO = FollowDTO()
                    followDTO.followingCount = 2
                    followDTO.followings[uid!!] = uid
                    followDTO.followings[auth?.uid.toString()] = auth?.uid.toString()
                    followDTO.followers[auth?.uid.toString()] = auth?.uid.toString()

                    if (tsDocFollowing != null) {
                        transaction.set(tsDocFollowing, followDTO)
                    }
                    return@runTransaction
                }

                if (followDTO?.followings?.containsKey(uid)) { //이미 팔로우하고 있을 경우 -> 언팔로우
                    followDTO?.followingCount = followDTO?.followingCount - 1
                    followDTO?.followings.remove(uid)
                } else //팔로우하기
                {
                    followDTO?.followingCount = followDTO?.followingCount + 1
                    followDTO?.followings[uid!!] = uid
                }

                if (tsDocFollowing != null) {
                    transaction.set(tsDocFollowing, followDTO)
                }
                return@runTransaction
            }

            var tsDocFollower = firestore?.collection("Follow")?.document(uid!!) //남의 아이디
            firestore?.runTransaction { //내 팔로우 데이터 가져오기
                    transaction ->

                var followDTO =
                    tsDocFollower?.let { transaction.get(it).toObject(FollowDTO::class.java) }
                if (followDTO == null) //empty
                {
                    followDTO = FollowDTO()
                    followDTO!!.followerCount = 2
                    followDTO!!.followers[auth?.uid.toString()!!] = auth?.uid.toString()
                    followDTO!!.followers[uid!!]=uid
                    followDTO!!.followings[uid!!]=uid

                    if (tsDocFollower != null) {
                        transaction.set(tsDocFollower, followDTO!!)
                    }
                    return@runTransaction
                }

                if (followDTO?.followers?.containsKey(auth?.uid.toString())!!) { //이미 팔로우하고 있을 경우 -> 언팔로우
                    followDTO?.followerCount = followDTO?.followerCount!! - 1
                    followDTO?.followers!!.remove(auth?.uid.toString())
                } else //팔로우하기
                {
                    followDTO?.followerCount = followDTO?.followerCount!! + 1
                    followDTO?.followers!![auth?.uid.toString()] = auth?.uid.toString()
                }

                if (tsDocFollower != null) {
                    transaction.set(tsDocFollower, followDTO!!)
                }
                return@runTransaction
            }
        }
    }


    override fun getItemCount(): Int {
        return userlist.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var profileImgUrl = itemView.findViewById<ImageView>(R.id.search_profileImage) //프로필 이미지
        var followButton = itemView.findViewById<Button>(R.id.search_followButton) //팔로우버튼
        var strName = itemView.findViewById<TextView>(R.id.tv_search_name) //이름
        var title = itemView.findViewById<TextView>(R.id.tv_title) //칭호
    }


}
