# MAP - Examen Practic - CountDownLatch

## Fișiere Create

### Problema 1: Conditional Assignment
- **`src/model/statement/ConditionalAssignmentStatement.java`** - implementare completă v=exp1?exp2:exp3

### Problema 2: CountDownLatch
- **`src/model/state/LatchTable.java`** - interfață
- **`src/model/state/MapLatchTable.java`** - implementare thread-safe cu ReentrantReadWriteLock
- **`src/model/statement/NewLatchStatement.java`** - statement newLatch(var,exp)
- **`src/model/statement/AwaitStatement.java`** - statement await(var)
- **`src/model/statement/CountDownStatement.java`** - statement countDown(var)

## Fișiere Modificate

### `src/model/state/ProgramState.java`
- Adăugat: `private final LatchTable latchTable;`
- Modificat: constructori pentru a include LatchTable
- Adăugat: `public LatchTable latchTable() { return latchTable; }`
- Modificat: `toString()` pentru afișare LatchTable

### `src/controller/Controller.java`
- Modificat linia 37-43: adăugat `new MapLatchTable()` în constructorul ProgramState

### `src/model/statement/ForkStatement.java`
- Modificat linia 26-33: adăugat `state.latchTable()` în constructorul pentru fork

### `src/gui/MainWindow.fxml`
- Adăugat liniile 47-53: TableView pentru LatchTable cu coloane Location și Value

### `src/gui/MainWindowController.java`
- Adăugat liniile 56-63: declarații FXML pentru LatchTable
- Adăugat liniile 80-82: inițializare coloane LatchTable
- Adăugat linia 135: apel `updateLatchTable()`
- Adăugat liniile 169-178: metoda `updateLatchTable()`
- Adăugat liniile 310-327: clasa helper `LatchEntry`

### `src/gui/ProgramSelectionController.java`
- Adăugat liniile 263-362: două programe de test pentru examen

### `run.sh`
- Script nou pentru compilare și rulare automată