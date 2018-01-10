package net.vrgsoft.timelineviewkotlin

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout

import java.util.ArrayList
import java.util.Collections

class YearLayout : LinearLayout {
    private var mYearView: YearView? = null
    private var mClickView: ClickView? = null
    private var mExpanderView: ExpanderView? = null
    private var mScrollView: HorizontalScrollView? = null
    private var mContext: Context? = null
    private var mActivity: Activity? = null
    private var pos: Int = 0

    private val xInWindow: Int
        get() {
            val display = mActivity!!.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.x

        }

    constructor(context: Context) : super(context) {
        mContext = context
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        mContext = context
        initAttributes(attrs)
    }

    private fun initAttributes(attributeSet: AttributeSet) {
        val typedArray = mContext!!.theme.obtainStyledAttributes(attributeSet, R.styleable.YearLayout, 0, 0)
        try {
            Builder.mRowTextSize = typedArray.getDimension(R.styleable.YearLayout_row_text_size, 14.toFloat()).toInt()
            Builder.mDescriptionTextSize = typedArray.getDimension(R.styleable.YearLayout_description_text_size, 8.toFloat()).toInt()
            Builder.mYearTextSize = typedArray.getDimension(R.styleable.YearLayout_year_text_size, 9.toFloat()).toInt()
            Builder.mMinYear = typedArray.getInteger(R.styleable.YearLayout_min_year, 0)
            Builder.mMaxYear = typedArray.getInteger(R.styleable.YearLayout_max_year, 0)
            Builder.mYearTextRowColor = typedArray.getColor(R.styleable.YearLayout_year_row_text_color,
                    ContextCompat.getColor(mContext!!, android.R.color.black))
            Builder.mYearBackgroundColor = typedArray.getColor(R.styleable.YearLayout_year_background_color, ContextCompat.getColor(mContext!!, android.R.color.darker_gray))
            Builder.mYearTitleColor = typedArray.getColor(R.styleable.YearLayout_year_title_color, ContextCompat.getColor(mContext!!, android.R.color.black))
        } finally {
            typedArray.recycle()
        }
    }

    private fun initViews(context: Context?, builder: Builder) {
        for (i in builder.minYear..builder.maxYear) {
            builder.yearsInts.add(i)
        }
        mActivity = builder.activity
        LayoutInflater.from(context).inflate(R.layout.year_view, this, true)

        mExpanderView = findViewById<View>(R.id.view) as ExpanderView?
        mClickView = findViewById<View>(R.id.click) as ClickView?
        mYearView = findViewById<View>(R.id.year) as YearView?

        mExpanderView!!.setMinYear(builder.minYear)
        mExpanderView!!.setMaxYear(builder.maxYear)
        mExpanderView!!.setYearLineColor(builder.yearBackgroundColor)
        mExpanderView!!.setYearTitleColor(builder.yearTitleColor)
        mExpanderView!!.setDescriptionTextSize(builder.descriptionTextSize)
        mExpanderView!!.setYearTextSize(builder.yearTextSize)
        mExpanderView!!.setYearModel(builder.getYears()!!)
        mExpanderView!!.setYears(builder.minYear, builder.minYear + Utils.getXInWindow(builder.activity!!) / Utils.getColumnWidth(mContext!!))


        mClickView!!.setMinYear(builder.minYear)
        mClickView!!.setYearsCount(builder.maxYear - builder.minYear)
        mClickView!!.setOnRowClick(builder.getOnRowClick()!!)
        mClickView!!.init()


        mYearView!!.setMaxYear(builder.maxYear)
        mYearView!!.setMinYear(builder.minYear)
        mYearView!!.setRowTextSize(builder.rowTextSize)
        mYearView!!.setYearModels(builder.getYears()!!)
        mYearView!!.setYearRowColor(builder.yearTextRowColor)
        mYearView!!.init()


        mScrollView = findViewById<View>(R.id.scrollView) as HorizontalScrollView?
        mScrollView!!.viewTreeObserver.addOnScrollChangedListener {
            if (mScrollView != null) {
                val scrollX = mScrollView!!.scrollX
                mExpanderView!!.setPadX(scrollX)
                pos = scrollX / Utils.getColumnWidth(context!!)
                if (pos > 0 && pos < builder.getYears()!!.size - xInWindow / Utils.getColumnWidth(context)) {
                    mExpanderView!!.setYears(builder.yearsInts[pos],
                            builder.yearsInts[pos + xInWindow / Utils.getColumnWidth(context)])
                    Log.d("POS", (pos + xInWindow / Utils.getColumnWidth(context)).toString() + "")
                }
                if (pos == 0) {
                    mExpanderView!!.setYears(builder.minYear, builder.minYear + xInWindow / Utils.getColumnWidth(context))
                }
            }
        }

    }

    fun setBuilder(builder: Builder) {
        initViews(mContext, builder)
    }

    fun setCheckedPosition(position: Int) {
        mYearView!!.setCheckedPosition(position)
        mClickView!!.setCheckedPosition(pos)
    }

    fun setNormal(yearPosition: Int) {
        mYearView!!.setNormal(yearPosition)
        mClickView!!.setNormal(yearPosition)
    }

    class Builder {
        private var mYears: List<YearModel>? = null
        internal var activity: Activity? = null
            private set
        private val mYearsInts = ArrayList<Int>()
        private var mOnRowClick: OnRowClick? = null

        internal val yearsInts: MutableList<Int>
            get() = mYearsInts

        internal val minYear: Int
            get() = mMinYear

        internal val maxYear: Int
            get() = mMaxYear

        internal val yearBackgroundColor: Int
            get() = mYearBackgroundColor

        internal val yearTitleColor: Int
            get() = mYearTitleColor

        internal val yearTextRowColor: Int
            get() = mYearTextRowColor

        internal val yearTextSize: Int
            get() = mYearTextSize

        internal val descriptionTextSize: Int
            get() = mDescriptionTextSize

        internal val rowTextSize: Int
            get() = mRowTextSize

        fun setYearTextSize(yearTextSize: Int): Builder {
            mYearTextSize = yearTextSize
            return this
        }

        fun setDescriptionTextSize(descriptionTextSize: Int): Builder {
            mDescriptionTextSize = descriptionTextSize
            return this
        }

        fun setYearTitleColor(@ColorRes yearColor: Int): Builder {
            mYearTitleColor = yearColor
            return this
        }

        fun setYearRowTextColor(@ColorRes yearRowTextColor: Int): Builder {
            mYearTextRowColor = yearRowTextColor
            return this
        }

        fun setYearBackgroundColor(@ColorRes yearBackgroundColor: Int): Builder {
            mYearBackgroundColor = yearBackgroundColor
            return this
        }

        fun setYears(years: List<YearModel>): Builder {
            mYears = years
            return this
        }

        fun setMinYear(minYear: Int): Builder {
            mMinYear = minYear
            return this
        }

        fun setMaxYear(maxYear: Int): Builder {
            mMaxYear = maxYear
            return this
        }

        fun attachToActivity(activity: Activity): Builder {
            this.activity = activity
            return this
        }

        fun setOnRowClick(onRowClick: OnRowClick): Builder {
            mOnRowClick = onRowClick
            return this
        }

        fun setRowTextSize(rowTextSize: Int): Builder {
            mRowTextSize = rowTextSize
            return this
        }

        fun create(): Builder {
            Collections.sort(mYears!!) { carModel, t1 ->
                if (carModel.start_date() < t1.start_date() && carModel.end_date() < t1.end_date()) {
                    -1
                } else if (carModel.start_date() > t1.start_date() && carModel.end_date() > t1.end_date()) {
                    1
                } else {
                    0
                }
            }
            if (mMaxYear < mMinYear) {
                throw IllegalStateException("Max year cannot be less then min year")
            }
            return this
        }

        internal fun getYears(): List<YearModel>? {
            return mYears
        }

        internal fun getOnRowClick(): OnRowClick? {
            return mOnRowClick
        }

        companion object {
            internal var mMinYear: Int = 0
            internal var mMaxYear: Int = 0
            internal var mYearBackgroundColor: Int = 0
            internal var mYearTitleColor: Int = 0
            internal var mYearTextRowColor: Int = 0
            internal var mYearTextSize: Int = 0
            internal var mDescriptionTextSize: Int = 0
            internal var mRowTextSize: Int = 0
        }
    }
}
