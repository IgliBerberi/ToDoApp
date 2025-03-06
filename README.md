# TodoApp - Android Task Management Application

![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
![Language](https://img.shields.io/badge/Language-Java-orange.svg)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-purple.svg)

A modern, feature-rich task management application for Android built with Java and following MVVM architecture patterns.

![image](https://github.com/user-attachments/assets/d15dcd05-7e91-450b-8446-5ca819d1ae0d)
![image](https://github.com/user-attachments/assets/15c7eb3a-3e57-4273-9efa-a2e00aa995a3)
![image](https://github.com/user-attachments/assets/0f7cb314-3c8c-47bc-bdf0-42d281826818)
![image](https://github.com/user-attachments/assets/94d37889-86b5-43a7-a30f-fc446e31ceb3)
![image](https://github.com/user-attachments/assets/fe70e643-a0dd-4ebd-96c2-65866447e5ea)
![image](https://github.com/user-attachments/assets/758d3372-b178-42b0-b3f3-47ead997f42a)


## Features

- **User Authentication**: Sign up and login functionality to manage personal tasks
- **Task Management**: Create, edit, delete, and mark tasks as complete
- **Priority Levels**: Assign low, medium, or high priorities to tasks with visual indicators
- **Task Sorting**: Sort tasks by priority or alphabetically
- **Task Details**: View detailed information and add comments to tasks
- **Dark/Light Mode**: Modern UI with support for light mode

## Architecture & Technical Details

This application follows the MVVM (Model-View-ViewModel) architecture pattern and is built using:

- **Room Database**: For local data persistence
- **LiveData**: For reactive UI updates
- **ViewModel**: For managing UI-related data
- **RecyclerView**: For efficient list displays
- **Material Design Components**: For modern UI elements

## Project Structure

```
com.example.todoapp/
├── Comment.java                 # Comment entity
├── CommentAdapter.java          # Adapter for comment display
├── CommentDao.java              # Data Access Object for comments
├── LoginActivity.java           # Login screen
├── MainActivity.java            # Main app screen
├── RegisterActivity.java        # Registration screen
├── SessionManager.java          # Session management utility
├── Task.java                    # Task entity
├── TaskAdapter.java             # Adapter for task display
├── TaskDao.java                 # Data Access Object for tasks
├── TaskDatabase.java            # Room database configuration
├── TaskDetailActivity.java      # Task detail screen
├── TaskRepository.java          # Repository for data operations
├── TaskViewModel.java           # ViewModel for UI state
├── User.java                    # User entity
└── UserDao.java                 # Data Access Object for users
```

## Getting Started

### Prerequisites

- Android Studio Arctic Fox (2020.3.1) or higher
- Android SDK 21 or higher
- Java Development Kit (JDK) 8 or higher

### Installation

1. Clone the repository:
   ```
   git clone https://github.com/IgliBerberi/TodoApp.git
   ```

2. Open the project in Android Studio

3. Build and run the application on an emulator or physical device

## Future Enhancements

- Task categories/tags for better organization
- Due dates and reminders for tasks
- Cloud synchronization
- Task sharing capabilities
- Dark mode toggle
- Statistics and productivity tracking

## Contributing

Contributions are welcome! Feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Material Design Components for Android
- Android Jetpack libraries
- All open-source contributors
