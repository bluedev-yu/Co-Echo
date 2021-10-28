package bluedev_yu.coecho.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bluedev_yu.coecho.R
import kotlinx.android.synthetic.main.fragment_place_detail.view.*

class PlaceDetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var thisFragView=inflater.inflate(R.layout.fragment_place_detail, container, false)

        thisFragView.tv_placename_placedetail.setText(arguments?.getString(("name")))
        thisFragView.tv_address_placedetail.setText(arguments?.getString("address"))
        thisFragView.tv_category_placedetail.setText(arguments?.getString("category"))
        thisFragView.tv_phone_placedetail.setText(arguments?.getString("phone"))
        thisFragView.tv_url_placedetail.setText(arguments?.getString("url"))

        thisFragView.ib_backbutton.setOnClickListener {
                
        }
        // Inflate the layout for this fragment
        return thisFragView
    }


}