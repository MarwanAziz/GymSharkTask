# AI usage and implementation decisions

## How I used AI

I used AI as a development assistant throughout the challenge. I wrote detailed prompts describing the app's requirements, UI, architecture, and expected behaviour, then used AI to help establish the project structure, interpret the API response, define the product models, and write unit tests.

I reviewed the suggestions and made the final implementation decisions myself.

## Where I took a different approach

AI suggested keeping the architecture simpler and avoiding separate Gradle modules for a challenge of this size. I decided to introduce separate modules to establish clear boundaries between the presentation, domain, and data layers. My intention was to create a maintainable foundation while keeping the architecture proportionate to the requirements.

## Handling ambiguity

The API did not clearly document its price format or currency. I made an assumption about the price representation and used the device locale when displaying currency. These are limitations of the available API contract, and I would prefer to confirm the intended format and currency rather than rely on assumptions in a production application.

# GymSharkTask

Android catalogue app. It loads a Gymshark product list, shows it in a grid, and opens a product’s details from that same response.

Built with Kotlin, Jetpack Compose, and Hilt. Domain and data modules are plain JVM libraries.

## Modules

Dependencies point inward. The feature module talks only to the domain. The domain never sees Android, Compose, JSON, or HTTP.

```
:app
  ├── :feature:products ──► :domain
  ├── :data ──────────────► :domain
  │     └── :data:remote
  └── :domain
```

| Module | Kind | What it owns |
| --- | --- | --- |
| `:app` | Android application | Composition root, Hilt graph, navigation, theme |
| `:feature:products` | Android library | Product list and detail screens, and their ViewModels |
| `:domain` | JVM | `Product`, `ProductUseCase`, `ProductRepository`, success and failure types |
| `:data` | JVM | `RemoteProductRepository` and the mapper from a catalogue hit to `Product` |
| `:data:remote` | JVM | HTTP client and the JSON models for the catalogue payload |

`:data` and `:data:remote` stay separate. JSON models stay in `:data:remote`. The mapper stays in `:data`. Domain models stay in `:domain`.

## Request path

```
Screen → ViewModel → ProductUseCase → ProductRepository → CatalogueRemote
```

`:app` wires this in `AppModule`:

`CatalogueClient` → `RemoteProductRepository` → `ProductUseCase`

The list ViewModel calls `getProducts()`. Tapping a product passes that `Product` to the detail screen. Detail does not request the catalogue again.

`ProductUseCase.getProduct(id)` still looks a product up in a loaded list. `ProductNotFound` is decided there, not by the API. The screen does not call it.

## Layout

```
app/
  GymSharkTaskApplication.kt    Hilt application
  MainActivity.kt               List and detail host, with the slide between them
  ProductCoordinator.kt         List or detail. A selected product opens detail; back returns to the list
  di/AppModule.kt               Catalogue URL and the object graph
  ui/theme/                     Material theme

feature/products/
  list/                         Grid: loading, products, empty, and error with retry
  detail/                       Title, price, gallery, sizes, and HTML description
  PriceFormat.kt                Price string from the device locale
  ProductImage.kt               Photo, or a placeholder when the URL is missing or the load fails

domain/
  Product.kt                    Catalogue product and variant
  ProductRepository.kt          getProducts()
  ProductUseCase.kt             getProducts() and getProduct(id)
  DomainResult.kt               Success, CatalogueUnavailable, ProductNotFound

data/
  ProductMapper.kt              Catalogue hit → Product. Blank image URLs are dropped here
  RemoteProductRepository.kt    Remote failures become CatalogueUnavailable

data/remote/
  CatalogueClient.kt            OkHttp fetch of the catalogue JSON
  CatalogueResponse.kt          JSON models. Every hit field is optional except id
  CatalogueRemote.kt            fetchCatalogue()
```

Package root: `net.marwanaziz.gymsharktask`.

## Tests

Domain and data tests run on the JVM with JUnit 4.

| Module | What is covered |
| --- | --- |
| `:domain` | Product list and single-product lookup, including `ProductNotFound` |
| `:data` | Mapping a hit, and `CatalogueUnavailable` when the remote call fails |
| `:data:remote` | Decoding a local catalogue fixture, and HTTP, unreadable, and connection failures |
| `:feature:products` | List ViewModel states, price formatting, and size availability labels |
| `:app` | A selected product opens detail, and back returns to the list |
