# Tourist Guides App
## Description
**Tourist Guides App** is a full-stack web application designed to manage both the members of the **Association of Licensed Tourist Guides** as well as non-members who want to have access to a legitimate list of licensed tourist guides to choose from.

Tourist guides-members can register to the app and are automatically added to the *List of Licensed Tourist Guides* and their personal profile is accessible to all other users of the app. They have also access to a list of activities organised by the association and can add the ones they like to their *Favorites*. Non-members can register to the app and have access to the *List of Licensed Tourist Guides* as well as to the personal profiles of each tourist guide.

It features **JWT-based Authentication**, **role-based Authorization**, **REST API**, **Swagger UI** for API documentation and **Responsive design** for a consistent and optimal user experience across various screen sizes and devices (desktops, tablets, mobile phones).

![View of the frontend in different screen sizes](/assets/images/responsive-mockup.jpg)

## Roles and their capabilities
**Guide**
- Registers to the app and manages his account (update/delete) and the profile shown to the other users of the app. With his registration he is automatically added to the *List of Licensed Tourist Guides* and his profile is accessible from the other users of the app.
- Adds the activities he likes to his *Favorites* or removes them.
- Has access to:
  - the *List of Licensed Tourist Guides* where he can search for one, more or all the guides using simple or complex filtering criteria
  - the profile of each guide
  - the *List of Activities* where he can search for one, more or all the activities using simple or complex filtering criteria
  - the profile of each activity
  - his *Favorites* list

**Visitor**
- Registers to the app and manages his/her account (update/delete).
- Has access to:
  - the *List of Licensed Tourist Guides* where he can search for one, more or all the guides using simple or complex filtering criteria
  - the profile of each guide

**Super Admin**
- Updates/deletes existing guides and existing visitors
- Inserts/updates/deletes activities and data from the static tables.
- Has access to:
  - the *List of Licensed Tourist Guides* where he can search for one, more or all the guides using simple or complex filtering criteria
  - the profile of each guide
  - the *List of Visitors* where he can search for one, more or all the visitors using simple or complex filtering criteria
  - the *List of Activities* where he can search for one, more or all the activities using simple or complex filtering criteria
  - the profile of each activity
 
![Snapshots of the frontend](/assets/images/snapshots.jpg)
 
## Technologies Used
**Backend**
- Java 17
- Gradle 8.14.3
- Spring Boot 3.5.4
- Spring Web
- Spring Security
- Spring Data JPA
- Validation
- Lombok
- MySQL
- Swagger (OpenAPI)

**Frontend**
- Angular CLI 19.1.5
- Bootstrap and custom CSS

## Run the application locally via CLI
**Clone the repository**

- `git clone git@github.com:antoiosif/tourist-guides-app.git`

**Setup the DB**

- Create a DB in MySQL with the name `touristguidesdb`
- Create a user with name `touristguidesuser` and password `12345` and grant all privileges
- If you want to change the above settings you must change them also in the file `application-test.properties`
  
**Backend**
  
- `cd touristguidesapp` to navigate to the backend directory
- `./gradlew bootRun` to run backend for the schema to be created
- `Ctrl + C` to stop running
- `cd src/main/resources` and edit the file `application-test.properties` according to the instructions (uncomment to create the indexes, populate the static data tables, create the "SUPER_ADMIN" and populate the rest tables with dummy data)
- `./gradlew bootRun` to run again
- API is available at `http://localhost:8080`

**Frontend**

- `cd ../../../../tourist-guides-app-frontend` to navigate to the frontend directory
- `ng serve` to run
- In your browser navigate to `http://localhost:4200/`

## Build
**Backend**
  - BUILD -> `gradle clean build`
  - RUN -> `java -jar ./build/libs/touristguidesapp.jar`
    
**Frontend**
  - BUILD -> `ng build`
  - The build artifacts are in the `dist/` directory

## Credits
This app is the final project for the program [**Coding Factory**](https://codingfactory.aueb.gr/) of [*KEDIVIM/AUEB*](https://diaviou.aueb.gr/).
