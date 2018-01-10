package net.vrgsoft.timelineviewkotlin

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout

import java.util.ArrayList


class ClickView : LinearLayout, View.OnClickListener {

    private var mContext: Context? = null
    private val views = ArrayList<ItemHolder>()
    private val position: ScrollPosition? = null
    private var onRowClick: OnRowClick? = null
    private var checkedPosition = -1
    private var mYearsCount: Int = 0
    private var mMinYear: Int = 0

    fun setYearsCount(yearsCount: Int) {
        mYearsCount = yearsCount
    }

    fun setMinYear(minYear: Int) {
        mMinYear = minYear
    }

    constructor(context: Context) : super(context) {
        if (!this.isInEditMode) {
            mContext = context

        }
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        if (!this.isInEditMode) {
            mContext = context
        }
    }

    constructor(context: Context, attrs: AttributeSet, style: Int) : super(context, attrs, style) {
        if (!this.isInEditMode) {
            mContext = context
        }
    }

    fun init() {
        orientation = LinearLayout.HORIZONTAL
        addItem()
    }

    override fun onClick(view: View) {

    }


    private fun addItem() {
        showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        for (i in 0 until  mYearsCount) {
            val item = ItemHolder(this.mContext!!, this)
            val params = LinearLayout.LayoutParams(Utils.getColumnWidth(this!!.mContext!!), ViewGroup.LayoutParams.MATCH_PARENT)
            item.view.layoutParams = params
            item.popUp.id = ID_DEFAULT + i
            item.view.setOnClickListener {
                if (onRowClick != null) {
                    onRowClick!!.onClick(mMinYear + i, item)
                }
            }
            views.add(item)
            addView(item.view)
            Log.d("ClickView", "Added")

        }

    }

    inner class ItemHolder(context: Context, group: ClickView) {
        val view: View
        var popUp: FrameLayout


        init {
            //View view = View.inflate(context, R.layout.item_expander_view, group);
            view = (context as Activity).layoutInflater.inflate(R.layout.click_layout, null)
            popUp = view.findViewById<View>(R.id.llRoot) as FrameLayout


        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        Log.d("MyPos", l.toString() + " " + t)
    }

    private fun isOnScreen(v: View, scrollBounds: Rect): Boolean {
        return v.getGlobalVisibleRect(scrollBounds)
    }

    private fun getXInWindow(v: View): Int {
        val coords = IntArray(2)
        v.getLocationInWindow(coords)
        return coords[0]
    }


    interface ScrollPosition {
        fun visiblePostion(pos: Int)
    }

    fun setOnRowClick(onRowClick: OnRowClick) {
        this.onRowClick = onRowClick
    }


    fun setCheckedPosition(position: Int) {
        if (checkedPosition != -1) {
            setNormal(checkedPosition)
        }
        this.checkedPosition = position
        val params = LinearLayout.LayoutParams(LAYOUT_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT)
        val view = views[checkedPosition].view
        view.layoutParams = params

    }

    fun setNormal(position: Int) {
        val params = LinearLayout.LayoutParams(Utils.getColumnWidth(mContext),
                ViewGroup.LayoutParams.MATCH_PARENT)
        val holder = views[position]
        holder.view.layoutParams = params
        holder.popUp.removeAllViews()
        checkedPosition = -1
    }

    fun getItemHolderByPos(i: Int): ItemHolder {
        return views[i]
    }

    companion object {
        private val ID_DEFAULT = 999
        private val LAYOUT_WIDTH = 900
    }

}
