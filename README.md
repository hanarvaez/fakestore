# FakeStore - Android E-Commerce App

## Project Description

FakeStore is a native Android e-commerce application that demonstrates a complete implementation of
a shopping app with user authentication, product listing, favorites management, and user profile
features.

### Features

- **User Login**: Authenticate users with username and password
- **Product Catalog**: Browse products from the FakeStore API with local caching
- **Favorites**: Mark products as favorites, stored per user
- **User Profile**: View user information and favorite products count
- **Offline Support**: Local database caching for products and user data

---

## Architecture

The project follows **Clean Architecture** with clear separation of concerns across three main
layers:

```
Presentation Layer ──────> Domain Layer <────── Data Layer
```

### Layer Breakdown

| Layer            | Components                                                              | Responsibility                                            |
|------------------|-------------------------------------------------------------------------|-----------------------------------------------------------|
| **Presentation** | Screens, ViewModels, Components, Navigation                             | UI rendering, user interaction handling, state management |
| **Domain**       | Models, Repository Interfaces, Use Cases                                | Business logic, domain rules, data abstraction            |
| **Data**         | Repository Implementations, Room Database, Retrofit API, DTOs, Entities | Data access, network calls, local storage                 |

### Key Patterns Implemented

#### 1. MVVM (Model-View-ViewModel)

- **View**: Compose screens (HomeScreen, LoginScreen, FavoritesScreen, ProfileScreen)
- **ViewModel**: HomeViewModel, LoginViewModel, FavoritesViewModel, ProfileViewModel
- **Model**: Product, User, Rating domain models

#### 2. Repository Pattern

- **Abstraction**: `ProductRepository`, `UserRepository` interfaces in domain layer
- **Implementation**: `ProductRepositoryImpl`, `UserRepositoryImpl` in data layer
- Benefits: Decouples data sources from business logic, enables easy testing

#### 3. Use Case Pattern

- **Base Classes**: `SuspendUseCase`, `FlowUseCase`
- **Implementations**: GetProductsUseCase, GetFavoritesUseCase, ToggleFavoriteUseCase, etc.
- Benefits: Encapsulates business operations, ensures single responsibility

#### 4. Dependency Injection (Hilt)

- Modules: `NetworkModule`, `DatabaseModule`, `RepositoryModule`, `CoroutinesModule`
- Injects: API services, DAOs, Repositories, Use Cases, ViewModels

#### 5. State Management

- **ViewState**: Sealed classes (Initial, Loading, Content, Error)
- **ViewEvent**: User actions represented as sealed classes
- **BaseViewModel**: Provides state flow and event handling

---

## Architecture Decisions

### 1. Why Clean Architecture?

**Reason**: Separation of concerns is critical for maintainability and testability.

- **Presentation** depends on **Domain** (abstractions)
- **Data** implements **Domain** (interfaces)
- Changes in one layer don't affect others
- Easy to replace data sources (e.g., switch from API to mock)

### 2. Why MVVM with ViewState/ViewEvent Pattern?

**Reason**: Provides unidirectional data flow and predictable state management.

- ViewState represents UI state (Loading, Content, Error)
- ViewEvent represents user actions
- ViewModel processes events and updates state
- Debugging becomes easier with explicit state transitions

### 3. Why Use Cases?

**Reason**: Encapsulates business logic in reusable, testable units.

- Each use case has a single responsibility
- Use cases are independent of UI and data sources
- Easy to unit test with mocked repositories

### 4. Why Combine Flow for Products and Favorites?

**Reason**: Reactive updates when favorites change without manual refresh.

```kotlin
productsFlow.combine(favoriteDao.getFavoriteProductIds(userId)) { products, favoriteIds ->
    products.map { entity ->
        entity.toDomain(isFavorite = entity.id in favoriteIds)
    }
}
```

Benefits: UI automatically updates when favorites change

### 5. Why Favorites Stored Per User?

**Reason**: Each user has their own favorites list.

```kotlin
@Entity(tableName = "favorites", primaryKeys = ["productId", "userId"])
data class FavoriteEntity(
    val productId: Int,
    val userId: Int
)
```

Benefits: Proper multi-user support, data isolation per user

### 6. Why MockK for Testing?

**Reason**: Kotlin-native mocking library with better coroutine support than Mockito.

-idiomatic Kotlin API

- built-in coroutine support
- no static mocking issues

### 7. Why Local Database First?

**Reason**: Better user experience with offline support.

- `getUser()` checks local DB first, then API
- Products cached in Room after first fetch
- Reduces network calls, faster loading

---

## Technology Stack

| Category       | Technology                         |
|----------------|------------------------------------|
| Language       | Kotlin 2.3                         |
| UI Framework   | Jetpack Compose                    |
| Architecture   | Clean Architecture + MVVM          |
| DI             | Hilt                               |
| Networking     | Retrofit + OkHttp                  |
| Local Database | Room                               |
| Async          | Kotlin Coroutines + Flow           |
| Navigation     | Compose Navigation                 |
| Image Loading  | Coil                               |
| Testing        | JUnit 4, MockK, Compose UI Testing |

---

## Project Structure

```
app/src/main/java/co/com/monkeymobile/fakestore/
├── data/
│   ├── local/
│   │   ├── dao/          # Room DAOs
│   │   ├── entity/       # Room Entities
│   │   └── FakeStoreDatabase.kt
│   ├── remote/
│   │   ├── api/          # Retrofit API
│   │   └── dto/          # Data Transfer Objects
│   ├── repository/       # Repository Implementations
│   └── mapper/           # DTO to Entity mappers
├── di/                   # Hilt Modules
├── domain/
│   ├── model/            # Domain Models
│   ├── repository/       # Repository Interfaces
│   └── usecase/          # Use Cases
├── presentation/
│   ├── screens/          # Compose Screens
│   │   ├── home/
│   │   ├── login/
│   │   ├── favorites/
│   │   └── profile/
│   ├── components/       # Reusable UI Components
│   ├── navigation/       # Navigation Graph
│   └── ui/theme/         # Material Theme
└── FakeStoreApp.kt       # Application Class
```

---

## Build Variants

| Variant | BASE_URL                  |
|---------|---------------------------|
| debug   | https://fakestoreapi.com/ |
| release | https://fakestoreapi.com/ |

---

## Testing Coverage

### Unit Tests

- **Use Cases**: GetProductsUseCase, GetFavoritesUseCase, ToggleFavoriteUseCase, etc.
- **Repositories**: ProductRepositoryImpl, UserRepositoryImpl
- **ViewModels**: HomeViewModel, LoginViewModel, FavoritesViewModel, ProfileViewModel

### Instrumented Tests (Android)

- **LoginScreen**: Field display, input validation, error messages
- **HomeScreen**: Navigation from login, product display

---

## Running the App

1. **Build Debug APK**:
   ```bash
   ./gradlew assembleDebug
   ```

2. **Run Unit Tests**:
   ```bash
   ./gradlew testDebugUnitTest
   ```

3. **Run Instrumented Tests**:
   ```bash
   ./gradlew connectedDebugAndroidTest
   ```

---

## License

This project is for demonstration purposes.