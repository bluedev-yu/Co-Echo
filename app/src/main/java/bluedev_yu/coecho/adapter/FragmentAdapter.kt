package bluedev_yu.coecho.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import bluedev_yu.coecho.fragment.fragmentLikeStores
import bluedev_yu.coecho.fragment.fragmentMyFeed
import bluedev_yu.coecho.fragment.fragmentMyReview
import bluedev_yu.coecho.fragment.fragmentSubscriber

class FragmentAdapter (fm : FragmentManager, uid: String?) : FragmentPagerAdapter(fm){
    private val uid: String? = uid

    override fun getItem(position: Int): Fragment {
        val fragment = when(position)
        {
            0 -> fragmentMyFeed(uid)
            1 -> fragmentMyReview(uid)
            2 -> fragmentSubscriber(uid)

            else -> throw AssertionError()
        }
        return fragment
    }

    //tab 개수
    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? {
        val title = when(position)
        {
            0 -> "피드"
            1 -> "리뷰"
            2 -> "장소 즐겨찾기"

            else -> throw AssertionError()
        }
        return title    }
}