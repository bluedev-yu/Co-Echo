package bluedev_yu.coecho.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import bluedev_yu.coecho.R
import bluedev_yu.coecho.SNSFragementAdapter
import bluedev_yu.coecho.adapter.FragmentAdapter
import bluedev_yu.coecho.databinding.FragmentMapSearchBinding
import com.google.android.material.tabs.TabLayout


class fragmentMapSearch : Fragment() {

    private lateinit var binding: FragmentMapSearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapSearchBinding.inflate(layoutInflater)
        val view = binding.root

        val fragmentManager = (activity as FragmentActivity).supportFragmentManager

        val pagerAdapter = SNSFragementAdapter(fragmentManager)
        val pager = binding.MapViewPager
        pager.adapter =

        return inflater.inflate(R.layout.fragment_map_search, container, false)

    }


}