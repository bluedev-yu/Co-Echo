package bluedev_yu.coecho.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import bluedev_yu.coecho.KAKAO_Place
import bluedev_yu.coecho.R
import bluedev_yu.coecho.adapter.MapPlacesAdapter
import bluedev_yu.coecho.data.model.Place
import bluedev_yu.coecho.databinding.FragmentMapPlacesBinding
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.daum.mf.map.api.MapView
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory


class FragmentMapPlaces(query: String?) : Fragment() {

    var query: String? = query
    lateinit var recyclerView: RecyclerView
    private lateinit var binding: FragmentMapPlacesBinding

    val BASE_URL = "https://dapi.kakao.com/"

    val placeList = arrayListOf<Place>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMapPlacesBinding.inflate(layoutInflater)
        val view = binding.root


        recyclerView = binding.MapPlacesRecyclerView
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        searchKeyword(query.toString())
        return view
    }


    //주변 검색 데이터 타입
    private fun searchKeyword(keyword: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val interFace = retrofit.create(KAKAO_Place.KAKAOSearch::class.java)
        val call = interFace.getSearchKeywordWithoutLoca(
            FragmentMapShow.API_KEY,
            keyword
        )
        call.enqueue(object : Callback<KAKAO_Place.ResultSearchKeyword> {
            override fun onResponse(
                call: retrofit2.Call<KAKAO_Place.ResultSearchKeyword>,
                response: Response<KAKAO_Place.ResultSearchKeyword>
            ) {
                Log.d("카카오 검색 성공", "Raw: ${response.raw()}")
                Log.d("카카오 검색 성공", "Body: ${response.body()}")
                if (response.body() != null) {
                    placeList.clear()
                    var min=Math.min(15,response.body()!!.meta.pageable_count)
                    for (i in 0 until min) {
                        val tmp = response.body()!!.documents[i]
                        //Toast.makeText(this@MapActivity,tmp.place_name+" "+tmp.address_name,Toast.LENGTH_SHORT).show()
                        placeList.add(
                            Place(
                                tmp.place_name,
                                tmp.category_name,
                                tmp.phone,
                                tmp.address_name,
                                tmp.place_url,
                                tmp.distance,
                                tmp.x.toDouble(),
                                tmp.y.toDouble()
                            )
                        )
                    }
                    Log.i("확인", placeList.size.toString())
                    recyclerView.adapter = MapPlacesAdapter(placeList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

            }

            override fun onFailure(
                call: retrofit2.Call<KAKAO_Place.ResultSearchKeyword>,
                t: Throwable
            ) {
                Log.d("카카오 검색 실패", "통신 실패: ${t.message}")
            }

        })
    }

    private fun newInstant(query: String?): FragmentMapPlaces {
        val bundle = Bundle()
        val frag = FragmentMapPlaces(query)
        bundle.putString("query", query)
        frag.arguments = bundle
        return frag
    }


}