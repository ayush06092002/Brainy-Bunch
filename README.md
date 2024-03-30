# Brainy Bunch - Quiz App
<img src = "https://github.com/ayush06092002/Brainy-Bunch/assets/22142132/6996358a-a4d5-4d80-9c10-8d790646665b" width = "200" height = "200">
<br>
Brainy Bunch is a simple quiz app made using Jetpack Compose, a modern toolkit for building native Android UI.

## Overview

Brainy Bunch offers users an interactive quiz experience where they can test their knowledge across various topics. The app presents a series of questions, allowing users to select answers and track their progress as they move through the quiz.

## Features

- **Quiz Interface**: Engaging user interface designed using Jetpack Compose, providing a seamless experience for answering quiz questions.
  
- **Network Requests with Retrofit**: Utilizes Retrofit for handling network requests, enabling the app to fetch quiz data from a remote server efficiently and securely.
  
- **JSON Parsing with GSON**: Incorporates GSON for parsing JSON data received from the server into Kotlin data classes, facilitating easy manipulation and utilization of quiz data within the app.
  
- **Progress Tracking**: Tracks users' progress as they answer quiz questions, providing feedback on the number of correct and incorrect answers.
  
- **Randomized Question Order**: Implements list shuffling using `.shuffled()` to randomize the order of quiz questions, enhancing user engagement by presenting questions in different sequences.

## Learnings

During the development of Brainy Bunch, several key learnings were acquired:

1. **Retrofit**: Utilized Retrofit, a type-safe HTTP client for Android and Java, for handling network requests to fetch quiz data from a remote server.
  
2. **GSON**: Incorporated GSON, a Java library for JSON serialization and deserialization, to parse JSON data received from the server into Kotlin data classes.
  
3. **Wrapping of a Data Class**: Learned how to wrap a data class around JSON data retrieved from the server to facilitate easier parsing and manipulation.
  
4. **Making a Progress Bar Using Button**: Implemented a progress bar functionality that updates based on the user's interaction with the quiz questions.
  
5. **Shuffling of List Using `.shuffled()`**: Employed the `.shuffled()` method to randomize the order of quiz questions, providing users with a varied and engaging experience.

## Contributing

Contributions to Brainy Bunch are welcome! If you have any suggestions for improvements or would like to report a bug, feel free to open an issue or submit a pull request.


