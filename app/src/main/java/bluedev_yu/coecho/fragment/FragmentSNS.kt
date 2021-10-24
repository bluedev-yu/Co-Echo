package bluedev_yu.coecho.fragment

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import bluedev_yu.coecho.Fragment.FragmentFeeds
import bluedev_yu.coecho.adapter.FeedAdapter
import bluedev_yu.coecho.data.model.Feeds
import bluedev_yu.coecho.R
import bluedev_yu.coecho.UploadFeed
import bluedev_yu.coecho.data.model.FollowDTO
import bluedev_yu.coecho.databinding.FragmentFeedsBinding
import bluedev_yu.coecho.databinding.FragmentSnsBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FragmentSNS : Fragment() {
import androidx.fragment.app.Fragment
import bluedev_yu.coecho.R
import bluedev_yu.coecho.databinding.FragmentSnsBinding


class FragmentSNS : Fragment() {
    private lateinit var binding: FragmentSnsBinding

    lateinit var svSNS: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSnsBinding.inflate(layoutInflater)
        val view = binding.root

        val transaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.layout_child, FragmentFeeds())
        transaction.disallowAddToBackStack()
        transaction.commit()


        //searchview 리스너
        svSNS = binding.svSns
//        val id: Int = svSNS.getContext().getResources()
//            .getIdentifier("android:id/search_src_text", null, null)
//        val editText: EditText = svSNS.findViewById(id)
//        editText.setOnClickListener {
//            val transaction = childFragmentManager.beginTransaction()
//            transaction.replace(R.id.layout_child, FragmentSearchResults())
//            transaction.disallowAddToBackStack()
//            transaction.commit()
//        }

        svSNS.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //검색 버튼 눌러졌을 때 이벤트 처리 ->
                //검색 결과 페이지로 이동
                Toast.makeText(requireContext(), query, Toast.LENGTH_SHORT).show()

                val transaction = childFragmentManager.beginTransaction()
                transaction.replace(R.id.layout_child, FragmentSearchResults())
                transaction.disallowAddToBackStack()
                transaction.commit()

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //검색어 변경되었을 때 이벤트 처리
//                Toast.makeText(requireContext(), newText, Toast.LENGTH_SHORT).show()
                return false
            }

        })

        return view
    }

}