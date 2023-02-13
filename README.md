# asteroid-scanner
Tool to check for asteroids approaching earth so that we can send up Bruce Willis to blow it up.
It uses Nasa's public 'Near Earth Object' API - found on https://api.nasa.gov
First an initial query is done on the 'Neo - Feed' API to get a list of IDs and names of asteroids near to earth. 
Secondly full information is retrieved for all the asteroid IDs retreived, in separate rest queries (Neo - Lookup).

There's two application classes - App doing main initialization and the first Feed query, which it gives to the ApprachDetector.
This one iterates through the list and retrieves fly-by data (distance, date, speed etc). 
The 10 closest fly-byes are shown in the terminal.
In the .../model directory there's 8 datamodel classes, however theses are simple POJOs with no logic, should be self-explained.

The app works with default credentials, but will be significantly throttled. If you do repeated executions, we recommend signing up for an API key at https://api.nasa.gov, and setting the environment variable "API_KEY" to this value.

## Build
Requires gradle 7.6. Also used Java 17 to write this not sure about downward compatibility :)
```
./gradlew build
```

## Run
```
./gradlew run 
```

### Filter the 10 closest passings the coming week
```
--args="filterCloseApproachesThisWek"
```
