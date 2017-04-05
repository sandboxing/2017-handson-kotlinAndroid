package fr.ekito.myweatherlibrary.json.geocode

/**
 * Created by benoit-quenaudon on 4/5/17.
 */
fun Geocode.getLocation(): Location? {
  return results.firstOrNull()?.geometry?.location
}