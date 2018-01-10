package net.vrgsoft.timelineviewkotlin

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

import java.util.ArrayList
import java.util.Calendar
import java.util.HashMap

class ExpanderView : LinearLayout, View.OnClickListener {
    val items: List<TextView> = ArrayList()
    private var mContext: Context? = null
    private val mLastItem: ItemHolder? = null
    private var onClickPlusListener: OnClickPlus? = null
    private val relocationOffset: Int = 0
    private var prevYear = 0
    private var viewList: MutableList<View>? = null
    var carModel: List<YearModel>? = null
        private set
    private val mYearViews = HashMap<YearModel, ItemHolder>()
    private var mMinYear: Int = 0
    private var mMaxYear: Int = 0
    private var mYearTextSize = 9
    private var mDescriptionTextSize = 8
    private val calendar = Calendar.getInstance()

    //        this.yeareMax = maxCount;
    //        addItem();
    var maxCount = -1
        set(maxCount) {

        }

    private var lastYear: Int = 0
    private var lastVisYear: Int = 0
    private var firstYear: Int = 0
    private var coordX: Int = 0
    @ColorRes
    private var yearLineColor: Int = 0
    @ColorRes
    internal var yearTitleColor: Int = 0

    val stringFromList: Array<String?>
        get() {

            val size = items.size
            val text = arrayOfNulls<String>(size)
            for (i in 0 until size) {
                if (items[i].text.isNotEmpty()) {
                    text[i] = items[i].text.toString()
                }
            }

            return text

        }

    fun setMaxYear(maxYear: Int) {
        mMaxYear = maxYear
    }

    fun setMinYear(minYear: Int) {
        mMinYear = minYear
    }

    fun setYearLineColor(@ColorRes yearLineColor: Int) {
        this.yearLineColor = yearLineColor
    }

    fun setYearTextSize(yearTextSize: Int) {
        mYearTextSize = yearTextSize
    }

    fun setDescriptionTextSize(descriptionTextSize: Int) {
        mDescriptionTextSize = descriptionTextSize
    }

    fun setYearTitleColor(yearTitleColor: Int) {
        this.yearTitleColor = yearTitleColor
    }

    fun setYearModel(carModel: List<YearModel>) {
        this.carModel = carModel
        for (i in carModel.indices) {
            val item = ItemHolder(mContext, this)
            val car = carModel[i]
            prevYear = car.start_date()
            val width = ((if (car.end_date() > calendar.get(Calendar.YEAR))
                calendar.get(Calendar.YEAR)
            else
                car.end_date()) - car.start_date()) * Utils.getColumnWidth(mContext) + (Utils.getColumnWidth(mContext) - 50)
            val margin = (car.start_date() - mMinYear) * Utils.getColumnWidth(mContext) + 37
            val params = LinearLayout.LayoutParams(width, 40)
            params.setMargins(margin, 15, 0, 0)
            item.view.layoutParams = params
            item.name.text = car.title()
            item.year.text = String.format(" ` %s", car.description())
            item.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, mYearTextSize.toFloat())
            item.year.setTextSize(TypedValue.COMPLEX_UNIT_PX, mDescriptionTextSize.toFloat())

            if (yearLineColor != 0) {
                item.yearLine.setBackgroundColor(ContextCompat.getColor(mContext!!, yearLineColor))
            }
            if (yearTitleColor != 0) {
                item.name.setTextColor(ContextCompat.getColor(mContext!!, yearTitleColor))
                item.year.setTextColor(ContextCompat.getColor(mContext!!, yearTitleColor))
            }
            item.name.gravity = Gravity.CENTER_VERTICAL
            mYearViews.put(carModel[i], item)
        }

    }

    constructor(context: Context) : super(context) {
        if (!this.isInEditMode) {
            mContext = context
            init()
        }
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        if (!this.isInEditMode) {
            mContext = context
            init()
        }
    }

    constructor(context: Context, attrs: AttributeSet, style: Int) : super(context, attrs, style) {
        if (!this.isInEditMode) {
            mContext = context
            init()
        }
    }

    private fun init() {
        orientation = LinearLayout.VERTICAL
        viewList = ArrayList()
        addItem()
    }

    override fun onClick(view: View) {
        addItem()
    }


    private fun addItem() {
        var count = 0
        if (carModel != null) {

            removeAllViews()
            viewList!!.clear()
            for (i in carModel!!.indices) {
                if (carModel!![i].start_date() < firstYear && carModel!![i].end_date() > lastVisYear
                        || carModel!![i].end_date() > firstYear && carModel!![i].end_date() < lastVisYear
                        || (carModel!![i].start_date() < firstYear - 1
                        && carModel!![i].end_date() > firstYear - 1 && carModel!![i].start_date() < lastVisYear)
                        || carModel!![i].start_date() < lastVisYear && carModel!![i].end_date() > lastVisYear
                        || carModel!![i].start_date() <= firstYear && carModel!![i].end_date() >= firstYear
                        || carModel!![i].start_date() <= lastVisYear + 1 && carModel!![i].end_date() >= lastVisYear
                        || carModel!![i].start_date() >= firstYear && carModel!![i].end_date() <= lastVisYear
                        || carModel!![i].start_date() >= firstYear && carModel!![i].start_date() < lastVisYear) {
                    val model = carModel!![i]
                    prevYear = model.start_date()
                    val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100)


                    if (firstYear >= model.start_date()) {

                        val padd = coordX - (model.start_date() - mMinYear) * Utils.getColumnWidth(mContext)
                        if (padd + 60 > mYearViews[carModel!![i]]?.view?.width!!) {

                            mYearViews[carModel!![i]]?.name?.setPadding(padd - mYearViews[carModel!![i]]!!.name.width, 0, 0, 0)
                        } else {
                            mYearViews[carModel!![i]]?.name?.setPadding(padd, 0, 0, 0)
                        }
                    } else {
                        mYearViews[carModel!![i]]?.name!!.setPadding(10, 0, 0, 0)
                    }


                    params.setMargins(40, 0, 0, 0)
                    mYearViews[carModel!![i]]!!.name.layoutParams = params

                    addView(mYearViews[carModel!![i]]?.view, 0)
                    viewList!!.add(0, mYearViews[carModel!![i]]!!.view)
                    lastYear = carModel!![i].start_date()
                    count++
                    Log.d("COUNT", count.toString())
                }
                if (carModel!![i].start_date() > lastVisYear + 2) {
                    break
                }
            }

        }
        Log.d("ExpanderView", "Added")
    }


    fun setOnClickPlusListener(onClickPlusListener: OnClickPlus) {
        this.onClickPlusListener = onClickPlusListener
    }

    fun setPadX(scrollX: Int) {
        coordX = scrollX
    }

    interface OnClickPlus {
        fun onClickPlus(last: String)
    }

    private class ItemHolder(context: Context?, group: ExpanderView) {
        val view: View
        val name: TextView
        val year: TextView
        val yearLine: LinearLayout

        init {
            //View view = View.inflate(context, R.layout.item_expander_view, group);
            val view = (context as Activity).layoutInflater.inflate(R.layout.item_expander_view, null)
            name = view.findViewById<View>(R.id.tx_name) as TextView
            year = view.findViewById<View>(R.id.tx_year) as TextView
            yearLine = view.findViewById<View>(R.id.year_line) as LinearLayout
            this.view = view
        }
    }

    fun setYears(first: Int, last: Int) {
        firstYear = first
        lastVisYear = last
        addItem()
    }

    private fun isOnScreen(v: View, scrollBounds: Rect): Boolean {
        return v.getGlobalVisibleRect(scrollBounds)
    }

    private fun getXInWindow(v: View): Int {
        val coords = IntArray(2)
        v.getLocationInWindow(coords)
        return coords[0]
    }
}
