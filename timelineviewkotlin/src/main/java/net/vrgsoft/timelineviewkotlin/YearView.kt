package net.vrgsoft.timelineviewkotlin

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.squareup.picasso.Picasso
import java.lang.IllegalArgumentException

import java.util.ArrayList

class YearView : LinearLayout, View.OnClickListener {

    private var mContext: Context? = null
    private val views = ArrayList<View>()
    private var checkedPosition = -1
    private var mMaxYear: Int = 0
    private var mMinYear: Int = 0
    private var mYearModels: List<YearModel>? = null
    private var mRowTextSize = 14
    @ColorRes
    private var mYearRowColor: Int = 0

    fun setYearRowColor(yearRowColor: Int) {
        mYearRowColor = yearRowColor
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

    fun setYearModels(yearModels: List<YearModel>) {
        mYearModels = yearModels
    }

    fun init() {
        orientation = LinearLayout.HORIZONTAL
        addItem()
    }

    override fun onClick(view: View) {

    }

    fun setRowTextSize(rowTextSize: Int) {
        mRowTextSize = rowTextSize
    }

    private fun addItem() {
        showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        for (i in 0 until mMaxYear - mMinYear) {
            val item = ItemHolder(mContext, this)
            val params = LinearLayout.LayoutParams(Utils.getColumnWidth(mContext!!), ViewGroup.LayoutParams.MATCH_PARENT)
            item.view.layoutParams = params
            item.name.setTextColor(ContextCompat.getColor(mContext!!, mYearRowColor))
            val sb = StringBuilder()
            sb.append(mMaxYear).append(i).append("")
            val c = sb.toString()
            item.name.text = c
//            item.name.setText(mMinYear + i + "")
            item.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, mRowTextSize.toFloat())
            if (mYearModels!!.isNotEmpty() && mYearModels!![i].image() != null) {
                item.CardView1.visibility = View.VISIBLE
                if (mContext != null) {
                    try {
                        Picasso.with(mContext).load(mYearModels!![0].image()).into(item.container1)
                    } catch (e: IllegalArgumentException) {
                        Log.e("YearView", "Error")
                    }

                }
            }
            views.add(item.view)
            addView(item.view)
            Log.d("YearView", "Added")
        }

    }

    private class ItemHolder internal constructor(context: Context?, group: YearView) {
        val view: View
        val name: TextView
        internal val container1: ImageView
        internal val container2: ImageView
        internal val CardView1: CardView
        internal val CardView2: CardView

        init {
            //View view = View.inflate(context, R.layout.item_expander_view, group);
            val view = (context as Activity).layoutInflater.inflate(R.layout.year_layout, null)
            container1 = view.findViewById<View>(R.id.image_container1) as ImageView
            container2 = view.findViewById<View>(R.id.image_container2) as ImageView
            CardView1 = view.findViewById<View>(R.id.image_card1) as CardView
            CardView2 = view.findViewById<View>(R.id.image_card2) as CardView
            name = view.findViewById<View>(R.id.tx_year) as TextView


            this.view = view
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


    interface ScrolPosition {
        fun visiblePostion(pos: Int)
    }


    fun onDestroy() {

    }


    fun setCheckedPosition(position: Int) {
        if (checkedPosition != -1) {
            setNormal(checkedPosition)
        }
        this.checkedPosition = position
        val params = LinearLayout.LayoutParams(LAYOUT_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT)
        val view = views[checkedPosition]
        view.layoutParams = params

    }

    fun setNormal(position: Int) {
        val params = LinearLayout.LayoutParams(Utils.getColumnWidth(mContext!!), ViewGroup.LayoutParams.MATCH_PARENT)
        val view = views[position]
        view.layoutParams = params
        checkedPosition = -1
    }

    fun setMaxYear(maxYear: Int) {
        mMaxYear = maxYear
    }

    fun setMinYear(minYear: Int) {
        mMinYear = minYear
    }

    companion object {
        private val LAYOUT_WIDTH = 900
    }
}
