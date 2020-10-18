# Install

Please install the application before the workshop starts, because there will be no support during the workshop for the initial setup!

## Java

This application uses Java 11 to run!

## MongoDB

To run this application there must be a MongoDB 4.4 running at the host/port configured in `src/main/resources/application.yml`. 

You can either install it normally on your machine following the [official instructions](https://docs.mongodb.com/manual/installation/#mongodb-community-edition-installation-tutorials) or use the `docker-compose.yml` and run `docker-compose up`.

If you didn't use the MongoDB provided by the docker-compose, you may need to change the configuration in `src/main/resources/application.yml` for the application to work properly.

## Check installation

- Run the application (`mvn spring-boot:run` or with IDE) and navigate to <http://localhost:8080/lecture00/findAll> => **You should see a List of 5 Books**
- Run the tests (`mvn test` or with IDE) => there should be one green test

# Workshop

- The workshop is divided in lectures
- Every lecture has a branch which contains one or more tasks to complete
- During the workshop we will checkout a lecture-branch, solve the tasks and move on to the next lecture

> The master branch contains the initial lecture 00 which does not contain any tasks. lecture 00 is used to check the initial setup.

Have fun!
