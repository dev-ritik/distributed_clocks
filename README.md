## Lamport Clock Demonstration
This repository contains a demonstration of Lamport's Logical and Vector clocks.

    ### To Execute
    ```
    javac src/lamport/*.java -d bin
    java -cp ./bin lamport.Parser
    ```
    
    ### Input
Input to be added in `src/lamport/input.txt` in the format as:
- Qn number (1 or 2)
- commands (as in the example below)

- Qn 1
```
1
begin process p1
send p2 m1
print abc
print def
end process

begin process p2
print x1
recv p1 m1
print x2
send p1 m2
print x3
end process p2
```

- Qn  2
```
2
begin process p1
send p2 m1
print abc
print def
end process

begin process p2
print x1
recv p1 m1
print x2
send p1 m2
print x3
end process p2
```

###Output (in terminal)
- Qn 1
```
printed p2 x1 1
sent p1 m1 p2 1
received p2 m1 p1 2
printed p2 x2 3
printed p1 abc 2
sent p2 m2 p1 4
printed p1 def 3
printed p2 x3 5
```

- Qn 2
```
sent p1 m1 p2 [1, 0]
printed p2 x1 [0, 1]
received p2 m1 p1 [1, 2]
printed p2 x2 [1, 3]
printed p1 abc [2, 0]
sent p2 m2 p1 [1, 4]
printed p1 def [3, 0]
printed p2 x3 [1, 5]
```

### Errors
Errors will be printed in the terminal as well
- Invalid target process
- Deadlock
- Parsing error


###Note
The order of execution can be arbitrary