# TodoApp - Android Task Management Application

![License](https://img.shields.io/badge/License-MIT-blue.svg)
![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
![Language](https://img.shields.io/badge/Language-Java-orange.svg)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-purple.svg)

A modern, feature-rich task management application for Android built with Java and following MVVM architecture patterns.

![image](https://github.com/user-attachments/assets/74a1dab3-7694-4d61-9765-523e867e7b8b)
![image](https://github.com/user-attachments/assets/15c7eb3a-3e57-4273-9efa-a2e00aa995a3)
![image](https://github.com/user-attachments/assets/758d3372-b178-42b0-b3f3-47ead997f42a)
![image](https://github.com/user-attachments/assets/94d37889-86b5-43a7-a30f-fc446e31ceb3)
![image](https://github.com/user-attachments/assets/fe70e643-a0dd-4ebd-96c2-65866447e5ea)
![image](https://github.com/user-attachments/assets/18eca8de-3de2-4516-9382-9d60d101c8d8)


## Features

- **User Authentication**: Sign up and login functionality to manage personal tasks
- **Task Management**: Create, edit, delete, and mark tasks as complete
- **Priority Levels**: Assign low, medium, or high priorities to tasks with visual indicators
- **Task Sorting**: Sort tasks by priority or alphabetically
- **Task Details**: View detailed information and add comments to tasks
- **Profile Management**: Update personal information and change password
- **Dark/Light Mode**: Modern UI with support for light mode

## Architecture & Technical Details

This application follows the MVVM (Model-View-ViewModel) architecture pattern and is built using:

- **Room Database**: For local data persistence
- **LiveData**: For reactive UI updates
- **ViewModel**: For managing UI-related data
- **RecyclerView**: For efficient list displays
- **Material Design Components**: For modern UI elements

## Project Structure

### Java Source Files
```
com.example.todoapp/
├── Comment.java                 # Comment entity
├── CommentAdapter.java          # Adapter for comment display
├── CommentDao.java              # Data Access Object for comments
├── LoginActivity.java           # Login screen
├── MainActivity.java            # Main app screen
├── ProfileActivity.java         # User profile management screen
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

### Resources
```
res/
├── drawable/
│   ├── ic_add.xml               # Add icon
│   ├── ic_delete.xml            # Delete icon
│   ├── ic_info.xml              # Info icon
│   ├── ic_launcher_background.xml
│   ├── ic_launcher_foreground.xml
│   └── priority_badge.xml       # Priority indicator drawable
├── layout/
│   ├── activity_login.xml       # Login screen layout
│   ├── activity_main.xml        # Main screen layout
│   ├── activity_profile.xml     # Profile management layout
│   ├── activity_register.xml    # Registration screen layout
│   ├── activity_task_detail.xml # Task details screen layout
│   ├── dialog_add_task.xml      # Add task dialog layout
│   ├── item_comment.xml         # Comment list item layout
│   └── item_task.xml            # Task list item layout
├── menu/
│   └── menu_main.xml            # Main menu options
├── mipmap/                      # App icon resources
├── values/
│   ├── colors.xml               # Color definitions
│   ├── strings.xml              # String resources
│   ├── styles.xml               # Style definitions
│   └── themes.xml               # Theme definitions
└── xml/
    ├── backup_rules.xml
    └── data_extraction_rules.xml
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
