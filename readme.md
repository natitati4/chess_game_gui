# Chess Game with GUI

This is a local chess game with a graphical user interface (GUI) implemented using Java's Swing library.

# Requirements
Must have JDK installed.

# How to Run

To run the game, open a terminal and follow the steps below:

Windows:
```bash
git clone https://github.com/natitati4/chess_game_gui.git
cd chess_game_gui
gradlew build
java -jar app/build/libs/app.jar
```

Linux:
```bash
git clone https://github.com/natitati4/chess_game_gui.git
cd chess_game_gui
chmod u+x gradlew
./gradlew build
java -jar app/build/libs/app.jar
```

# Limitations
No option to draw by agreement.\
Considers checkmate/stalemate when en passant is the only move available (this is very rare though, and quite a pain to implement lol).

Enjoy playing chess!
