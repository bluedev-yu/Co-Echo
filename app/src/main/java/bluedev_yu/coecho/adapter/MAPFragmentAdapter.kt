package bluedev_yu.coecho.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import bluedev_yu.coecho.fragment.FragmentMapHashtag
import bluedev_yu.coecho.fragment.FragmentMapPlaces

class MAPFragmentAdapter(fm:FragmentManager):FragmentPagerAdapter(fm){
    override fun getCount():Int = 2

    override fun getItem(position: Int): Fragment {
        val fragment = when(position)
        {
            0 -> FragmentMapHashtag()
            else -> FragmentMapPlaces()

        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val title = when(position)
        {
            0 -> "해시태그"
            else -> "장소"
        }
        return title
    }

}