package bluedev_yu.coecho.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import bluedev_yu.coecho.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragmentSubscriber.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragmentSubscriber : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_subscriber, container, false)
    }

    private fun newInstant() : fragmentSubscriber
    {
        val args = Bundle()
        val frag = fragmentSubscriber()
        frag.arguments = args
        return frag
    }
}