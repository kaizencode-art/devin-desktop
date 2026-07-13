# Environment Management Architecture – DEVIN

This document explains how environment state is handled in DEVIN.

## Core idea

The `core` module uses an immutable model.  
Objects are never modified in place. Any change creates a new instance.

When an environment changes:
- the old `EnvModel` is removed from the `EnvStore`
- a new `EnvModel` is created with a new UUID

This keeps state changes explicit and avoids side effects. It also makes future features like Undo/Redo possible without redesigning the core.

## UI selection

Because IDs change on every update, the UI cannot rely on UUIDs to keep the current selection.

In `desktop-app`, selection is handled by position:
- `SelectedEnvFx` stores the index of the selected environment
- when the store changes, the same index is reselected
- the UI updates with the new instance

From the user’s perspective, the selection stays the same.

## Responsibilities

### core
- business logic only
- immutable `EnvModel` and `EnvVariableModel`
- `EnvStore` manages state and UUIDs
- no JavaFX dependency

### desktop-app
- UI-related code
- `EnvStoreFx` bridges the store to JavaFX
- `SelectedEnvFx` handles selection continuity

## Notes

This approach is more rigid than a mutable CRUD model, but it keeps the core predictable and easy to test.  
The UI remains a thin layer on top of it.
