#/bin/bash

curl "https://my-weather-api.herokuapp.com/geocode?address=Paris,FR" > ./src/main/assets/json/geocode_paris.json;
curl "https://my-weather-api.herokuapp.com/weather?lat=48.856614&lon=2.3522219&lang=EN" > ./src/main/assets/json/weather_paris.json;

curl "https://my-weather-api.herokuapp.com/geocode?address=Toulouse,FR" > ./src/main/assets/json/geocode_toulouse.json;
curl "https://my-weather-api.herokuapp.com/weather?lat=43.604652&lon=1.444209&lang=EN" > ./src/main/assets/json/weather_toulouse.json;

curl "https://my-weather-api.herokuapp.com/geocode?address=London,UK" > ./src/main/assets/json/geocode_london.json;
curl "https://my-weather-api.herokuapp.com/weather?lat=51.5073509&lon=-0.1277583&lang=EN" > ./src/main/assets/json/weather_london.json;

curl "https://my-weather-api.herokuapp.com/geocode?address=Berlin,DE" > ./src/main/assets/json/geocode_berlin.json;
curl "https://my-weather-api.herokuapp.com/weather?lat=52.52000659999999&lon=13.404954&lang=EN" > ./src/main/assets/json/weather_berlin.json;

curl "https://my-weather-api.herokuapp.com/geocode?address=Madrid,ES" > ./src/main/assets/json/geocode_madrid.json;
curl "https://my-weather-api.herokuapp.com/weather?lat=40.4167754&lon=-3.7037902&lang=EN" > ./src/main/assets/json/weather_madrid.json;