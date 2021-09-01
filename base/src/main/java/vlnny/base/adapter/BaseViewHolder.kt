package vlnny.base.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.reflect.KClass

abstract class BaseViewHolder<MV>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindView(item : MV)

    protected fun <T> setOnViewClickListener(view: View, listener: ((T) -> Unit)?, item: T) =
        view.setOnClickListener {
            listener?.invoke(item)
        }
}