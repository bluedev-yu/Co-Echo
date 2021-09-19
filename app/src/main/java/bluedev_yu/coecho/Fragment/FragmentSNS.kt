package bluedev_yu.coecho.fragment

import android.app.ActionBar
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bluedev_yu.coecho.R
import bluedev_yu.coecho.databinding.FragmentSnsBinding

class FragmentSNS : Fragment() {

    private var _binding: FragmentSnsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSnsBinding.inflate(inflater, container, false)
        //val view = binding.root
        return inflater.inflate(R.layout.fragment_sns, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}