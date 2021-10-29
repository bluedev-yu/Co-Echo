package bluedev_yu.coecho.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.MAPFragmentAdapter
import bluedev_yu.coecho.databinding.FragmentMapSearchBinding

class FragmentMapSearch : Fragment() {

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

        val MapFragmentManager = (activity as FragmentActivity).supportFragmentManager

        val MapPagerAdapter = MAPFragmentAdapter(childFragmentManager)
        val MapPager = binding.MapViewPager
        MapPager.adapter = MapPagerAdapter
        val MapTab = binding.MapTabs
        MapTab.setupWithViewPager(MapPager)

        return view
    }

}