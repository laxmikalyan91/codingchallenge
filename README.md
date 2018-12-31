# Ticket Service Coding Challenge
---
Ticket Service Web application helps to facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance
venue.

* This application is developed based on Springboot, Java 1.8 , Maven ,Gson, commons-validator,lombok,slf4j,Mockito,reflections, Concurrent HashMap(In-Memory).
* Concurrent HashMap(Thread-Safe)is shared through the entire application to hold the information about the bestholdseats, reverseved seats and number of seats available.
* In realtime Scenario Database is used here to store information and acquire locks and maintain data consistency for each request.
* Because data is stored In-memory taken care of java heap memory Size by passing it through JVM args.
* This application asynchronously(Parallely) handles http requests for user to access all the 3 Endpoints.

### Assumptions
---
1) Number of Seats in a Row and in a Column is 20
2) Total Occupancy is Number of Seats in a Row * Number of Seats in a Column = 400
3) Users are provided seats based on the seats status and number of seats available.
4) Maximum BestSeatHold Time for the user is 90 seconds. If the user fail to reserve the seats with in 90 seconds then the hold on the seats is removed. seats are put back in available user has to make another request to hold the seats.
5) No notification for expiration of bestholdseats.

### Application has 3 GET Endpoints
---

```
1) http://localhost:8080/walmart/ticket-service/availableseats
```
##### This Service gives the number of available seats with in the Venue which are neither held nor resevered.

![alt text](https://github.com/laxmikalyan91/codingchallenge/blob/master/images/availableseats.png)

```
2) http://localhost:8080/walmart/ticket-service/findandholdbestseats?numOfSeats=3&customerEmail=laxmikalyan91@gmail.com
```
##### This Service can find and hold best available seats with numOfSeats and customerEmail as request parameters.

![alt text](https://github.com/laxmikalyan91/codingchallenge/blob/master/images/bestholdseat.png)

```
3) http://localhost:8080/walmart/ticket-service/reserve-seats?holdId=7&customerEmail=laxmikalyan91@gmail.com
```
##### This Service is used to reserve seats which are held with HoldId and customerEmail as request parameters.

![alt text](https://github.com/laxmikalyan91/codingchallenge/blob/master/images/ReserveSeats.png)


### 3rd party maven Dependencies and plugins:
---
```
1)Gson
```
* Gson is from google to parse Java Objects to Json and viceversa.
* source:https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.5
* Downlaod and Install the JAR into your local Maven repository as below.
* mvn install:install-file -Dfile=/Users/laxmikalyan/Downloads/gson-2.8.5.jar -DgroupId= com.google.code.gson\-DartifactId=gson -Dversion=2.8.5 -Dpackaging=jar
* Note: please change the file path.
```
2)Lombok plugin
```
* Lombok is used as a replacement of setters,getters,tostring,constructors.
* No need of writing boilerplate code lombok takes care of it.very efficient and useful makes life easy.
* source:Intellij IDEA
 
```
3)commons-validator
```
* commons-validator is to validate the Email address.
* source : https://mvnrepository.com/artifact/commons-validator/commons-validator/1.4.0
* Downlaod and Install the JAR into your local Maven repository as below.
* mvn install:install-file -Dfile=/Users/laxmikalyan/Downloads/commons-validator-1.4.0.jar -DgroupId=commons-validator \-DartifactId=commons-validator -Dversion=1.4.0 -Dpackaging=jar
* Note: please change the file path.


Building Project
---


These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

What things you need to install the software and how to install them

```
Give examples
```

### Installing

A step by step series of examples that tell you how to get a development env running

Say what the step will be

```
Give the example
```

And repeat

```
until finished
```

End with an example of getting some data out of the system or using it for a little demo

## Running the tests

Explain how to run the automated tests for this system

### Break down into end to end tests

Explain what these tests test and why

```
Give an example
```

### And coding style tests

Explain what these tests test and why

```
Give an example
```

## Deployment

Add additional notes about how to deploy this on a live system

## Built With

* [Dropwizard](http://www.dropwizard.io/1.0.2/docs/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management
* [ROME](https://rometools.github.io/rome/) - Used to generate RSS Feeds

## Contributing

Please read [CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426) for details on our code of conduct, and the process for submitting pull requests to us.

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

* **Billie Thompson** - *Initial work* - [PurpleBooth](https://github.com/PurpleBooth)

See also the list of [contributors](https://github.com/your/project/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc
