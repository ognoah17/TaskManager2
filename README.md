# **Task Manager**

## **Overview**
The Task Manager is a comprehensive application designed to streamline task management, reminders, notifications, and analytics for professionals. This README provides details about the backend implementation, which has been fully completed, and an overview of planned features for the frontend and deployment.

---

## **Backend**
The backend of this application is built using **Spring Boot** with **PostgreSQL** as the database. It follows a layered monolithic architecture for better modularity and maintainability.

---

## **Architecture**
The application adheres to the **Model-View-Controller-Service (MVCS)** architecture pattern to ensure separation of concerns and modularity:

- **Model Layer**: Represents the data and business logic (e.g., entities like Task, Category, User).
- **View Layer**: (Planned) Will handle the presentation logic via the frontend.
- **Controller Layer**: Manages HTTP requests and responses, interacting with the service layer.
- **Service Layer**: Contains the business logic and communicates with the data access layer.
- **Data Access Layer**: Manages interactions with the PostgreSQL database via JPA repositories.

---

## **Features**

### **Authentication and Authorization**
- Role-based access control using **Spring Security** with JWT tokens.
- Passwords are securely encrypted using bcrypt.
- User roles include Admin and Regular User.
- Secure endpoints with method-level access restrictions.

### **Task Management**
- Create, update, retrieve, and delete tasks.
- Tasks include attributes like priority, status, due date, and categories.

### **Category Management**
- Manage task categories with CRUD operations.
- Tasks can belong to multiple categories.

### **Reminders and Notifications**
- Reminders are linked to tasks with support for recurrence and snoozing.
- Notifications provide in-app and email-based alerts.

### **Audit Logs**
- Logs user actions like login, task updates, and deletions.

### **Reports and Analytics**
- Generate summaries of tasks, such as category breakdown and pending tasks.

---

## **DTOs (Data Transfer Objects)**
DTOs are used to transfer data between layers to:
- Decouple the internal domain models from external representations.
- Improve security by exposing only necessary fields.
- Simplify request/response payloads for REST APIs.

---

## **Tech Stack**
- **Java** with Spring Boot
- **PostgreSQL** for the database
- **Spring Security** for authentication and authorization
- **Maven** for dependency management
- **JUnit** for testing

---

## **How to Run**
1. Clone the repository.
2. Set up a PostgreSQL database and update the `application.properties` file with your credentials.
3. Build the project using Maven:
   ```bash
   mvn clean install
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

---

## **Current API Endpoints**
- **Authentication**: `/auth/login`, `/auth/register`
- **Tasks**: `/tasks`, `/tasks/{id}`
- **Categories**: `/categories`, `/categories/{id}`
- **Reminders**: `/reminders`, `/reminders/{id}`
- **Notifications**: `/notifications`, `/notifications/{id}`

---

## **Planned Features**

### **Frontend**
The frontend will be developed using **React** with **Vite** and will integrate with the backend APIs. Key features include:
- User Interface for task management, reminders, and notifications.
- Dashboards for analytics and reporting.
- Responsive Design to ensure compatibility across devices.

### **GPT-3.5 Chatbot UI**
A chatbot UI powered by GPT-3.5 will be integrated as a personal assistant for enhanced functionality, including:
- **Task Management**: Create, update, and retrieve tasks via natural language commands.
- **Reminders and Notifications**: Set and manage reminders with conversational input.
- **Analytics and Reports**: Generate reports or summaries based on voice or text queries.
- **Interactive Support**: Provide guidance and support for using application features.

---

## **Deployment**
- The backend will be hosted on **Heroku**, while the frontend will be hosted on **Netlify**.
- **Docker** will be used for containerization, and is currently in use for integration testing.
- **GitHub Actions** will automate CI/CD pipelines.

---

## **Additional Features**
- **Mobile Optimization**: Ensure usability on mobile devices.
- **Calendar Integration**: Sync tasks and reminders with external calendars.

---

## **Integration Testing**
Integration tests are implemented to ensure end-to-end functionality, including:
- Testing the interaction between controllers, services, and repositories.
- Verifying API endpoints using tools like **JUnit** and **MockMvc**.
- Database testing to ensure correct persistence and retrieval of data.
- Postman scripts for manual testing of API workflows.
