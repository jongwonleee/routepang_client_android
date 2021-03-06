package com.itaewonproject.mypage

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.*
import com.itaewonproject.APIs
import com.itaewonproject.JsonParser
import com.itaewonproject.R
import com.itaewonproject.Routepang
import com.itaewonproject.adapter.AdapterRouteEdit
import com.itaewonproject.model.receiver.Location
import com.itaewonproject.model.receiver.Product
import com.itaewonproject.model.sender.Customer
import com.itaewonproject.rests.get.GetLocationConnector
import java.lang.NullPointerException

class RouteEditFragment : Fragment(), AdapterRouteEdit.OnStartDragListener {
    lateinit var  customer:Customer


    override fun OnStartDrag(viewHolder: RecyclerView.ViewHolder) {
        itemTouchHelper.startDrag(viewHolder)
    }

    private lateinit var buttonMap: ImageView
    private lateinit var buttonBack: ImageView
    private lateinit var buttonEdit: ImageView
    private lateinit var textTitle: TextView
    private lateinit var editTitle: TextView
    private lateinit var textTotalTime: TextView
    private lateinit var textTotalMove: TextView
    private lateinit var recyclerView: RecyclerView

    lateinit var list: ArrayList<Product>
    private var durations: Long? = null
    private var distances: Long? = null
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var adapter: AdapterRouteEdit? =null
    private lateinit var callback: EditItemTouchHelperCallback
    private lateinit var geoApiContext: GeoApiContext
    private var editMode = false
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        try{
            if (isVisibleToUser && isResumed ) {
                Log.i("isvisible","${(parentFragment as RouteFragment).route.title}")
                list = (parentFragment as RouteFragment).route.products
                adapter!!.list = list
                adapter!!.resetSteplist()
                adapter!!.notifyDataSetChanged()
                editTitle.text = Editable.Factory.getInstance().newEditable((parentFragment as RouteFragment).route.title)
                textTitle.text = (parentFragment as RouteFragment).route.title
                setEditMode()
            }
        }catch (e:NullPointerException){
            e.printStackTrace()
        }

    }

    private fun setListViewOption(view: View) {
        list = arrayListOf()
        adapter = AdapterRouteEdit(view.context, this)
        callback = EditItemTouchHelperCallback(adapter!!)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
        val linearLayoutManager = LinearLayoutManager(view.context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = adapter

        recyclerView.setHasFixedSize(true)
        //setEditMode()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i("view Created!!!","!!")
        buttonMap = view.findViewById(R.id.image_map)
        recyclerView = view.findViewById(R.id.edit_recyclerView) as RecyclerView
        buttonBack = view.findViewById(R.id.button_back)
        buttonEdit = view.findViewById(R.id.image_edit)
        textTitle = view.findViewById(R.id.text_title)
        editTitle = view.findViewById(R.id.edit_title)
        textTotalTime = view.findViewById(R.id.text_totalTime)
        textTotalMove = view.findViewById(R.id.text_totalMove)
        buttonMap.setOnClickListener {
            if (!editMode) {
                (parentFragment as RouteFragment).toFragment(true)
            }
        }
        buttonBack.setOnClickListener {
            if (!editMode) (parentFragment as RouteFragment).toFragment(false)
        }
        buttonEdit.setOnClickListener {
            editMode = !editMode
            setEditMode()
        }
        geoApiContext = GeoApiContext.Builder().apiKey(context!!.getString(R.string.google_key)).build()
        setListViewOption(view)
    }

    private fun setEditMode() {
        if((activity!!.application as Routepang).customer.customerId == customer.customerId){
            callback.swipable = editMode
            if (editMode) {
                textTitle.visibility = View.INVISIBLE
                editTitle.visibility = View.VISIBLE
                buttonBack.visibility = View.GONE
                buttonMap.visibility = View.GONE
                editTitle.text = Editable.Factory.getInstance().newEditable(textTitle.text)
            } else {
                textTitle.visibility = View.VISIBLE
                editTitle.visibility = View.INVISIBLE
                buttonBack.visibility = View.VISIBLE
                buttonMap.visibility = View.VISIBLE
                val route = (parentFragment as RouteFragment).route
                if(!route.title.equals(editTitle.text.toString())) {
                    route.title = editTitle.text.toString()
                }
                textTitle.text = route.title
                setTotals()
            }
            adapter!!.setEditMode(editMode)
        }
        buttonEdit.visibility=View.GONE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.customer = arguments!!.getSerializable("customer")as Customer
        return inflater.inflate(R.layout.fragment_route_edit, container, false)
    }

    private fun setTotals() {
        var durations: Long = 0
        distances = 0
        val arrayPoints = arrayListOf<LatLng>()
        Log.i("!!!", "$list")
        for (l in list) {
            arrayPoints.add(l.location.latlng())
            durations += l.location.usedTime.toLong()
        }

        // FIXME: context variable
        for (i in 0..arrayPoints.size - 2) {
            calculateDirections(arrayPoints[i], arrayPoints[i + 1], i)
        }
        this.durations = durations
    }

    private fun calculateDirections(origin: LatLng, dest: LatLng, pos: Int) {
        val destination = com.google.maps.model.LatLng(dest.latitude, dest.longitude)
        val directions = DirectionsApiRequest(geoApiContext)

        directions.alternatives(true)
        directions.mode(TravelMode.TRANSIT)
        directions.origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
        directions.language("ko")
        directions.destination(destination)
            .setCallback(object : PendingResult.Callback<DirectionsResult> {
                override fun onResult(result: DirectionsResult) {
                    Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString())
                    Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString())
                    setResult(result, pos)
                    Log.d(Constraints.TAG, "onResult: successfully retrieved directions.")
                }

                override fun onFailure(e: Throwable) {
                    Log.e(Constraints.TAG, "calculateDirections: Failed to get directions: " + e.message)
                }
            })
    }


    //TODO route.legs[]의 원본 저장하는 리스트 만들기
    fun setResult(result: DirectionsResult, pos: Int) {
        Handler(Looper.getMainLooper()).post({
            val route = result.routes[0]
            assert(durations != null)
            assert(distances != null)

            var durations = this.durations!!
            var distances = this.distances!!

            durations += route.legs[0].duration.inSeconds
            distances += route.legs[0].distance.inMeters
            Log.i("!!!", "$durations, $distances")
            textTotalTime.text = APIs.secToString(durations)
            textTotalMove.text = if (distances > 1000) {
                "%.1f km".format((distances.toFloat() / 1000))
            } else {
                "${distances}m"
            }

            this.durations = durations
            this.distances = distances

            adapter!!.stepList[pos] = result.routes[0].legs[0].steps.toList()
            /*or(s in result.routes[0].legs[0].steps)
                Log.i("!!!steps:",s.htmlInstructions)*/
        })
    }
}
