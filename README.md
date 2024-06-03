# Colour Memory Game

## Description

Colour Memory Game is a simple memory game where players flip over two cards at a time, trying to find matching pairs. The objective is to find all matching pairs of cards. The game can be played either through a graphical user interface (GUI) or a text-based interface.

## How to Build and Run the Application

### Prerequisites

- Ensure you have Java Development Kit (JDK) installed.
- Ensure you have Gradle installed or use the included Gradle Wrapper.

### Build the Application

To build the application, run the following command:

gradle build


### Run the Application

#### Graphical User Interface (GUI)

To run the game with a graphical user interface, use the following command:

gradle run


#### Text-Based User Interface (TextUI)

To run the game with a text-based user interface, use the following command:

gradle run -PmainClass=se.mindlab.TextGameUI


### Create an Executable JAR

To create an executable JAR file that includes all dependencies, use the following command:

./gradlew fatJar


The JAR file will be created in the `build/libs` directory with the name `memorygame-all.jar`.

### Run the Executable JAR

To run the game from the JAR file, use the following command:


## Game Instructions

1. At the start of the game, all cards are face down.
2. Players take turns flipping two cards at a time by clicking on them (GUI) or entering the coordinates (TextUI).
3. If the two flipped cards match, they remain face up.
4. If the two flipped cards do not match, they will be turned face down after a short delay.
5. The game ends when all pairs have been found.
6. The score is based on the number of matched pairs minus number of failed matches.

Enjoy playing the Colour Memory Game!
