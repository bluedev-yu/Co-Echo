package bluedev_yu.coecho

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import bluedev_yu.coecho.Fragment.FragmentResultHashtag
import bluedev_yu.coecho.Fragment.FragmentResultPeople

class SNSFragementAdapter(fm : FragmentManager, query: String?) : FragmentPagerAdapter(fm){
    private val query: String? = query

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        val fragment = when(position)
        {
            0 -> FragmentResultHashtag(query)
            else -> FragmentResultPeople(query)
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val title = when(position)
        {
            0 -> "해시태그"
            else -> "사용자"
        }
        return title
    }

}