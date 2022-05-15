# Multimedia Technologies 2019-2020 Assignment
A simple airport management system using Java18 and JavaFX18 (also tested in Java11 and JavaFX11). We process incoming flight requests assigning them to the appropriate gates and removing them when the planes take off. We also hold the total income based on how long the plane stayed at the airport, the gate's type and the services the flight used.
## Project structure
```
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── README.md
└── src
    └── main
        ├── java
        │   └── multimedia
        │       ├── controllers
        │       │   ├── DelayedPopupController.java
        │       │   ├── FlightsPopupController.java
        │       │   ├── GatesPopupController.java
        │       │   ├── HoldingPopupController.java
        │       │   ├── MainWindowController.java
        │       │   ├── NextDeparturesPopupController.java
        │       │   └── PopupController.java
        │       ├── exceptions
        │       │   └── InvalidInputException.java
        │       ├── Main.java
        │       ├── model
        │       │   ├── Airport.java
        │       │   ├── BaseGate.java
        │       │   ├── CargoGate.java
        │       │   ├── Flight.java
        │       │   ├── Gate.java
        │       │   ├── GeneralParkingGate.java
        │       │   ├── LongStayGate.java
        │       │   ├── ZoneAGate.java
        │       │   ├── ZoneBGate.java
        │       │   └── ZoneCGate.java
        │       └── util
        │           ├── Helper.java
        │           ├── Initializer.java
        │           └── TimeScheduler.java
        └── resources
            ├── fxml
            │   └── MainWindow.fxml
            └── medialab
                ├── airport_default.txt
                └── setup_default.txt

```
##### Repository root directory
* `build.gradle:` Gradle build file that uses JavaFX18 and a gradle plugin to download the necessary JavaFX18 libraries.
* `gradle:` Directory that has the gradle wrapper files
* `gradlew:` Unix script/Gradle wrapper
* `gradlew.bat:` Windows script/Gradle wrapper
##### src/main/java/multimedia
* controllers
    * `DelayedPopupController.java:` Controller for the delayed flights popup
    * `FlightsPopupController.java:` Controller for the flights popup
    * `GatesPopupController.java:` Controller for the gates popup
    * `HoldingPopupController.java:` Controller for the holding flights popup
    * `MainWindowController.java:` Controller of the main window of the application responsible for handling all user inputs
    * `NextDeparturesPopupController.java:` Controller for the next departures popup
    * `PopupController.java:` Controller for the base popup class
* exceptions
    * `InvalidInputException:` Exception thrown when the input scenario inside the medialab directory holds invalid info
* model
    * `Airport.java:` Airport class that holds airport info and handles flight events
    * `BaseGate.java:` Base gate class that can be extended
    * `CargoGate.java:` Cargo gate class
    * `Flight.java:` Class representing flight objects that hold basic flight information
    * `Gate.java:` Simple gate class
    * `GeneralParkingGate.java:` General parking gate class
    * `LongStayGate.java:` Long stay gate class
    * `ZoneAGate.java:` Zone A gate class
    * `ZoneBGate.java:` Zone B gate class
    * `ZoneCGate.java:` Zone C gate class
* util
    * `Helper.java:` Helper class that provides capitalization of Strings and a random number generator
    * `Initializer.java:` Initializer class that reads scenario input and populates the airport class with gate and flight data
    * `TimeScheduler.java:` Time scehduler class responsible for timer tasks related to flight events and updating the UI at regular intervals to simulate the passing of time
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
