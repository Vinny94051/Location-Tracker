package vlnny.base.adapter

import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<VH : BaseViewHolder<MV>, MV> : RecyclerView.Adapter<VH>() {

    private var items: List<MV> = mutableListOf()
    val currentList: List<MV>
        get() = items

    /**
     * Override this fun if you need to bind only new items
     * @param oldItem item which will be replaced
     * @param newItem item which will be inserted
     *
     * @return true if item equals, false if don't
     */
    open fun equals(oldItem: MV, newItem: MV): Boolean =
        oldItem == newItem

    override fun getItemCount(): Int = items.size

    abstract fun setViewHolders(parent: ViewGroup): HashMap<Int, BaseViewHolder<out MV>>

    override fun getItemViewType(position: Int) = ONE_ITEM_VIEW_TYPE

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH =
        setViewHolders(parent)[viewType] as VH

    override fun onBindViewHolder(holder: VH, position: Int)
            = holder.bindView(items[position])

    fun updateList(data: List<MV>) {
        items = data
        notifyDataSetChanged()
    }

    protected fun addItem(item: MV) {
        items.toMutableList().add(item)
        notifyItemChanged(items.size - 1)
    }

    protected fun replaceItem(item: MV, position: Int) {
        val itemsTmp = items.toMutableList()
        itemsTmp[position] = item
        items = itemsTmp

        Log.e(javaClass.simpleName, "$items, : $item")
    }

    protected fun deleteItem(position: Int) {
        items.toMutableList().removeAt(position)
        updateList(items)
    }

    companion object {
        /**
         * If recycler has only one item view type, use this const like view type
         *
         */

        const val ONE_ITEM_VIEW_TYPE = 1
    }
}