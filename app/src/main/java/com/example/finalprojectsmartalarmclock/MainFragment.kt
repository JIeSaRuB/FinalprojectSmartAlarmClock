package com.example.finalprojectsmartalarmclock

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalprojectsmartalarmclock.databinding.FragmentMainBinding
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

class MainFragment : Fragment() {
    private var forecastList: RecyclerView? = null
    private var forecastArray: ArrayList<ForecastItem> = ArrayList()
    var locationManager: LocationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val binding = FragmentMainBinding.inflate(layoutInflater)
        //Дата и время
        val timer = object: CountDownTimer(1000,1000){
            override fun onTick(p0: Long) {
                binding.currentTime.text = SimpleDateFormat("HH:mm:ss").format(Date())
            }
            override fun onFinish() {
                this.start()
            }
        }
        binding.currentDate.text = SimpleDateFormat("dd.MM.yyyy").format(Date())
        timer.start()
        //Погода
        forecastList = binding.forecast
        val forecastAdapter = ForecastAdapter(forecastArray)
        val forecastLM = LinearLayoutManager(requireContext())
        forecastList?.layoutManager = forecastLM
        forecastList?.adapter = forecastAdapter

        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkPermission()
    }
    fun checkPermission() {
        if(ActivityCompat.checkSelfPermission(requireContext(), ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(requireContext(), ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(ACCESS_FINE_LOCATION), 101)
        } else {
            requestLocation()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            101 -> {
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation()
                } else {
                    Toast.makeText(requireContext(), resources.getString(R.string.warning), Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
    fun requestLocation() {
        try {
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, locationListener)
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), resources.getString(R.string.exception), Toast.LENGTH_LONG).show()
        }
    }
    private val locationListener: LocationListener = LocationListener { location -> getForecast(location.latitude.toString(), location.longitude.toString()) }
    private fun getForecast(latitude: String, longitude: String) {
        Thread {
            try {
                val forecastData = URL("https://api.open-meteo.com/v1/forecast?latitude=$latitude&longitude=$longitude&hourly=temperature_2m,weathercode")
                    .readText(Charsets.UTF_8)
                val hourly = JSONObject(forecastData).getJSONObject("hourly")
                for(i in 0 until hourly.getJSONArray("time").length()) {
                    forecastArray.add(
                        ForecastItem(
                            hourly.getJSONArray("time").getString(i).split("T")[0],
                            hourly.getJSONArray("time").getString(i).split("T")[1],
                            weatherCodeToWeather(hourly.getJSONArray("weathercode").getInt(i)),
                            hourly.getJSONArray("temperature_2m").getDouble(i).roundToInt(),
                        )
                    )
                }
                requireActivity().runOnUiThread { forecastList?.adapter?.notifyDataSetChanged()}
            } catch (e: InterruptedException) {
                Log.e("TAG", e.message.toString())
            }
        }.start()
    }
    fun weatherCodeToWeather(code: Int): String {
        val weather = when(code) {
            0 -> "Ясно"
            in 1..2 -> "Переменная облачность"
            45 -> "Туман"
            48 -> "Изморозь"
            in 51..55 -> "Морось"
            in 56..57 -> "Ледяная морось"
            in 61..65 -> "Дождь"
            in 71..77 -> "Снег"
            in 80..82 -> "Ливневый дождь"
            in 90..99 -> "Гроза"
            else -> "Облачно"
        }

        return weather
    }


    fun getCurrentDate(): String{
        val ctime = SimpleDateFormat("HH:mm:ss\n")
        return ctime.format(Date())
    }

}