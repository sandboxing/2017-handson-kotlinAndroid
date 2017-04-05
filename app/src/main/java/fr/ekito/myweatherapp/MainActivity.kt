package fr.ekito.myweatherapp

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.joanzapata.iconify.widget.IconTextView
import fr.ekito.myweatherlibrary.WeatherSDK
import fr.ekito.myweatherlibrary.json.weather.Weather
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.content_main.weather_forecast_day1
import kotlinx.android.synthetic.main.content_main.weather_forecast_day2
import kotlinx.android.synthetic.main.content_main.weather_forecast_day3
import kotlinx.android.synthetic.main.content_main.weather_forecast_layout
import kotlinx.android.synthetic.main.content_main.weather_icon_day1
import kotlinx.android.synthetic.main.content_main.weather_icon_day2
import kotlinx.android.synthetic.main.content_main.weather_icon_day3
import kotlinx.android.synthetic.main.content_main.weather_main_icon
import kotlinx.android.synthetic.main.content_main.weather_main_layout
import kotlinx.android.synthetic.main.content_main.weather_main_text
import kotlinx.android.synthetic.main.content_main.weather_temp_day1
import kotlinx.android.synthetic.main.content_main.weather_temp_day2
import kotlinx.android.synthetic.main.content_main.weather_temp_day3
import kotlinx.android.synthetic.main.content_main.weather_title
import rx.Observer
import java.util.Date
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), MainActivityWeatherCallback {

  private val now = Date()

//  private fun findViewsById() {
//    toolbar = findViewById(R.id.toolbar) as Toolbar
//
//    title = findViewById(R.id.weather_title) as TextView
//
//    iconToday = findViewById(R.id.weather_main_icon) as IconTextView
//    forecastToday = findViewById(R.id.weather_main_text) as TextView
//
//    iconDay1 = findViewById(R.id.weather_icon_day1) as IconTextView
//    forecastDay1 = findViewById(R.id.weather_forecast_day1) as TextView
//    tempDay1 = findViewById(R.id.weather_temp_day1) as TextView
//
//    iconDay2 = findViewById(R.id.weather_icon_day2) as IconTextView
//    forecastDay2 = findViewById(R.id.weather_forecast_day2) as TextView
//    tempDay2 = findViewById(R.id.weather_temp_day2) as TextView
//
//    iconDay3 = findViewById(R.id.weather_icon_day3) as IconTextView
//    forecastDay3 = findViewById(R.id.weather_forecast_day3) as TextView
//    tempDay3 = findViewById(R.id.weather_temp_day3) as TextView
//
//    mainLayout = findViewById(R.id.weather_main_layout) as RelativeLayout
//    foreCastLayout = findViewById(R.id.weather_forecast_layout) as LinearLayout
//  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setSupportActionBar(toolbar)

    weather_main_layout.visibility = View.GONE
    weather_forecast_layout.visibility = View.GONE

    val fab = findViewById(R.id.fab) as FloatingActionButton
    fab.setOnClickListener { view -> DialogHelper.locationDialog(view, this@MainActivity) }
  }

  /**
   * Retrieve Weather Data from the REST API
   */
  override fun getWeatherData(view: View, location: String) {

    Snackbar.make(view, "Getting your weather :)", Snackbar.LENGTH_SHORT).show()

    WeatherSDK.getGeocode(location)
        .map { geocode ->
          WeatherSDKUtil.extractLocation(geocode)
        }
        .switchMap { location ->
          WeatherSDK.getWeather(location!!.lat, location.lng)
        }
        .timeout(40, TimeUnit.SECONDS).subscribe(object : Observer<Weather> {
      override fun onCompleted() {}

      override fun onError(error: Throwable) {
        Snackbar.make(view, "Weather Error : " + error, Snackbar.LENGTH_LONG).show()
      }

      override fun onNext(weather: Weather) {
        updateWeatherUI(weather, location)
      }
    })
  }

  fun updateWeatherUI(weather: Weather?, location: String) {

    if (weather != null) {
      weather_main_layout.visibility = View.VISIBLE
      weather_forecast_layout.visibility = View.VISIBLE

      val timeFormat = android.text.format.DateFormat.getTimeFormat(
          MainApplication.get())
      val dateFormat = android.text.format.DateFormat.getDateFormat(
          MainApplication.get())
      weather_title.text = getString(
          R.string.weather_title) + " " + location + "\n" + dateFormat.format(
          now) + " " + timeFormat.format(now)

      val forecasts = WeatherSDKUtil.getDailyForecasts(weather)

      if (forecasts.size == 4) {
        setForecastForToday(forecasts[0], weather_main_text, weather_main_icon)
        setForecastForDayX(forecasts[1], 1, weather_forecast_day1, weather_icon_day1,
            weather_temp_day1)
        setForecastForDayX(forecasts[2], 2, weather_forecast_day2, weather_icon_day2,
            weather_temp_day2)
        setForecastForDayX(forecasts[3], 3, weather_forecast_day3, weather_icon_day3,
            weather_temp_day3)
      }
    }
  }

  private fun setForecastForToday(dayX: DailyForecastModel, forecastDayX: TextView,
      iconDayX: IconTextView) {
    forecastDayX.text = "Today : " + dayX.forecast + "\n" + dayX.temperatureString
    iconDayX.text = dayX.icon
  }

  private fun setForecastForDayX(dayX: DailyForecastModel, idx: Int, forecastDayX: TextView,
      iconDayX: IconTextView, tempDayX: TextView?) {
    forecastDayX.text = "Day " + idx + ": " + dayX.forecast
    if (tempDayX != null) {
      tempDayX.text = dayX.temperatureString
    }
    iconDayX.text = dayX.icon
  }
}
