# Objective

Provide a simple and responsive desktop application to manage environments and their variables (creation, visualization, in-place editing).
Respect the immutability of the models (**EnvModel**, **EnvVariableModel**) while maintaining a fluid UI through index-based selection (no IDs on the UI side).
Decouple the pure domain layer (store without JavaFX) from the UI layer through adapters (**EnvStoreFx**, **SelectedEnvFx**) to improve testability and maintainability.

# Tech Stack

* **Language & Runtime:** Java 21+
* **UI:** JavaFX 23
* **Build:** Gradle (wrapper included)
* **UI Theme:** Atlantafx (Primer Dark/Light)
* **Icons:** Ikonli
* **UI Reactivity:** ObservableList (via EnvStoreFx), JavaFX properties in SelectedEnvFx
* **Table Navigation / Editing:** WellBehavedFX (org.fxmisc.wellbehaved.event)
* **Testing:** JUnit 5, Mockito
* **Models:** Java `record` (immutable) for EnvModel and EnvVariableModel
* **Architecture:** EnvStore (pure domain, custom listeners) + EnvStoreFx (ObservableList mirror) + SelectedEnvFx (index-based selection)

## Project Status & Intent

This project is primarily an experimental and exploratory effort. There is currently no fixed roadmap or predefined end goal. It is driven by experimentation and learning.

If anyone sees potential in this project, they are free to use it, extend it, adapt it, or build upon it in any way they find useful.
