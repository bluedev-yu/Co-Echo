package bluedev_yu.coecho.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import bluedev_yu.coecho.R
import bluedev_yu.coecho.SNSFragementAdapter
import bluedev_yu.coecho.databinding.FragmentSearchResultsBinding
import com.google.android.material.tabs.TabLayout

class FragmentSearchResults : Fragment() {

    private lateinit var binding: FragmentSearchResultsBinding
    private lateinit var fragmentResultHashtag: FragmentResultHashtag
    private lateinit var fragmentResultPeople: FragmentResultPeople


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchResultsBinding.inflate(layoutInflater)
        val view = binding.root

        val fragmentManager = (activity as FragmentActivity).supportFragmentManager

        val pagerAdapter = SNSFragementAdapter(childFragmentManager)
        val pager = binding.SNSViewPager
        pager.adapter = pagerAdapter
        val tab = binding.SNSTab
        tab.setupWithViewPager(pager)

        return view
    }

}