# AGENTS.md

## Project
Android application built with Kotlin and Jetpack Compose.

## Architecture
- Follow MVVM-C and Clean Architecture where appropriate.
- Keep dependencies flowing towards the domain layer.
- Separate remote API DTOs from domain models.
- Use a repository to abstract data access.
- Keep business logic out of Composable functions.

## Guidelines
- Use Kotlin coroutines for asynchronous operations.
- Represent UI states explicitly: Loading, Success, and Error.
- Handle missing fields and invalid image URLs safely.
- Avoid unnecessary abstractions and dependencies.
- Write unit tests for ViewModels, repositories, and model mapping.
- Follow existing project conventions before introducing new patterns.

## Before completing a task
- Run relevant unit tests.
- Check for compilation errors.
- Summarise changes and any remaining issues.

## Testing

Write the test for a behaviour before its implementation.

- Mapper tests in `:data` for a normal hit, a hit with labels, a null label list, and a missing image.
- Repository test in `:data` for `CatalogueUnavailable` and for `ProductNotFound` with the requested id.
- One HTTP test in `:data:remote` that serves a local fixture and checks the decoded JSON.
- Coordinator test in `:app` that a selected id opens detail.
- Domain and data tests run on the JVM with JUnit 4.

Run the tests for every module you change before finishing.

## How to change code

- Match the names and patterns already in the module you are editing.
- Pass dependencies through constructors.
- Add a type only when it owns a decision or hides a boundary. A class that only forwards a call should not be added.
- Keep functions small and UI state immutable.
- Leave unrelated files unchanged.
- Do not add Kotlin Multiplatform, another feature module, or a second use-case class unless the task requires it.
- Do not merge `:data` and `:data:remote`. Do not move the mapper into `:data:remote` or the JSON models into `:domain`.
