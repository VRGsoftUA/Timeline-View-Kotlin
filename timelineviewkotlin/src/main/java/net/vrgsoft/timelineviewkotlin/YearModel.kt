package net.vrgsoft.timelineviewkotlin

interface YearModel {
    fun title(): String?

    fun start_date(): Int

    fun end_date(): Int

    fun image(): String?

    fun description(): String?
}
