# Modificări Examen MAP - Switch & CountSemaphore

## Fișiere Noi Create

### Problema 1 - Switch Statement
- `src/model/statement/SwitchStatement.java` - Implementare switch care se transformă în if-then-else

### Problema 2 - CountSemaphore
- `src/model/state/SemaphoreTable.java` - Interfață pentru tabelul de semafoare
- `src/model/state/MapSemaphoreTable.java` - Implementare thread-safe cu HashMap și locks
- `src/model/statement/CreateSemaphoreStatement.java` - Creează semafor nou
- `src/model/statement/AcquireStatement.java` - Achiziționează semafor (adaugă în waiting list)
- `src/model/statement/ReleaseStatement.java` - Eliberează semafor (șterge din waiting list)

## Fișiere Modificate

1. **`src/model/state/ProgramState.java`**
   - Adăugat câmp SemaphoreTable și getter
   - Actualizați constructorii să primească SemaphoreTable

2. **`src/model/statement/ForkStatement.java`**
   - Transmite SemaphoreTable la fork (shared între thread-uri)

3. **`src/controller/Controller.java`**
   - Inițializează MapSemaphoreTable() în addNewProgram()

4. **`src/gui/MainWindow.fxml`**
   - Adăugat TableView pentru SemaphoreTable (3 coloane: Index, Count, Waiting List)

5. **`src/gui/MainWindowController.java`**
   - Adăugat SemaphoreEntry class și câmpuri FXML
   - Implementat updateSemaphoreTable() pentru afișare

6. **`src/gui/ProgramSelectionController.java`**
   - Adăugat Example 12 (test Switch) - Output: {1, 2, 300}
   - Adăugat Example 13 (test Semaphore) - Output: {10, 200, 9} sau {10, 9, 200}

## Rulare
```bash
./run.sh
```