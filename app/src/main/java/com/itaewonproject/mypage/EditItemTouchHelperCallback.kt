package com.itaewonproject.mypage

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.adapter.AdapterRouteEdit

class EditItemTouchHelperCallback(var adapter: AdapterRouteEdit) : ItemTouchHelper.Callback() {
    private var listener: OnItemMoveListener

    var swipable = false
    init {
        listener = adapter
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlag: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlag, ItemTouchHelper.LEFT)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        listener.onItemDrag()
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return viewHolder.itemViewType == target.itemViewType
    }

    override fun onMoved(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        fromPos: Int,
        target: RecyclerView.ViewHolder,
        toPos: Int,
        x: Int,
        y: Int
    ) {

        listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return swipable
    }

    interface OnItemMoveListener {
        fun onItemMove(from: Int, to: Int): Boolean
        fun onItemSwipe(pos: Int): Boolean
        fun onItemDrag(): Boolean
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onItemSwipe(viewHolder.layoutPosition)
    }
}
