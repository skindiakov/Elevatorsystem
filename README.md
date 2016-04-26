Starting up project:

To start project you need to setup scala "2.11.7" or newer and sbt.
In root directory call command "sbt" and in interactive sbt command line call "main/run"
By default Jetty server will start on http://localhost:8080/

Also Jetty can be started manually from IDE by simple running of class rest.jetty.JettyLauncher


To view REST API documentation, do the next steps:

 1. Run the application (`main/run` in SBT interactive session)
 2. Open `http://localhost:8080/elevator/api-docs` to Explore available methods and input parameters

Base description:
By default System creates haus with 20 floors and 3 elevators.
To change this setting simple call url "/elevator/haus/:floors/:elevators" for example `http://localhost:8080/elevator/haus/10/5` will create haus with 10 floors and 5 elevators

To add passenger for elevator call url /elevator/passenger/:floor/:destination"
for example `http://localhost:8080/elevator/passenger/7/2` will create passenger, who is currently on 7th floor and wants to get to 2th

To get status of all elevators call url "/elevator/status" for example `http://localhost:8080/elevator/status"
Status of all elevators will be returned in JSON format

It is possible to move system into manyal tyme steps.
To do it call "/elevator/stopTimer" after that system will not bypass time automaticaly.
Instead it will react on calls to "/elevator/step/:number" where number means on how many steps should system be moved ahead
For example call `http://localhost:8080/elevator/step/3" will make system to move three steps ahead.
