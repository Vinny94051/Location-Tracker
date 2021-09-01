package vlnny.base.ext

import android.animation.Animator
import android.view.ViewPropertyAnimator

fun ViewPropertyAnimator.setAnimationEndListener(listener: (Animator?) -> Unit) {
    this.setListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {}
        override fun onAnimationEnd(animation: Animator?) {
            listener.invoke(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationStart(animation: Animator?) {}
    })
}

fun ViewPropertyAnimator.setAnimationStartListener(listener: (Animator?) -> Unit) {
    this.setListener(object : Animator.AnimatorListener {
        override fun onAnimationRepeat(animation: Animator?) {}
        override fun onAnimationEnd(animation: Animator?) {}
        override fun onAnimationCancel(animation: Animator?) {}
        override fun onAnimationStart(animation: Animator?) {
            listener.invoke(animation)
        }
    })
}

