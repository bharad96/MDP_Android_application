package com.example.mdp_group15_2021s2.util

import android.util.Log
import com.example.mdp_group15_2021s2.BuildConfig
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import com.example.mdp_group15_2021s2.entity.Map
import java.math.BigInteger

class Parser() {

    private var payload: JSONObject? = null
    var robotX = 0
    var robotY = 0
    var robotDir = ""
    var robotStatus = ""
    var lastImageID = ""
    private var currentPayload = ""

    // Used to draw the grid map
    val exploredMap = Array(Map.COLUMN) { Array(Map.ROW) { "" } }

    var validPayload = true

    fun parse(payload: String) {
        val tmpPayload: JSONObject?
        this.currentPayload = payload

        try {
            tmpPayload = JSONObject(payload)
            this.payload = tmpPayload

            setRobot()
            setMDF()
            processImage()
        } catch (jsonEx: JSONException) {
            Log.d(TAG, "JSON EXCEPTION1")
            this.validPayload = false
        }
    }



    private fun setRobot() {
        if (!this.validPayload) return

        this.payload?.let {
            try {
                val robot = it.getJSONArray("robotPosition")

                this.robotX = robot.getInt(0)
                this.robotY = 19 - robot.getInt(1)
                val angle = robot.getInt(2)

                this.robotDir = when (angle) {
                    0 -> "UP"
                    90 -> "RIGHT"
                    180 -> "DOWN"
                    270 -> "LEFT"
                    else -> "RIGHT" // DEFAULT RIGHT
                }
            } catch (jsonEx: JSONException) {
                Log.d(TAG, "JSON EXCEPTION")
                this.validPayload = false
            } catch (indexEx: IndexOutOfBoundsException) {
                Log.d(TAG, "INDEX OUT OF BOUNDS EXCEPTION")
                this.validPayload = false
            } catch (castEx: ClassCastException) {
                Log.d(TAG, "CLASS CAST EXCEPTION")
                this.validPayload = false
            }
        }
    }

    fun setStatus(): Boolean { return try { this.robotStatus = this.payload?.getString("status") ?: "Unknown"; true } catch (e: Exception) { Log.d(TAG, "EXCEPTION"); false } }

    fun processImage() {
//        if (!this.validPayload) return

        this.payload?.let {
            try {
                val images = it.getJSONArray("images")
                var imgID = "0"

                var imgX: Int
                var imgY: Int

                for (i in 0 until images.length()) {
                    var image = images.getJSONObject(i)
                    imgID = image.getString("id")
                    imgX = image.getInt("x")
                    imgY = image.getInt("y")

                    hexImage += " ($imgID,$imgX,$imgY),"
                    Log.e(TAG, "Process Img i:$i x:$imgX y:$imgY id:$imgID")
                    this.exploredMap[imgX][imgY] = imgID
                }

                if (hexImage.isNotEmpty()) hexImage = hexImage.trimEnd(',') // Previously substring remove length-1
                this.lastImageID = imgID
            } catch (jsonEx: JSONException) {
                Log.d(TAG, "JSON EXCEPTION")
                this.validPayload = false
            } catch (indexEx: IndexOutOfBoundsException) {
                Log.d(TAG, "Process Image INDEX OUT OF BOUNDS EXCEPTION")
                this.validPayload = false
            } catch (castEx: ClassCastException) {
                Log.d(TAG, "CLASS CAST EXCEPTION")
                this.validPayload = false
            }
        }
    }

    private fun setMDF() {
        if (!this.validPayload) { Log.d("MDF", "Invalid Payload"); return }

        mdfPayload = this.currentPayload

        this.payload?.let {
            try {
                var map = JSONObject(it.getString("map"))
                Log.e("Parser", "map: $map")
                var exploredMDF = map.getString("explored")
                var obstacleMDF = map.getString("obstacle")
                Log.e("Parser", "Explored: $exploredMDF")
                Log.e("Parser", "Obstacle: $obstacleMDF")
                /**
                 * Explored Portion
                 */
                hexMDF = exploredMDF
                exploredMDF = BigInteger(exploredMDF, 16).toString(2)
                exploredMDF = exploredMDF.substring(2,
                        302)
                if (DEBUG) Log.d("MDF", "Explored MDF: $exploredMDF")

                // Explored mdf is now len 300
                val exploredLength = exploredMDF.replace("0", "").length
                val obstaclePad = exploredLength % 4
                if (DEBUG) Log.d("MDF", "Obstacle Padding: $obstaclePad")

                hexExplored = obstacleMDF
                obstacleMDF = BigInteger(obstacleMDF, 16).toString(2)
                val obstacleMdfHexToBinLen = hexExplored.length * 4
                obstacleMDF = String.format("%${obstacleMdfHexToBinLen}s", obstacleMDF).replace(" ", "0")
                if (DEBUG) Log.d("MDF", "Obstacle MDF: $obstacleMDF")

                Log.d("MDF", "Parsing Explored String on map")
                for (i in 0 until Map.ROW) {
                    for (j in 0 until Map.COLUMN) {
                        val characterIndex = (i * Map.COLUMN) + j
                        exploredMap[j][i] = exploredMDF[characterIndex].toString()
                    }
                }
                if (DEBUG) printMapDbg()

//                obstacleMDF = 00000000000000000000000010000000000000000000 length = 44
                Log.d("MDF", "Parsing Obstacle String on map")
                var counter = 0
                for (i in 0 until Map.ROW) {
                    for (j in 0 until Map.COLUMN) {
                        if (exploredMap[j][i] == "1") {
                            if (obstacleMDF[counter] == '1') {
                                exploredMap[j][i] = "O"
                            }
                            counter++
                        }
//                        Log.e(TAG, "i:${i} j:${j} counter:${counter} ")
                    }
                }
                if (DEBUG) printMapDbg()

            } catch (jsonEx: JSONException) {
                Log.e(TAG, "JSON EXCEPTION")
                this.validPayload = false
            } catch (indexEx: IndexOutOfBoundsException) {
                Log.e(TAG, "Set MDF INDEX OUT OF BOUNDS EXCEPTION")
                this.validPayload = false
            } catch (castEx: ClassCastException) {
                Log.e(TAG, "CLASS CAST EXCEPTION")
                this.validPayload = false
            }
        }
    }

    private fun printMapDbg() {
        Log.d("MDF-Map", "=========================================")
        for (i in 0 until Map.ROW) {
            var s = ""
            for (j in 0 until Map.COLUMN) { s += exploredMap[j][i] }
            Log.d("MDF-Map", s)
        }
        Log.d("MDF-Map", "=========================================")
    }

    companion object {
        const val TAG = "Parser"
        val DEBUG = BuildConfig.DEBUG

        var hexMDF = "0x0000000000000000"
        var hexExplored = "0x0000000000000000"
        var hexImage = ""
        var mdfPayload = ""
    }
}