# Ticket Service Coding Challenge
---
Ticket Service web application helps to facilitates the discovery, temporary hold, and final reservation of seats within a high-demand performance
venue.

* This application is developed based on springboot, Java 1.8 , Maven, Gson, commons-validator,lombok,slf4j,Mockito,reflections, Concurrent HashMap(In-Memory).
* Concurrent HashMap(Thread-Safe)is shared through the entire application to hold the information about the bestholdseats, reserved seats and number of seats available.
* In realtime Scenario Database is used here to store information, acquire locks(Lock Based Protocol) on database for concurrency control to preserve and maintain data consistency for each request.
* Because data is stored In-memory taken care of java heap memory size by passing it as options to Java Runtime Environment(JRE).
* This application handles asynchronously(Parallel) http requests from user to access all the 3 Endpoints.

### Assumptions
---
1) Number of seats in a row and in a column is 20
2) Total occupancy is number of seats in a row * number of seats in a column = 400
3) Users are provided seats based on the seats status and number of seats available.
4) Maximum BestSeatHold Time for the user is 90 seconds. If the user fail to reserve the seats with in 90 seconds then the hold on the seats is removed. seats are put back in available user has to make another request to hold the seats.
5) No notification for expiration of bestholdseats.


Building and Running the Walmart Ticket Service Application
---
1. Clone the project and change directory to codingchallenge
```
git clone https://github.com/laxmikalyan91/codingchallenge.git
cd codingchallenge
```
2. I am using Mac here.Make sure homebrew is installed first if not please use the below command in the terminal.
HomeBrew helps in install the stuff we need that apple didn’t provide.
```
ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```
3. If Homebrew is already installed ignore step 2.Make Sure Maven is Installed in your Machine.If not installed please use the command below to install
```
brew install maven
```
4. Build the Project before you run use the below commands to do so.
* To run the build with tests use 1st command
* To run the build skipping tests use 2nd command.
```
mvn clean install
mvn clean install -DskipTests
```
##### If you encounter any build failures for 3rd party maven Dependencies and plugin missing please follow the below steps explained in detail with commands.

### Download and Install 3rd party maven Dependencies and plugins:
---
```
1)Gson
```
* Gson is from google to parse Java Objects to Json and viceversa.
* source:https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.5
* Download and Install the JAR into your local Maven repository as below.
* mvn install:install-file -Dfile=/Users/laxmikalyan/Downloads/gson-2.8.5.jar -DgroupId= com.google.code.gson\-DartifactId=gson -Dversion=2.8.5 -Dpackaging=jar
* Note: please change the file path.
```
2)Lombok plugin
```
* Lombok is used as a replacement of setters,getters,tostring,constructors.
* No need of writing boilerplate code lombok takes care of it.very efficient and useful makes life easy.
* source: Intellij IDEA
 
```
3)commons-validator
```
* commons-validator is to validate the Email address.
* source : https://mvnrepository.com/artifact/commons-validator/commons-validator/1.4.0
* Download and Install the JAR into your local Maven repository as below.
* mvn install:install-file -Dfile=/Users/laxmikalyan/Downloads/commons-validator-1.4.0.jar -DgroupId=commons-validator \-DartifactId=commons-validator -Dversion=1.4.0 -Dpackaging=jar
* Note: please change the file path.

5. Once all tests are passed and build is success.Change directory to target and use the below command to execute the Jar.
![alt text](https://github.com/laxmikalyan91/codingchallenge/blob/master/images/Build_Success.png)
```
cd target
java -Xmx1024m -Xms256m -jar ticketservice-0.0.1-SNAPSHOT.jar
```
* -Xmx --> Max Heap Size
* -Xms --> Min Heap Size

6. After running the above command wait for Walmart Ticket Service Application to start on port 8080 you should see the below

![alttext] (https://github.com/laxmikalyan91/codingchallenge/blob/master/images/Walmart%20Ticket%20Service%20Application%20Start.png)

7.There are two ways you can run this project 
* Command line using Curl 
* Using an IDE. I used Intellij Idea here.
  
8.  Now you can test the endpoints.There are two ways you can test this project. 
* Using postman
* Command line using Curl

#### I have guided both ways in detail with commands and screenshots attached.please follow the below steps:

### Test Through Postman
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

### Test through Curl in Terminal
---
If is curl not installed on your machine please follow the below step in terminal to install.

```
brew install curl
```
```
1) curl -X GET http://localhost:8080/walmart/ticket-service/availableseats
```
![alt text](https://github.com/laxmikalyan91/codingchallenge/blob/master/images/availableseats_curl.png)
```
2) curl -X GET 'http://localhost:8080/walmart/ticket-service/findandholdbestseats?numOfSeats=4&customerEmail=laxmikalyan91@gmail.com'
```
![alt text](https://github.com/laxmikalyan91/codingchallenge/blob/master/images/bestholdseat_curl.png)
```
3) curl -X GET 'http://localhost:8080/walmart/ticket-service/reserve-seats?holdId=10&customerEmail=laxmikalyan91@gmail.com'
```
![alt text](https://github.com/laxmikalyan91/codingchallenge/blob/master/images/reseredseats_curl.png)

