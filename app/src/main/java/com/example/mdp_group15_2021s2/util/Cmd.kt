@file:Suppress("unused")

package com.example.mdp_group15_2021s2.util

import java.text.DecimalFormat

object Cmd {
    /**
     * Utility Variables
     */
    private val coordinatesFormatter = DecimalFormat("00")

    /**
     * Exploration/Fastest Path
     */
    const val EXPLORATION_START = "ES|"
    const val FASTEST_PATH_START = "FS|"
    const val STOP = "RS|"

    /**
     * Robot movements
     */
    const val DIRECTION_LEFT = "TL"
    const val DIRECTION_RIGHT = "TR"
    const val DIRECTION_UP = "F"
    const val DIRECTION_DOWN = "R"

    /**
     * Map Status
     */
    const val CLEAR = "clr"

    @JvmStatic
    fun getWayPoint(x: Int, y: Int): String { return "XWP${coordinatesFormatter.format(x)}${coordinatesFormatter.format(y)}" }

    @JvmStatic
    fun getStartPoint(x: Int, y: Int): String { return "XWP${coordinatesFormatter.format(x)}${coordinatesFormatter.format(y)}" }
}