package com.itaewonproject.mypage

import android.util.Log
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.itaewonproject.adapter.AdapterRouteList
import java.util.*

class RoutesItemTouchHelperCallback (var adapter:AdapterRouteList): ItemTouchHelper.Callback(){
    private var listener:OnItemMoveListener
    private var mFrom:Int?=null
    private var mTo:Int?=null
    private var befY:Int?=null
    private var aftY:Int?=null
    lateinit var movedTime: Date
    init{
        listener = adapter
    }



    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        var dragFlag:Int =ItemTouchHelper.UP or ItemTouchHelper.DOWN
        var pos = viewHolder.layoutPosition
        var routes = adapter.list
        if(pos<routes.size){
            //if()
            return makeMovementFlags(dragFlag,ItemTouchHelper.LEFT)
        }else
        {
            return makeMovementFlags(0,0)
        }
    }



    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        if(mFrom!=null && mTo!=null&& befY!=null && aftY!=null){
            listener.OnMerge(mFrom!!,mTo!!,movedTime)

        }
        mTo = null
        mFrom = null
        befY=null
        aftY=null

    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        /*if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }*/
        Log.i("!!!@","$befY + $aftY, $mFrom $mTo")

        // remember FIRST from position
            mFrom = viewHolder.adapterPosition
            mTo = target.adapterPosition
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
        if(befY==null) befY=y
        else aftY=y
        //Log.i("!!!","$fromPos, $toPos")
        //listener.OnItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    interface OnItemMoveListener{
        fun OnMerge(from:Int,to:Int, date: Date)
        fun OnShake(pos:Int, date: Date)
        fun OnItemSwipe(pos:Int):Boolean
        //fun OnItemDrag(from:Int,to:Int,date:Date):Boolean

    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.OnItemSwipe(viewHolder.layoutPosition)
    }

}