package vlnny.base.router

import android.content.Intent
import android.transition.Slide
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.manager.SupportRequestManagerFragment
import vlnny.base.fragment.BaseFragment

class Router(
    private var activity: AppCompatActivity,
    var fragmentManager: FragmentManager
) {

    fun openActivity(to: AppCompatActivity) =
        activity.startActivity(Intent(activity, to::class.java))

    private fun createSlideFragmentTransaction(fragment: Fragment) = fragment.apply {
         enterTransition = Slide(Gravity.END)
         exitTransition = Slide(Gravity.START)
    }


    fun replaceFragmentWithAddingInBackStack(fragment: Fragment, container: Int, tag: String) =
        fragmentManager
            .beginTransaction()
            .addToBackStack(tag)
            .replace(container, fragment, tag)
            .commit()

    fun addFragmentWithAddingInBackStack(fragment: Fragment, container: Int, tag: String) =
        fragmentManager
            .beginTransaction()
            .addToBackStack(tag)
            .add(container, createSlideFragmentTransaction(fragment), tag)
            .commit()

    fun removeFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .remove(fragment)
            .commit()
    }

    fun isFragmentInBackStack(fragmentId: String): Boolean =
        when {
            fragmentManager.fragments.contains(fragmentManager.findFragmentByTag(fragmentId)) -> true
            else -> false
        }


    fun isAlmostEmpty() =
        when {
            fragmentManager.fragments.size == 1 -> true
            fragmentManager.fragments.size == 2 && (fragmentManager.fragments[1] is SupportRequestManagerFragment || fragmentManager.fragments[0] is SupportRequestManagerFragment) -> true
            else -> false
        }

    internal fun getFragmentByPositionFromPreTop(positionFromPreTop: Int): BaseFragment? {
        val fragTag = fragmentManager.getBackStackEntryAt(
            fragmentManager.backStackEntryCount - positionFromPreTop
        ).name

        val fragment = fragmentManager.findFragmentByTag(fragTag)
        return if (fragment != null) fragment as BaseFragment else null
    }
}