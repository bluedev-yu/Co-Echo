package bluedev_yu.coecho.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.MapPlacesAdapter
import bluedev_yu.coecho.data.model.Place
import bluedev_yu.coecho.databinding.FragmentMapPlacesBinding


class FragmentMapPlaces : Fragment() {

    lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentMapPlacesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapPlacesBinding.inflate(layoutInflater)
        val view = binding.root

        val placeList = arrayListOf(
            Place(null, "장소1", "1", "1", 1, "1", "1", "1", 1.0, 1.0),
            Place(null, "장소1", "1", "1", 1, "1", "1", "1", 1.0, 1.0),
            Place(null, "장소1", "1", "1", 1, "1", "1", "1", 1.0, 1.0),
            Place(null, "장소1", "1", "1", 1, "1", "1", "1", 1.0, 1.0)
        )

        recyclerView = binding.MapPlacesRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = MapPlacesAdapter(placeList)
        return view
    }

    private fun newInstant(): FragmentMapPlaces {
        val bundle = Bundle()
        val frag = FragmentMapPlaces()
        frag.arguments = bundle
        return frag
    }

}