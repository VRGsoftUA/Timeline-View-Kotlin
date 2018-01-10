package net.vrgsoft.timelineview

import net.vrgsoft.timelineviewkotlin.YearModel


class SomeModel(private var start_date: Int, private var end_date: Int, private var image: String?, private var title: String?) : YearModel {

    fun setStart_date(start_date: Int) {
        this.start_date = start_date
    }

    fun setEnd_date(end_date: Int) {
        this.end_date = end_date
    }

    fun setImage(image: String) {
        this.image = image
    }

    fun setTitle(title: String) {
        this.title = title
    }

    override fun title(): String? {
        return title
    }

    override fun start_date(): Int {
        return start_date
    }

    override fun end_date(): Int {
        return end_date
    }

    override fun image(): String? {
        return image
    }

    override fun description(): String? {
        return title
    }

}
