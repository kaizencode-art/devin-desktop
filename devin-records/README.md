# DEVIN Records
> Persistence layer for DEVIN using EclipseStore

## Objective

This module handles the long-term storage of environment configurations. It ensures that environments created or modified in the application are persisted across sessions.

## Tech Stack

* **Language & Runtime:** Java 21+
* **Storage Engine:** EclipseStore (formerly MicroStream)
* **Dependency:** Depends on `:core`

## Architecture

The persistence is implemented using an adapter pattern to react to core store changes:
* **EclipseStoreEnvAdapter:** Handles the low-level storage operations using EclipseStore's embedded engine.
* **EnvPersistenceSubscriber:** A subscriber that listens to `EnvStore` events and triggers persistence updates.
* **DataRoot:** The root object for EclipseStore persistence.

## Configuration

The storage location is typically configured within the application to point to a local directory where EclipseStore will manage its data files.
