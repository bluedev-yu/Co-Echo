package bluedev_yu.coecho.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.data.model.userDTO
import bluedev_yu.coecho.Fragment.FragmentMyPage
import com.google.firebase.auth.FirebaseAuth

class SearchPeopleAdapter(val userlist: ArrayList<userDTO>) : RecyclerView.Adapter<SearchPeopleAdapter.CustomViewHolder>(){

    var auth : FirebaseAuth? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_search_user, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

        auth = FirebaseAuth.getInstance()

        //holder.profileImgUrl.setImageResource(feedList.get(position).profileImgUrl)
        holder.strName.text = userlist.get(position).strName
        holder.strName.setOnClickListener(object: View.OnClickListener{
            //해당 유저의 마이페이지를 띄우기
            override fun onClick(v: View?) {
                var fragmentUserPage = FragmentMyPage()
                var bundle = Bundle()
                bundle.putString("uid", auth?.uid.toString())
                fragmentUserPage.arguments = bundle

                val activity = v!!.context as AppCompatActivity
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.snsLayout, fragmentUserPage)
                    .addToBackStack(null)
                    .commit()
            }
        })
    }


    override fun getItemCount(): Int {
        return userlist.size
    }

    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        //var profileImgUrl = itemView.findViewById<ImageView>(R.id.iv_profile) //프로필 이미지
        var strName = itemView.findViewById<TextView>(R.id.tv_search_name) //이름
    }


}
