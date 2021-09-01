package vlnny.base.activity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import vlnny.base.ext.hideKeyboard
import vlnny.base.router.Router

abstract class BaseActivity : AppCompatActivity() {

    val router: Router
        get() = Router(this, supportFragmentManager)

    private var backStackCounter: Int = 0

    private var onBackStackChangedListener =
        FragmentManager.OnBackStackChangedListener {
            hideKeyboard(currentFocus ?: View(this))
            backStackCounter = supportFragmentManager.backStackEntryCount

            if (backStackCounter > 0) {
                if (supportFragmentManager.backStackEntryCount > 1) {
                    router.getFragmentByPositionFromPreTop(2)?.onFragmentHide()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        supportActionBar?.hide()
        backStackCounter = supportFragmentManager.backStackEntryCount
        setOnBackStackChangedListener()
    }

    @LayoutRes
    abstract fun layoutId(): Int

    private fun setOnBackStackChangedListener() =
        supportFragmentManager.addOnBackStackChangedListener(onBackStackChangedListener)

}