# DEVIN Core
> Business logic and immutable domain models for DEVIN

## Objective

This module contains the heart of the DEVIN application. It defines the domain models and the logic for managing environment variables, ensuring that state changes are predictable through immutability.

## Tech Stack

* **Language & Runtime:** Java 21+
* **Testing:** JUnit 5, Mockito
* **Models:** Java `record` (immutable) for `EnvModel` and `EnvVariableModel`

## Architecture

The architecture relies on a strictly immutable approach:
* **Models:** `EnvModel` and `EnvVariableModel` are immutable records. Any update results in a new instance with a new UUID.
* **EnvStore:** Manages the list of environments. It notifies listeners when the state changes.
* **Decoupling:** No dependency on any UI framework (like JavaFX).

## Usage

```java
var store = new EnvStore();
store.registerListener(() -> System.out.println("Store updated!"));
store.addEnv(new EnvModel(UUID.randomUUID(), "Production", List.of()));
```
