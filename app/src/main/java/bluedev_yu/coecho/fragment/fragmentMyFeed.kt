package bluedev_yu.coecho.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bluedev_yu.coecho.R

class fragmentMyFeed : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_feed, container, false)
    }

    fun newInstant() : fragmentMyFeed
    {
        val args = Bundle()
        val frag = fragmentMyFeed()
        frag.arguments = args
        return frag
    }

}