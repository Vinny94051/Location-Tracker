package vlnny.base.ext

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.speech.tts.TextToSpeech
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import vlnny.base.R

fun Activity.showToast(message: String) {
    val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
    toast.setGravity(0, 0, 0)
    toast.show()
}


fun Activity.vibrate() {
    val v = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    @Suppress("DEPRECATION")
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O)
        v.vibrate(VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE))
    else
        v.vibrate(150)
}

fun Activity.showSnack(container: View, text: String) {
    Snackbar.make(container, text, Snackbar.LENGTH_LONG).show()
}

fun Activity.hideKeyboard(btn: View) {
    val imm =
        this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(
        btn.windowToken,
        HIDE_NOT_ALWAYS
    )
}

fun Activity.showKeyboard(btn: View) {
    btn.requestFocus()
    val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(btn, InputMethodManager.SHOW_IMPLICIT)
}

fun Activity.hideActionBar() {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE)
    this.window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN,
        WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}

fun Context.openLink(link: String) {
    var linkLocal = link.trim()
    if (!linkLocal.startsWith("http"))
        linkLocal = "http://$linkLocal"

    try {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(linkLocal)
        this.startActivity(intent)
    } catch (ex: ActivityNotFoundException) {
        ex.printStackTrace()
        Toast.makeText(this, getString(R.string.link_is_invalid), Toast.LENGTH_SHORT).show()
    }
}

fun Activity.startShareIntent(
    send: String,
    @StringRes chooserHeaderId: Int
) {
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, send)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, getString(chooserHeaderId))
    startActivity(shareIntent)
}

fun Activity.startShareIntent(
    send : String,
    chooserHeader : String
){
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, send)
        type = "text/plain"
    }

    val shareIntent = Intent.createChooser(sendIntent, chooserHeader)
    startActivity(shareIntent)
}