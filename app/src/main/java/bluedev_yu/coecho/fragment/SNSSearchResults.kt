package bluedev_yu.coecho.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import bluedev_yu.coecho.SNSFragementAdapter
import bluedev_yu.coecho.adapter.FragmentAdapter
import bluedev_yu.coecho.databinding.FragmentSnsSearchResultsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


/**
 * A simple [Fragment] subclass.
 * Use the [SNSSearchResults.newInstance] factory method to
 * create an instance of this fragment.
 */
class SNSSearchResults : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentSnsSearchResultsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSnsSearchResultsBinding.inflate(layoutInflater)
        val view = binding.root

        val fragmentManager = (activity as FragmentActivity).supportFragmentManager

        val pagerAdapter = SNSFragementAdapter(fragmentManager)
        val pager = binding.SNSViewPager
        pager.adapter = pagerAdapter
        val tab = binding.SNSTab
        tab.setupWithViewPager(pager)

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SNSSearchResults.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SNSSearchResults().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
