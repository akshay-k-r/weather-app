package com.example.krishiweather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class Forecast : AppCompatActivity() {

    val PERMISSION_ID = 42
    var LAT = "12.97"
    var LON = "77.59"

    private lateinit var mFusedLocationClient: FusedLocationProviderClient


    val API: String = "ce146299911d327964c4682ae4aa1198"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        setContentView(R.layout.activity_forecast)

    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(): Location? {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val gps = DoubleArray(2)
                        LAT = location.latitude.toString()
                        LON = location.longitude.toString()
                        forecastTask().execute()
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }

        return null
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            LAT = mLastLocation.latitude.toString()
            LON = mLastLocation.longitude.toString()





        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }















    inner class forecastTask : AsyncTask<String , Void , String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            /* Showing the ProgressBar, Making the main design GONE */

            findViewById<TextView>(R.id.forecastErrorText).visibility = View.GONE
        }

        override fun doInBackground(vararg params: String?): String? {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/forecast?lat=$LAT&lon=$LON&units=metric&appid=$API").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response
        }

        @SuppressLint("SetTextI18n")
        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)
                val weatherList = jsonObj.getJSONArray("list")
                when {
                    weatherList.getJSONObject(0).getString("dt_txt").contains("00:00:00") -> {
                        /** Day 1 Data**/
                        /** Day 1 Data**/
                        findViewById<TextView>(R.id.day0Morning).text = "Morning" + weatherList.getJSONObject(3).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Afternoon).text = "Afternoon" + weatherList.getJSONObject(4).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Evening).text = "Evening" + weatherList.getJSONObject(6).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Night).text = "Night" + weatherList.getJSONObject(7).getJSONObject("main").getString("temp")
                        /** Day 2 Data**/
                        /** Day 2 Data**/
                        findViewById<TextView>(R.id.day1Morning).text = "Morning" + weatherList.getJSONObject(11).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Afternoon).text = "Afternoon" + weatherList.getJSONObject(12).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Evening).text = "Evening" + weatherList.getJSONObject(14).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Night).text = "Night" + weatherList.getJSONObject(18).getJSONObject("main").getString("temp")
                        /** Day 3 Data**/
                        /** Day 3 Data**/
                        findViewById<TextView>(R.id.day2Morning).text = "Morning" + weatherList.getJSONObject(19).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Afternoon).text = "Afternoon" + weatherList.getJSONObject(20).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Evening).text = "Evening" + weatherList.getJSONObject(22).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Night).text = "Night" + weatherList.getJSONObject(23).getJSONObject("main").getString("temp")
                        /** Day 4 Data**/
                        /** Day 4 Data**/
                        findViewById<TextView>(R.id.day3Morning).text = "Morning" + weatherList.getJSONObject(27).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Afternoon).text = "Afternoon" + weatherList.getJSONObject(28).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Evening).text = "Evening" + weatherList.getJSONObject(30).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Night).text = "Night" + weatherList.getJSONObject(31).getJSONObject("main").getString("temp")
                        /** Day 5 Data**/
                        /** Day 5 Data**/
                        findViewById<TextView>(R.id.day4Morning).text = "Morning" + weatherList.getJSONObject(35).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Afternoon).text = "Afternoon" + weatherList.getJSONObject(36).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Evening).text = "Evening" + weatherList.getJSONObject(38).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Night).text = "Night" + weatherList.getJSONObject(39).getJSONObject("main").getString("temp")

                    }
                    weatherList.getJSONObject(0).getString("dt_txt").contains("03:00:00") -> {
                        /** Day 1 Data**/
                        /** Day 1 Data**/
                        findViewById<TextView>(R.id.day0Morning).text = "Morning " + weatherList.getJSONObject(2).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Afternoon).text = "Afternoon " + weatherList.getJSONObject(3).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Evening).text = "Evening " + weatherList.getJSONObject(5).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Night).text = "Night " + weatherList.getJSONObject(6).getJSONObject("main").getString("temp")
                        /** Day 2 Data**/
                        /** Day 2 Data**/
                        findViewById<TextView>(R.id.day1Morning).text = "Morning " + weatherList.getJSONObject(10).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Afternoon).text = "Afternoon " + weatherList.getJSONObject(11).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Evening).text = "Evening " + weatherList.getJSONObject(13).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Night).text = "Night " + weatherList.getJSONObject(14).getJSONObject("main").getString("temp")
                        /** Day 3 Data**/
                        /** Day 3 Data**/
                        findViewById<TextView>(R.id.day2Morning).text = "Morning " + weatherList.getJSONObject(18).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Afternoon).text = "Afternoon " + weatherList.getJSONObject(19).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Evening).text = "Evening " + weatherList.getJSONObject(21).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Night).text = "Night " + weatherList.getJSONObject(22).getJSONObject("main").getString("temp")
                        /** Day 4 Data**/
                        /** Day 4 Data**/
                        findViewById<TextView>(R.id.day3Morning).text = "Morning " + weatherList.getJSONObject(26).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Afternoon).text = "Afternoon " + weatherList.getJSONObject(27).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Evening).text = "Evening " + weatherList.getJSONObject(29).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Night).text = "Night " + weatherList.getJSONObject(30).getJSONObject("main").getString("temp")
                        /** Day 5 Data**/
                        /** Day 5 Data**/
                        findViewById<TextView>(R.id.day4Morning).text = "Morning " + weatherList.getJSONObject(34).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Afternoon).text = "Afternoon " + weatherList.getJSONObject(35).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Evening).text = "Evening " + weatherList.getJSONObject(37).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Night).text = "Night " + weatherList.getJSONObject(38).getJSONObject("main").getString("temp")
                    }
                    weatherList.getJSONObject(0).getString("dt_txt").contains("06:00:00") -> {
                        /** Day 1 Data**/
                        /** Day 1 Data**/
                        findViewById<TextView>(R.id.day0Morning).text = "Morning " + weatherList.getJSONObject(1).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Afternoon).text = "Afternoon " + weatherList.getJSONObject(2).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Evening).text = "Evening " + weatherList.getJSONObject(4).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Night).text = "Night " + weatherList.getJSONObject(5).getJSONObject("main").getString("temp")
                        /** Day 2 Data**/
                        /** Day 2 Data**/
                        findViewById<TextView>(R.id.day1Morning).text = "Morning " + weatherList.getJSONObject(8).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Afternoon).text = "Afternoon " + weatherList.getJSONObject(9).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Evening).text = "Evening " + weatherList.getJSONObject(11).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Night).text = "Night " + weatherList.getJSONObject(12).getJSONObject("main").getString("temp")
                        /** Day 3 Data**/
                        /** Day 3 Data**/
                        findViewById<TextView>(R.id.day2Morning).text = "Morning " + weatherList.getJSONObject(16).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Afternoon).text = "Afternoon " + weatherList.getJSONObject(17).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Evening).text = "Evening " + weatherList.getJSONObject(19).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Night).text = "Night " + weatherList.getJSONObject(20).getJSONObject("main").getString("temp")
                        /** Day 4 Data**/
                        /** Day 4 Data**/
                        findViewById<TextView>(R.id.day3Morning).text = "Morning " + weatherList.getJSONObject(24).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Afternoon).text = "Afternoon " + weatherList.getJSONObject(25).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Evening).text = "Evening " + weatherList.getJSONObject(27).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Night).text = "Night " + weatherList.getJSONObject(28).getJSONObject("main").getString("temp")
                        /** Day 5 Data**/
                        /** Day 5 Data**/
                        findViewById<TextView>(R.id.day4Morning).text = "Morning " + weatherList.getJSONObject(32).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Afternoon).text = "Afternoon " + weatherList.getJSONObject(33).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Evening).text = "Evening " + weatherList.getJSONObject(35).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Night).text = "Night " + weatherList.getJSONObject(36).getJSONObject("main").getString("temp")
                    }
                    weatherList.getJSONObject(0).getString("dt_txt").contains("09:00:00") -> {
                        /** Day 1 Data**/
                        /** Day 1 Data**/
                        findViewById<TextView>(R.id.day0Morning).text = "Morning " + weatherList.getJSONObject(0).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Afternoon).text = "Afternoon " + weatherList.getJSONObject(1).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Evening).text = "Evening " + weatherList.getJSONObject(3).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Night).text = "Night " + weatherList.getJSONObject(4).getJSONObject("main").getString("temp")
                        /** Day 2 Data**/
                        /** Day 2 Data**/
                        findViewById<TextView>(R.id.day1Morning).text = "Morning " + weatherList.getJSONObject(8).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Afternoon).text = "Afternoon " + weatherList.getJSONObject(9).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Evening).text = "Evening " + weatherList.getJSONObject(11).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Night).text = "Night " + weatherList.getJSONObject(12).getJSONObject("main").getString("temp")
                        /** Day 3 Data**/
                        /** Day 3 Data**/
                        findViewById<TextView>(R.id.day2Morning).text = "Morning " + weatherList.getJSONObject(16).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Afternoon).text = "Afternoon " + weatherList.getJSONObject(17).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Evening).text = "Evening " + weatherList.getJSONObject(19).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Night).text = "Night " + weatherList.getJSONObject(20).getJSONObject("main").getString("temp")
                        /** Day 4 Data**/
                        /** Day 4 Data**/
                        findViewById<TextView>(R.id.day3Morning).text = "Morning " + weatherList.getJSONObject(24).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Afternoon).text = "Afternoon " + weatherList.getJSONObject(25).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Evening).text = "Evening " + weatherList.getJSONObject(27).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Night).text = "Night " + weatherList.getJSONObject(28).getJSONObject("main").getString("temp")
                        /** Day 5 Data**/
                        /** Day 5 Data**/
                        findViewById<TextView>(R.id.day4Morning).text = "Morning " + weatherList.getJSONObject(32).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Afternoon).text = "Afternoon " + weatherList.getJSONObject(33).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Evening).text = "Evening " + weatherList.getJSONObject(35).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Night).text = "Night " + weatherList.getJSONObject(36).getJSONObject("main").getString("temp")

                    }
                    weatherList.getJSONObject(0).getString("dt_txt").contains("12:00:00") -> {
                        /** Day 1 Data**/
                        /** Day 1 Data**/
                        findViewById<TextView>(R.id.day0Morning).text = "Morning " + "N/A"
                        findViewById<TextView>(R.id.day0Afternoon).text = "Afternoon " + weatherList.getJSONObject(0).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Evening).text = "Evening " + weatherList.getJSONObject(2).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Night).text = "Night " + weatherList.getJSONObject(3).getJSONObject("main").getString("temp")
                        /** Day 2 Data**/
                        /** Day 2 Data**/
                        findViewById<TextView>(R.id.day1Morning).text = "Morning " + weatherList.getJSONObject(7).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Afternoon).text = "Afternoon " + weatherList.getJSONObject(8).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Evening).text = "Evening " + weatherList.getJSONObject(10).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Night).text = "Night " + weatherList.getJSONObject(11).getJSONObject("main").getString("temp")
                        /** Day 3 Data**/
                        /** Day 3 Data**/
                        findViewById<TextView>(R.id.day2Morning).text = "Morning " + weatherList.getJSONObject(15).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Afternoon).text = "Afternoon " + weatherList.getJSONObject(16).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Evening).text = "Evening " + weatherList.getJSONObject(18).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Night).text = "Night " + weatherList.getJSONObject(19).getJSONObject("main").getString("temp")
                        /** Day 4 Data**/
                        /** Day 4 Data**/
                        findViewById<TextView>(R.id.day3Morning).text = "Morning " + weatherList.getJSONObject(23).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Afternoon).text = "Afternoon " + weatherList.getJSONObject(24).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Evening).text = "Evening " + weatherList.getJSONObject(26).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Night).text = "Night " + weatherList.getJSONObject(27).getJSONObject("main").getString("temp")
                        /** Day 5 Data**/
                        /** Day 5 Data**/
                        findViewById<TextView>(R.id.day4Morning).text = "Morning " + weatherList.getJSONObject(31).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Afternoon).text = "Afternoon " + weatherList.getJSONObject(32).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Evening).text = "Evening " + weatherList.getJSONObject(34).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Night).text = "Night " + weatherList.getJSONObject(35).getJSONObject("main").getString("temp")
                    }
                    weatherList.getJSONObject(0).getString("dt_txt").contains("15:00:00") -> {
                        /** Day 1 Data**/
                        /** Day 1 Data**/
                        findViewById<TextView>(R.id.day0Morning).text = "Morning " + "N/A"
                        findViewById<TextView>(R.id.day0Afternoon).text = "Afternoon " + "N/A"
                        findViewById<TextView>(R.id.day0Evening).text = "Evening " + weatherList.getJSONObject(1).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Night).text = "Night " + weatherList.getJSONObject(2).getJSONObject("main").getString("temp")
                        /** Day 2 Data**/
                        /** Day 2 Data**/
                        findViewById<TextView>(R.id.day1Morning).text = "Morning " + weatherList.getJSONObject(6).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Afternoon).text = "Afternoon " + weatherList.getJSONObject(7).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Evening).text = "Evening " + weatherList.getJSONObject(9).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Night).text = "Night " + weatherList.getJSONObject(10).getJSONObject("main").getString("temp")
                        /** Day 3 Data**/
                        /** Day 3 Data**/
                        findViewById<TextView>(R.id.day2Morning).text = "Morning " + weatherList.getJSONObject(14).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Afternoon).text = "Afternoon " + weatherList.getJSONObject(15).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Evening).text = "Evening " + weatherList.getJSONObject(17).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Night).text = "Night " + weatherList.getJSONObject(18).getJSONObject("main").getString("temp")
                        /** Day 4 Data**/
                        /** Day 4 Data**/
                        findViewById<TextView>(R.id.day3Morning).text = "Morning " + weatherList.getJSONObject(22).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Afternoon).text = "Afternoon " + weatherList.getJSONObject(23).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Evening).text = "Evening " + weatherList.getJSONObject(25).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Night).text = "Night " + weatherList.getJSONObject(26).getJSONObject("main").getString("temp")
                        /** Day 5 Data**/
                        /** Day 5 Data**/
                        findViewById<TextView>(R.id.day4Morning).text = "Morning " + weatherList.getJSONObject(30).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Afternoon).text = "Afternoon " + weatherList.getJSONObject(31).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Evening).text = "Evening " + weatherList.getJSONObject(33).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Night).text = "Night " + weatherList.getJSONObject(34).getJSONObject("main").getString("temp")
                    }
                    weatherList.getJSONObject(0).getString("dt_txt").contains("18:00:00") -> {
                        /** Day 1 Data**/
                        /** Day 1 Data**/
                        findViewById<TextView>(R.id.day0Morning).text = "Morning " + "N/A"
                        findViewById<TextView>(R.id.day0Afternoon).text = "Afternoon " + "N/A"
                        findViewById<TextView>(R.id.day0Evening).text = "Evening " + weatherList.getJSONObject(0).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day0Night).text = "Night " + weatherList.getJSONObject(1).getJSONObject("main").getString("temp")
                        /** Day 2 Data**/
                        /** Day 2 Data**/
                        findViewById<TextView>(R.id.day1Morning).text = "Morning " + weatherList.getJSONObject(5).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Afternoon).text = "Afternoon " + weatherList.getJSONObject(6).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Evening).text = "Evening " + weatherList.getJSONObject(8).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Night).text = "Night " + weatherList.getJSONObject(9).getJSONObject("main").getString("temp")
                        /** Day 3 Data**/
                        /** Day 3 Data**/
                        findViewById<TextView>(R.id.day2Morning).text = "Morning " + weatherList.getJSONObject(13).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Afternoon).text = "Afternoon " + weatherList.getJSONObject(14).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Evening).text = "Evening " + weatherList.getJSONObject(16).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Night).text = "Night " + weatherList.getJSONObject(17).getJSONObject("main").getString("temp")
                        /** Day 4 Data**/
                        /** Day 4 Data**/
                        findViewById<TextView>(R.id.day3Morning).text = "Morning " + weatherList.getJSONObject(21).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Afternoon).text = "Afternoon " + weatherList.getJSONObject(22).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Evening).text = "Evening " + weatherList.getJSONObject(24).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Night).text = "Night " + weatherList.getJSONObject(25).getJSONObject("main").getString("temp")
                        /** Day 5 Data**/
                        /** Day 5 Data**/
                        findViewById<TextView>(R.id.day4Morning).text = "Morning " + weatherList.getJSONObject(29).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Afternoon).text = "Afternoon " + weatherList.getJSONObject(30).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Evening).text = "Evening " + weatherList.getJSONObject(32).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Night).text = "Night " + weatherList.getJSONObject(33).getJSONObject("main").getString("temp")
                    }
                    weatherList.getJSONObject(0).getString("dt_txt").contains("21:00:00") -> {
                        /** Day 1 Data**/
                        /** Day 1 Data**/
                        findViewById<TextView>(R.id.day0Morning).text = "Morning " + "N/A"
                        findViewById<TextView>(R.id.day0Afternoon).text = "Afternoon " + "N/A"
                        findViewById<TextView>(R.id.day0Evening).text = "Evening " + "N/A"
                        findViewById<TextView>(R.id.day0Night).text = "Night " + weatherList.getJSONObject(0).getJSONObject("main").getString("temp")
                        /** Day 2 Data**/
                        /** Day 2 Data**/
                        findViewById<TextView>(R.id.day1Morning).text = "Morning " + weatherList.getJSONObject(4).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Afternoon).text = "Afternoon " + weatherList.getJSONObject(5).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Evening).text = "Evening " + weatherList.getJSONObject(7).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day1Night).text = "Night " + weatherList.getJSONObject(8).getJSONObject("main").getString("temp")
                        /** Day 3 Data**/
                        /** Day 3 Data**/
                        findViewById<TextView>(R.id.day2Morning).text = "Morning " + weatherList.getJSONObject(12).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Afternoon).text = "Afternoon " + weatherList.getJSONObject(13).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Evening).text = "Evening " + weatherList.getJSONObject(15).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day2Night).text = "Night " + weatherList.getJSONObject(16).getJSONObject("main").getString("temp")
                        /** Day 4 Data**/
                        /** Day 4 Data**/
                        findViewById<TextView>(R.id.day3Morning).text = "Morning " + weatherList.getJSONObject(20).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Afternoon).text = "Afternoon " + weatherList.getJSONObject(21).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Evening).text = "Evening " + weatherList.getJSONObject(23).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day3Night).text = "Night " + weatherList.getJSONObject(24).getJSONObject("main").getString("temp")
                        /** Day 5 Data**/
                        /** Day 5 Data**/
                        findViewById<TextView>(R.id.day4Morning).text = "Morning " + weatherList.getJSONObject(28).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Afternoon).text = "Afternoon " + weatherList.getJSONObject(29).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Evening).text = "Evening " + weatherList.getJSONObject(31).getJSONObject("main").getString("temp")
                        findViewById<TextView>(R.id.day4Night).text = "Night " + weatherList.getJSONObject(32).getJSONObject("main").getString("temp")

                    }
                }
                

            } catch (e: Exception) {

                findViewById<TextView>(R.id.forecastErrorText).visibility = View.VISIBLE
                """findViewById<TextView>(R.id.latitude).visibility =View.VISIBLE
                findViewById<TextView>(R.id.latitude).visibility =View.VISIBLE"""
            }

        }
    }












}