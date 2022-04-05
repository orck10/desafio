# Challenge

#### About
This challenge have focus on development of an application who manage teams and users. This users have a role who associate them with the team. Teams need have a lead who are a user.
For that was developed the endpoints who do this work. If your application is running you can see this endpoints on your host follow by "/swagger-ui/index.html".

eg:


- http://localhost:8080/swagger-ui/index.html

With these and points you can send the massive data of users and teams, or only one. To save the data I used the H2 database, and to log the data alteration on transactions MongoDB

#### Run tests
To run tests you must have installed maven, docker and Docker Composer, because you will need have the mongo instance runing. To do this open de "docker" directory and run the follow command:

- docker-compose -f startDB.yml up -d

Before rum testes need comment the line 6 and uncomment line 5 of main properties.

After that, you are able to run the tests with the follow command:

- mvn clean install

#### Run app
Before run the app need comment the line 5 and uncomment line 6 of main properties.
To run this application with the both databases you must have installed Docker and Docker Composer. With docker installed you can open the "docker" directory with "start.yml" file, after that you can run the follow command:

- docker-compose -f start.yml up -d

This will spend some minutes, because the Maven will do the download of the dependencies. After conclude you can use the app.

To see the MongoDB data, you need a client.
To access the data of H2 DB you can use it in the you browser.
