package bluedev_yu.coecho

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_layout.view.*

class MyAdapter(): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    var titles = arrayOf("one", "two", "three", "four", "five")
    var details = arrayOf("Item one", "Item two", "Item three", "Item four", "Item five")

    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        public var itemtitle: TextView = itemview.name
        public var itemdetail: TextView = itemview.location
        public var star1: ImageView = itemview.star1
        public var star2: ImageView = itemview.star2
        public var star3: ImageView = itemview.star3
        public var star4: ImageView = itemview.star4
    }

    // viewHolder 객체를 생성하고 반환합니다. 뷰는 XML 파일을 inflate하여 생성합니다.
    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): MyViewHolder {
        var v: View = LayoutInflater.from(viewgroup.context).inflate(R.layout.card_layout, viewgroup, false)

        return MyViewHolder(v)
    }

    //ViewHolder 객체와 리스트 항목의 위치를 나타내는 정수 값을 받아서  RecyclerView에 반환합니다.
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemtitle.setText(titles.get(position))
        holder.itemdetail.setText(details.get(position))
    }

    //현재 리스트에 보여줄 항목의 개수를 리턴합니다.
    override fun getItemCount(): Int {
        return titles.size
    }
}