Popular Movies
================

Popular Movies is an Android app built for the Udacity course [Developing Android Apps: Android Fundamentals](https://www.udacity.com/course/ud853).
It shows data on popular movies from themoviedb.org.   

In order to get this project  to build and run, you'll need an API key from themoviedb.org.   

First, sign up to themoviedb.org
https://www.themoviedb.org/account/signup

Once signed  in, you'll get to the main page
https://www.themoviedb.org/account/[your account name]

Chose API from the menu on the left.  Drill down to create an API key.   

Next, add  your API key to the code by adding the following to your ~/.gradle/gradle.properties file:
  
MyTheMovieDbApiKey="[Your API key]"

In your build.gradle  (Module: app) these lines will reference that key:

buildTypes.each{
     it.buildConfigField 'String', 'THE_MOVIE_DB_API_KEY', MyTheMovieDbApiKey
}






