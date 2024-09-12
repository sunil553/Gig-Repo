This project is implemented using the MVVM architecture with a clean and modular approach, ensuring easy maintenance and scalability as the project grows.

The app includes a simple API call that displays a list of countries. Apikey used from accuweather.com , this has a limited hits .



**MVVM (Model-View-ViewModel): Provides a clear separation of concerns, making the codebase easier to manage and test.**

Project Structure The project is structured following Clean Architecture principles:

domain: Contains the business logic or domain layer of the application. This layer includes entities, use cases, and any business rules.

data: This layer is responsible for interacting with external data sources such as databases or APIs. It contains repository implementations and data sources.

presentation: This layer handles the UI logic and presentation of data. It contains the ViewModels, UI components, and navigation logic.


**Dependency Injection with Hilt: Facilitates the injection of dependencies, enhancing modularity and testability.**

**Retrofit: Simplifies network operations, making API calls efficient and straightforward.**

**Jetpack Compose: Enables building a modern, responsive UI with less code.**

**JUnit and Mockito: Provides robust testing frameworks to ensure code reliability and quality.**
