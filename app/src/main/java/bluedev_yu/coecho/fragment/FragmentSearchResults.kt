package bluedev_yu.coecho.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import bluedev_yu.coecho.SNSFragementAdapter
import bluedev_yu.coecho.databinding.FragmentSearchResultsBinding

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
        //검색 query 받아오기
        val bundle = arguments
        val query = bundle!!.getString("query")

//        Toast.makeText(requireContext(), "검색결과 : $query", Toast.LENGTH_SHORT).show()

        // Inflate the layout for this fragment
        binding = FragmentSearchResultsBinding.inflate(layoutInflater)
        val view = binding.root

        val fragmentManager = (activity as FragmentActivity).supportFragmentManager

        val pagerAdapter = SNSFragementAdapter(childFragmentManager, query)
        val pager = binding.SNSViewPager
        pager.adapter = pagerAdapter
        val tab = binding.SNSTab
        tab.setupWithViewPager(pager)

        return view
    }

}