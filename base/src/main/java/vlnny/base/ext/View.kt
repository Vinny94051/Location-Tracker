package vlnny.base.ext

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hideKeyboard() {
    val imm =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(
        this.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS
    )
}

fun View.showKeyboard() {
    this.requestFocus()
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.clearText(){
    this.text = "".toEditable()
}

fun EditText.isEmpty() : Boolean =
    this.text.toString().isEmpty()

fun ImageView.rotateFromTopToBottom(){
    animate().rotation(180f).start()
}

fun ImageView.rotateDefault() =
    animate().rotation(0f).start()
