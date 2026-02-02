# MAP Practical Exam Implementation

## Overview
This implementation adds two new features to the Toy Language interpreter as specified in the MAP-PracticalExam.pdf:
1. **RepeatUntil Statement** - A loop construct that executes a statement repeatedly until a condition becomes true
2. **CyclicBarrier Mechanism** - A synchronization primitive for coordinating multiple threads

## Files Modified and Created

### New Files Created

#### 1. RepeatUntil Statement
- **`src/model/statement/RepeatUntilStatement.java`**
  - Implements the `repeat stmt1 until exp2` construct
  - Transforms to: `stmt1; while(!exp2) stmt1`
  - Includes typecheck method to verify exp2 is boolean

- **`src/model/expression/NotExpression.java`**
  - Helper class for negating boolean expressions
  - Used by RepeatUntil to create the while condition

#### 2. CyclicBarrier Mechanism
- **`src/model/state/BarrierTable.java`**
  - Interface defining barrier table operations
  - Maps integer keys to pairs of (value, list of thread IDs)

- **`src/model/state/MapBarrierTable.java`**
  - Thread-safe implementation using ReentrantLock
  - Ensures atomic operations for concurrent access

- **`src/model/statement/NewBarrierStatement.java`**
  - Creates new barriers in the BarrierTable
  - Syntax: `newBarrier(var, exp)`
  - Evaluates exp to get barrier count, stores location in var

- **`src/model/statement/AwaitStatement.java`**
  - Synchronization point for threads
  - Syntax: `await(var)`
  - Blocks thread until barrier count is reached

### Files Modified

#### 1. **`src/model/state/ProgramState.java`**
- Added `BarrierTable barrierTable` field
- Updated constructors to initialize BarrierTable
- Added `barrierTable()` getter method
- Updated `toString()` to display BarrierTable

#### 2. **`src/model/statement/ForkStatement.java`**
- Modified to pass BarrierTable to forked program states
- Ensures barrier table is shared across threads

#### 3. **`src/gui/MainWindow.fxml`**
- Added TableView for displaying BarrierTable
- Three columns: Index, Value, List

#### 4. **`src/gui/MainWindowController.java`**
- Added BarrierTable display components
- Created `BarrierEntry` helper class for table rows
- Added `updateBarrierTable()` method
- Integrated barrier table updates in GUI refresh

#### 5. **`src/gui/ProgramSelectionController.java`**
- Added two exam test programs:
  - **Exam Program 1**: RepeatUntil test (Expected output: {0,1,2,30})
  - **Exam Program 2**: CyclicBarrier test (Expected output: {4,20,300})

## Implementation Details

### RepeatUntil Statement
- **Execution**: Pops itself, pushes `stmt1; while(!exp2) stmt1`
- **Type Checking**: Verifies exp2 is boolean, typechecks stmt1
- **Usage**: Executes stmt1 at least once, then repeats while condition is false

### CyclicBarrier Mechanism
- **Thread Synchronization**: Threads wait at barrier until specified count is reached
- **Thread Safety**: All BarrierTable operations use locks
- **Barrier Logic**:
  - If count not reached: thread ID added to list, await re-pushed
  - If count reached: thread proceeds without blocking

## Test Programs

### Test 1: RepeatUntil
```
int v; int x; int y; v=0;
(repeat (fork(print(v);v=v-1);v=v+1) until v==3);
x=1;nop;y=3;nop;
print(v*10)
```
Expected Output: {0, 1, 2, 30}

### Test 2: CyclicBarrier
```
Ref int v1; Ref int v2; Ref int v3; int cnt;
new(v1,2);new(v2,3);new(v3,4);newBarrier(cnt,rH(v2));
fork( await(cnt);wh(v1,rh(v1)*10);print(rh(v1)) );
fork( await(cnt);wh(v2,rh(v2)*10);wh(v2,rh(v2)*10);print(rh(v2)) );
await(cnt);
print(rH(v3))
```
Expected Output: {4, 20, 300}

## Running the Implementation
1. Compile and run the JavaFX application
2. Select one of the exam programs from the program list
3. Use "Run One Step" to execute step-by-step
4. Observe the BarrierTable updates in the GUI
5. Check the output matches expected results

## Key Features
- Full type checking support for both new constructs
- Thread-safe barrier implementation
- GUI visualization of barrier state
- Comprehensive test programs demonstrating functionality
- Backward compatibility maintained for existing programs