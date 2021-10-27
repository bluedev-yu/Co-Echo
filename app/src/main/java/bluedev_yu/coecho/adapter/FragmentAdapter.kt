package bluedev_yu.coecho.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import bluedev_yu.coecho.Fragment.fragmentLikeStores
import bluedev_yu.coecho.Fragment.fragmentMyFeed
import bluedev_yu.coecho.Fragment.fragmentMyReview
import bluedev_yu.coecho.Fragment.fragmentSubscriber

class FragmentAdapter (fm : FragmentManager) : FragmentPagerAdapter(fm){

    override fun getItem(position: Int): Fragment {
        val fragment = when(position)
        {
            0 -> fragmentMyFeed().newInstant()
            1 -> fragmentMyReview().newInstant()
            2 -> fragmentLikeStores().newInstant()
            3 -> fragmentSubscriber().newInstant()

            else -> throw AssertionError()
        }
        return fragment
    }

    //tab 개수
    override fun getCount(): Int = 4

    override fun getPageTitle(position: Int): CharSequence? {
        val title = when(position)
        {
            0 -> "피드"
            1 -> "리뷰"
            2 -> "찜한 가게"
            3 -> "구독"

            else -> throw AssertionError()
        }
        return title    }
}