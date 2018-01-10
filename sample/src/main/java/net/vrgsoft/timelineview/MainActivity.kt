package net.vrgsoft.timelineview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import net.vrgsoft.timelineviewkotlin.ClickView
import net.vrgsoft.timelineviewkotlin.OnRowClick
import net.vrgsoft.timelineviewkotlin.YearLayout
import net.vrgsoft.timelineviewkotlin.YearModel


import java.util.ArrayList
import java.util.Random

class MainActivity : AppCompatActivity(), OnRowClick {
    private var mYearLayout: YearLayout? = null
    private var mModels: MutableList<YearModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mModels = ArrayList()
        for (i in 1860..2017) {
            mModels!!.add(SomeModel(i, i + Random().nextInt(10),
                    "https://camo.mybb.com/e01de90be6012adc1b1701dba899491a9348ae79/687474703a2f2f7777772e6a71756572797363726970742e6e65742f696d616765732f53696d706c6573742d526573706f6e736976652d6a51756572792d496d6167652d4c69676874626f782d506c7567696e2d73696d706c652d6c69676874626f782e6a7067", i.toString()))
        }
        mYearLayout = findViewById<View>(R.id.year_layout) as YearLayout

        val builder = YearLayout.Builder()
        builder.setYears(mModels as ArrayList<YearModel>)
                .setMaxYear(2017)
                .setMinYear(1860)
                .attachToActivity(this)
                .setOnRowClick(this)
                .setYearBackgroundColor(R.color.line_color)
                .setYearTitleColor(R.color.colorPrimary)
                .setYearRowTextColor(R.color.colorPrimaryDark)
                .create()
        mYearLayout!!.setBuilder(builder)
    }

    override fun onClick(year: Int, view: ClickView.ItemHolder) {
        Toast.makeText(this, "Clicked on year " + year, Toast.LENGTH_SHORT).show()
    }
}
