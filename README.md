# Multimedia Technologies 2019-2020 Assignment
A simple airport management system using Java11 and JavaFX11. We process incoming flight requests assigning them to the appropriate gates and removing them when the planes take off. We also hold the total income based on how long the plane stayed at the airport, the gate's type and the services the flight used.
## Project structure
```
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
└── src
    └── main
        ├── java
        │   └── multimedia
        │       ├── controllers
        │       │   ├── MainWindowController.java
        │       │   └── PopupController.java
        │       ├── Main.java
        │       ├── model
        │       │   ├── Flight.java
        │       │   └── Gate.java
        │       └── util
        │           └── Helper.java
        └── resources
            ├── fxml
            │   └── MainWindow.fxml
            └── medialab
                ├── airport_default.txt
                └── setup_default.txt
```
##### Repository root directory
* `build.gradle:` Gradle build file that uses JavaFX11 and a gradle plugin to download the necessary JavaFX11 libraries.
* `gradle:` Directory that has the gradle wrapper files
* `gradlew:` Unix script/Gradle wrapper
* `gradlew.bat:` Windows script/Gradle wrapper
##### src/main/java/multimedia
* controllers
    * `MainWindowController.java:` Controller of the main window of the application responsible for handling all user inputs
    * `PopupController.java:` Controller of all the popups the user creates
* model
    * `Flight.java:` Class representing flight objects that hold basic flight information
    * `Gate.java:` Class representing gate objects that hold basic gate information
* util
    * `Helper.java:` Helper class that handles certain events and provides helper functions
* `Main.java:` The main class responsible for starting the application
##### src/main/resources
* fxml
    * `MainWindow.fxml:` fxml file that describes the layout of the main window. Created with Scene Builder
* medialab
    * `airport_default.txt:` File that is read when the Load button is pressed with the default scenario. Describes gate information
    * `setup_default.txt:` File that is read when the Load button is pressed with the default scenario. Describes flight information
## How to run (The gradle plugin requires JavaFX 11+)
1. Make sure the `$JAVE_HOME` environment variable is set up correctly.
2. Put some scenario files in the medialab directory. Their names must be `airport_<scenario_name>.txt` and `setup_<scenario_name>.txt`. The default ones can be used as well.
3. Run the `run` task with the gradle wrapper
    * For UNIX: `./gradlew run`
    * For Windows: `gradlew.bat run`
4. Load a scenario.
5. Start the simulation.
6. Submit flight requests.
