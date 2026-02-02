# Modificări Examen MAP - Lock Mechanism și For Statement

## Fișiere Noi Create:

### Problem 1: For Statement
- `/src/model/statement/ForStatement.java` - Implementare statement for

### Problem 2: Lock Mechanism
- `/src/model/state/LockTable.java` - Interfață pentru tabelul de lock-uri
- `/src/model/state/MapLockTable.java` - Implementare thread-safe pentru LockTable
- `/src/model/statement/NewLockStatement.java` - Statement pentru creare lock nou
- `/src/model/statement/LockStatement.java` - Statement pentru achiziție lock
- `/src/model/statement/UnlockStatement.java` - Statement pentru eliberare lock

## Fișiere Modificate:

### 1. `/src/model/state/ProgramState.java`
- **Modificat:** Constructor și câmpuri
- **Adăugat:** LockTable ca nou câmp și getter `lockTable()`

### 2. `/src/model/statement/ForkStatement.java`
- **Modificat:** Metoda `execute()`
- **Adăugat:** Transmitere LockTable către thread-ul nou creat

### 3. `/src/controller/Controller.java`
- **Modificat:** Metoda `addNewProgram()`
- **Adăugat:** Inițializare MapLockTable în constructor ProgramState

### 4. `/src/gui/MainWindow.fxml`
- **Modificat:** Layout-ul GUI
- **Adăugat:** TableView pentru afișare LockTable cu coloane Location și Value

### 5. `/src/gui/MainWindowController.java`
- **Modificat:** Metoda `initialize()` și `updateAllComponents()`
- **Adăugat:**
  - Câmpuri pentru LockTable display
  - Metoda `updateLockTable()`
  - Clasa helper `LockEntry`

### 6. `/src/gui/ProgramSelectionController.java`
- **Modificat:** Metoda `createPrograms()`
- **Adăugat:**
  - Example 12 - Program demonstrativ pentru For Statement
  - Example 13 - Program demonstrativ pentru Lock Mechanism

## Script-uri Create:

### 1. `/run.sh`
- Script pentru compilare și rulare cu opțiuni GUI/Text

### 2. `/compile.sh`
- Script doar pentru compilare