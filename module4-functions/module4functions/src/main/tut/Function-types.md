# Function types

Note to self: 
* Use the Scala 2.12 Specification as much as possible
* https://www.scala-lang.org/files/archive/spec/2.12/03-types.html#function-types

According to https://www.scala-lang.org/files/archive/spec/2.12/03-types.html
there is a distinction between Funtion Types (a Value Type) and Method Types (a Non-Value Type).

If possible, explain the difference.

Function types associate to the right, e.g. `S => T => U` is the same as `S => (T => U)`

Here is how you add numbers:

```tut
1 + 1
```

Another block of code:
