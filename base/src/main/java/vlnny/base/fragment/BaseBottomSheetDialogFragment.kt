package vlnny.base.fragment

import androidx.annotation.LayoutRes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


abstract class BaseBottomSheetDialogFragment :
    BottomSheetDialogFragment() {

    protected var onFragmentCloseListener: (() -> Unit)? = null

    @LayoutRes
    abstract fun layoutId(): Int

    fun setOnCloseDialogListener(listener: (() -> Unit)) {
        onFragmentCloseListener = listener
    }

    override fun dismiss() {
        super.dismiss()
        onFragmentCloseListener?.invoke()
    }

}