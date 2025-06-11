# <img src="https://github.com/user-attachments/assets/6cc25ed8-e5aa-4ac3-9ea5-77014038a777" width="60" height="60" align="center" /> F1-Journal

**F1-Journal** is a Kotlin Multiplatform Desktop application for comparing Formula 1 driver performance under various weather and temperature conditions.

## ⭐️Features
### 1. 📊 Average Driver Positions – Dry vs Rain
Displays a chart of the average race positions for each driver in dry and wet conditions.

### 2. 💧 Performance Difference – Dry − Rain
Highlights the performance gap between dry and wet tracks for each driver. Sorted to show who performs best in the rain.

### 📅 Movie Details
Analyzes how asphalt temperature affects drivers' lap times.

### 🔀 Data Import / Export
The app allows importing and exporting data to/from a server:
- 🔄 Import: User selects a JSON or XML file to upload current F1 data to the server.
- 📀 Export: User chooses a location and file name to save data from the server.

Supported Formats
- JSON
- XML

## ⚙️Technologies
- **Jetpack Compose Multiplatform** - Modern UI toolkit for declarative UI design.

- **Ktor** - Server communication

- **Koin** - Dependency Injection

- **Vico** - Advanced charting ([Vico documentation](https://www.patrykandpatrick.com/vico/guide/stable))

- **Voyager** - Navigation between screens

- **Coil** - Efficient image loading and loading states.

- **Jackson / org.json** - JSON and XML processing

- **BuildConfig** - Configuration management for build-time constants, including platform-specific values (e.g., API URLs, environment flags). ([BuildConfig plugin documentation](https://github.com/gmazzo/gradle-buildconfig-plugin))

- **MVI architecture** - Scalable and maintainable state management

## Here are some overview pictures:
![Image](https://github.com/user-attachments/assets/4c97ca4e-b1f9-413a-b239-f68a5c4b6f45)
![Image](https://github.com/user-attachments/assets/1a16a320-d9a1-4350-a883-0670c404e012)

## Installation

1. **Setup Android client:**
    1. **Clone project**
        ```bash
        git clone https://github.com/AdamDawi/F1-Journal
        ```
    2. **Open the project in Android Studio.**
    3. **Be sure the versions in gradle are same as on github**

2. **Run the backend server**
    1. **Clone project**<br>
        ```bash
        git clone https://github.com/jakubdziem/f1-personal-tracker-server
        cd f1-personal-tracker-server
        ```
    2. **Build Docker image:**<br>
        ```bash
        docker build -t f1-personal-tracker:latest .
        ```
    3. **Start container on port 8080** <br>
        ```bash
        docker-compose up
        ```
3. **Add the server url to your local gradle.properties file as follows:**
```properties
BASE_URL="http://localhost:8080"
```
4. **Run client app:**
In android studio run:
```bash
./gradlew run
```

## Requirements
Windows operating system

## Author
Adam Dawidziuk🧑‍💻
