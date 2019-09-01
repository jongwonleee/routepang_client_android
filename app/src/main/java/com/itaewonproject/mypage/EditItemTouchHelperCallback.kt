package com.itaewonproject.mypage

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.adapter.AdapterRouteEdit
import java.util.*

class EditItemTouchHelperCallback (var adapter: AdapterRouteEdit): ItemTouchHelper.Callback(){
    private var listener:OnItemMoveListener
    private var mFrom:Int?=null
    private var mTo:Int?=null
    lateinit var movedTime: Date

    init{
        listener = adapter
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        var dragFlag:Int =ItemTouchHelper.UP or ItemTouchHelper.DOWN
        Log.i("!!","layout:${viewHolder.layoutPosition} adapter:${viewHolder.adapterPosition}")
        //
            return makeMovementFlags(dragFlag,ItemTouchHelper.LEFT)

        //var swipeFlag = ItemTouchHelper.LEFT
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        if (mFrom != null && mTo != null&& movedTime!=null)
           listener.OnItemDrag(mFrom!!,mTo!!,movedTime!!)
        mTo = null
        mFrom = mTo
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        // remember FIRST from position
        if (mFrom == null)
            mFrom = viewHolder.adapterPosition
        mTo = target.adapterPosition
        Log.i("onMove","$mFrom -> $mTo ")
        movedTime= Date()
        // Notify the adapter of the move
        return true
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
        listener.OnItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return false
    }

    interface OnItemMoveListener{
        fun OnItemMove(from:Int,to:Int):Boolean
        fun OnItemSwipe(pos:Int):Boolean
        fun OnItemDrag(from:Int,to:Int,date:Date):Boolean

    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.OnItemSwipe(viewHolder.layoutPosition)
    }

}