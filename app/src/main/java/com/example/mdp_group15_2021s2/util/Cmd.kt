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
    const val EXPLORATION_START = "PC,ES|"
    const val FASTEST_PATH_START = "PC,FS|"
    const val STOP = "PC,RS|"
    const val SP = "PC,SP|"

    /**
     * Robot movements
     */
    const val DIRECTION_LEFT = "AR,l83"
    const val DIRECTION_RIGHT = "AR,r83"
    const val DIRECTION_UP = "AR,f01"

    /**
     * Map Status
     */
//    const val CLEAR = "clr"

    @JvmStatic
    fun getWayPoint(x: Int, y: Int): String { return "XWP${coordinatesFormatter.format(x)}${coordinatesFormatter.format(y)}" }

    @JvmStatic
    fun getStartPoint(x: Int, y: Int): String { return "XWP${coordinatesFormatter.format(x)}${coordinatesFormatter.format(y)}" }
}