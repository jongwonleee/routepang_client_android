package com.itaewonproject.mypage

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.adapter.AdapterRouteEdit
import java.util.*

class EditItemTouchHelperCallback(var adapter: AdapterRouteEdit) : ItemTouchHelper.Callback() {
    private var listener: OnItemMoveListener

    var swipable = false
    init {
        listener = adapter
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        var dragFlag: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        return makeMovementFlags(dragFlag, ItemTouchHelper.LEFT)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        listener.OnItemDrag()
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return viewHolder.getItemViewType() == target.getItemViewType()
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

        listener.OnItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition())
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return swipable
    }

    interface OnItemMoveListener {
        fun OnItemMove(from: Int, to: Int): Boolean
        fun OnItemSwipe(pos: Int): Boolean
        fun OnItemDrag(): Boolean
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.OnItemSwipe(viewHolder.layoutPosition)
    }
}
