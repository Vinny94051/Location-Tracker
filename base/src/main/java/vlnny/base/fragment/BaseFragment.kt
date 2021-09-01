package vlnny.base.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import vlnny.base.activity.BaseActivity
import vlnny.base.ext.*
import vlnny.base.router.Router

abstract class BaseFragment : Fragment() {
    protected val router: Router
        get() = Router(
            activity as AppCompatActivity,
            (activity as BaseActivity).supportFragmentManager
        )

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(javaClass.simpleName, "OnAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(javaClass.simpleName, "OnCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(javaClass.simpleName, "OnViewCreated")
        initViewModel()
        initViews()
    }

    override fun onStart() {
        super.onStart()
        Log.d(javaClass.simpleName, "OnStart")
    }

    override fun onPause() {
        super.onPause()
        Log.d(javaClass.simpleName, "OnPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d(javaClass.simpleName, "OnResume")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(javaClass.simpleName, "OnDestroy")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(javaClass.simpleName, "OnDestroyView")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(javaClass.simpleName, "OnDetach")
    }

    @LayoutRes
    abstract fun layoutId(): Int

    open fun onFragmentHide() {
        Log.e(javaClass.simpleName, "onFragmentHide")
    }

    open fun initViewModel() {}
    open fun initViews() {}

    fun showToast(message: String) = activity?.showToast(message)

    fun showSnack(message: String) =
        activity?.showSnack(view!!, message)

    fun hideKeyboard(btn: View) = activity?.hideKeyboard(btn)

    fun showKeyboard(btn: View) = activity?.showKeyboard(btn)

    fun startShareIntent(send: String, @StringRes chooserHeadId: Int) =
        activity?.startShareIntent(send, chooserHeadId)

    fun startShareIntent(send: String, chooserHead: String) =
        activity?.startShareIntent(send, chooserHead)
}