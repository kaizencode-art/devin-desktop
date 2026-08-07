# DEVIN Desktop App
> JavaFX user interface for DEVIN

## Objective

This module provides a responsive desktop application to manage environments and their variables. It focuses on providing a fluid user experience while interacting with the immutable core.

## Tech Stack

* **Language & Runtime:** Java 21+
* **UI Framework:** JavaFX 23
* **Theme:** AtlantaFX (Primer Dark/Light)
* **Icons:** Ikonli
* **UI Utilities:** WellBehavedFX

## Architecture

The UI layer is designed to bridge the immutable core with the reactive requirements of JavaFX:
* **EnvStoreFx:** A bridge that mirrors the `EnvStore` into an `ObservableList` for JavaFX controls.
* **SelectedEnvFx:** Manages selection by index rather than ID, ensuring selection continuity even when underlying model instances change.
* **Controllers:** Use FXML and standard JavaFX patterns for UI logic.

## Running the App

The application can be started using the Gradle wrapper:
```bash
./gradlew :desktop-app:run
```
