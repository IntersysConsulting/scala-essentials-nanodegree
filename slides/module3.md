---
layout: docs
title: "Objects and classes"
section: "slides"
author: M.C. Oscar Vargas Torres
---

### M.C. Oscar Vargas Torres

@oscarvarto  
Scala Developer and  
(dys)functional programmer

------

### Training objectives

- Review OOP concepts and it's Scala implementation.
- Review Scala classes and its companion object.
- Scala case classes and pattern matching basics.
- Scala class inheritance.
- Review classic Design Patterns and compare with Scala implementations/alternatives.

------

### What skills will the trainee obtain?

add the skills the trainee will obtain

------

### Agenda

specify the agenda

------

# Objects

Objects in Scala are used for singletons and utility methods.

```scala
object MySingleton {
  var n: Int = 3
  def utilityMethod1(): String = ???
}
```

**Other** languages have *static* methods or fields instead.

------

# Classes

Scala supports OOP concepts, like classes:

```scala
// Definition
class Person(val firstName: String,
             val lastName: String,
             var age: Int)

// Usage
val p = new Person("Clark", "Kent", 30)
```

. . .

- Class names should be capitalized
- Primary constructor is in the class signature

Check [Classes tour].

[Classes tour]: https://docs.scala-lang.org/tour/classes.html

------

# Companion Object

An object of the same name than a class in the **same** source file

```scala
class Account

// Companion object
object Account {
}
```

------

# Case classes

Case classes example

```scala
case class Person(firstName: String,
                  lastName: String,
                  age: Int)
```

------

## Advantages over simple class

- Optimized for pattern matching
- Every parameter constructor becomes a `val`.
- `apply` method automatically provided (no need to use `new`)
- `unapply` method automatically provided (pattern matching)
- Automatic `toString`, `equals`, `hashCode`, and `copy` unless explicitly provided.

------

### Simple pattern matching

Programmers and Editors

```scala
sealed trait ProgrammingLanguage
case object CSharp      extends ProgrammingLanguage
case object JavaScript  extends ProgrammingLanguage
case object Python      extends ProgrammingLanguage
case object Scala       extends ProgrammingLanguage

sealed trait Editor
case object IntelliJ      extends Editor
case object Emacs         extends Editor
case object VSCode        extends Editor
case object VisualStudio  extends Editor

case class Programmer(name: String, progLang: ProgrammingLanguage)
```

------

One possibility

```scala
def chooseEditor(p: Programmer): Editor =
  p.progLang match {
      case CSharp     => VisualStudio
      case JavaScript => VSCode
      case Python     => Emacs
      case Scala      => IntelliJ
  }
```

------

## Scala OOP support

- Abstraction
- Encapsulation
- Inheritance
- Polymorphism

------

### Abstraction

```scala
trait Shape {
  def draw(): Unit
}

abstract class Quadrangle extends Shape { }
```

Traits can encode "interfaces"

------

### Encapsulation

```scala
class Person(val firstName: String,
             val lastName: String,
             var age: Int)
```

- You get a getter and setter automatically for `age` field.
- `firstName` and `lastName` have "getters" automatically.

------

# Subtyping

------

Called dynamic polymorphism also.

```scala
abstract class Person {
  def name: String
  def age: Int
}

case class Employer(val name: String,
                    val age: Int,
                    val taxno: Int)
    extends Person

case class Employee(val name: String,
                    val age: Int,
                    val salary: Int)
    extends Person
```

------

# Review of some OOP patterns

------

## Gamma et al. 1994

| Creational        | Structural        | Behavioral                |  Behavioral (cont)  |
|-------------------|-------------------|---------------------------|---------------------|
| Factory           | Adapter           | Interpreter               | Memento             |
| Abstract Factory  | Bridge            | Generics/Template         | Flyweight           |
| Builder           | Composite         | Chain of Responsibility   | Observer            |
| Prototype         | Decorator         | Command                   | State               |
| Singleton         | Facade            | Iterator                  | Strategy            |
|                   | Proxy             | Mediator                  | Visitor             |

------

### Creational Pattern: Builder

Java example:

```java
import java.util.OptionalInt;

public class NutritionFacts {
    private final int servingSize;          // (mL) required
    private final int servings;             // (per container) required
    private final OptionalInt calories;     // (per serving)
    private final OptionalInt fat;          // (g/serving)
    private final OptionalInt sodium;       // (mg/serving)
    private final OptionalInt carbohydrate; // (g/serving)
    // Code Elided
}
```

------

Private constructor:

```java
private NutritionFacts(int servingSize,
                        int servings,
                        OptionalInt calories,
                        OptionalInt fat,
                        OptionalInt sodium,
                        OptionalInt carbohydrate) {
    this.servingSize = servingSize;
    this.servings = servings;
    this.calories = calories;
    this.fat = fat;
    this.sodium = sodium;
    this.carbohydrate = carbohydrate;
}
```

------

Builder pattern for `NutritionFacts`:

```java
static public class Builder {
    private int servingSize = 0;
    private int servings = 0;
    private OptionalInt calories = OptionalInt.empty();
    private OptionalInt fat = OptionalInt.empty();
    private OptionalInt sodium = OptionalInt.empty();
    private OptionalInt carbohydrate = OptionalInt.empty();

    // Code elided
```

------

A method per field:

```java
public Builder servingSize(int servingSize) {
    this.servingSize = servingSize;
    return this;
}

public Builder servings(int servings) {
    this.servings = servings;
    return this;
}

public Builder calories(int calories) {
    this.calories = OptionalInt.of(calories);
    return this;
}
```

------

```java
public Builder fat(int fat) {
    this.fat = OptionalInt.of(fat);
    return this;
}

public Builder sodium(int sodium) {
    this.sodium = OptionalInt.of(sodium);
    return this;
}

public Builder carbohydrate(int carbohydrate) {
    this.carbohydrate = OptionalInt.of(carbohydrate);
    return this;
}
```

------

`build()` method calling `NutritionFacts` constructor.

```java
public NutritionFacts build() {
    return new NutritionFacts(servingSize,
            servings,
            calories,
            fat,
            sodium,
            carbohydrate);
}
```

------

Not necessary with Scala's case classes and default parameter values.

```scala
case class SNutritionFacts(servingSize: Int,
                           servings: Int,
                           calories: Option[Int] = None,
                           fat: Option[Int] = None,
                           sodium: Option[Int] = None,
                           carbohydrate: Option[Int] = None)
```

------

C\# equivalent:

```csharp
using LanguageExt;
using static LanguageExt.Prelude;

namespace DesignPatterns
{
  public class NutritionFacts
  {
    public int ServingSize { get; } = 0;
    public int Servings { get; } = 0;
    public Option<int> Calories { get; } = None;
    public Option<int> Fat { get; } = None;
    public Option<int> Sodium { get; } = None;
    public Option<int> Carbohydrate { get; } = None;

    public NutritionFacts(int servingSize,
                          int servings,
                          int? calories = null,
                          int? fat = null,
                          int? sodium = null,
                          int? carbohydrate = null) {
      ServingSize = servingSize;
      Servings = servings;
      Calories = Optional(calories);
      Fat = Optional(fat);
      Sodium = Optional(sodium);
      Carbohydrate = Optional(carbohydrate);
    }
  }
}
```

------

FSharp implementation (different solutions may be possible):

```fsharp
type FNutritionFactsRecord =
  { ServingSize: int
    Servings: int
    Calories: int Option
    Fat: int Option
    Sodium: int Option
    Carbohydrate: int Option
  }

let defaultNutritionFacts =
  { ServingSize = 0
    Servings = 0
    Calories = None
    Fat = None
    Sodium = None
    Carbohydrate = None
  }

let cocaCola = 
  { defaultNutritionFacts with 
      ServingSize = 1
      Servings = 2
  }
```

------

### Creational Pattern: Prototype

------

#### Problem and Solution

- **Problem**. Need to duplicate an object for some reason, but creating the object by `new` is
    not appropriate.
- **Solution**. Design an abstract base class that specifies a puret virtual clone method.
- **Consequences**. Configure an application with classes dynamically. *Each subclass must implement
    the `clone` method*

------

##### Exercise

We are going to present a simple problem and its solution using Scala case classes, then compare
with other languages implementations you will finish.

Assume the following `Point` and `Triangle` definitions:

```scala
case class Point(x: Double, y: Double)

trait Shape {
  def draw(): Unit
}
case class Triangle(p1: Point, p2: Point, p3: Point)
    extends Shape {
  override def draw(): Unit = ???
}

case class Square(p1: Point, p2: Point, p3: Point, p4: Point)
    extends Shape {
  override def draw(): Unit = ???
}
```

(A different encoding with typeclasses is possible)

------

Because both `Point` and `Triangle` are immutable case classes, you can simply share the instance
references, and no clone would be necessary.

```scala
val t1: Triangle =    // assume definition here
val t2: Triangle = t1 // sharing instance reference here
```

------

- However, we could use named parameters to modify some of the properties while doing the
  copy/clone.
- You will have to implement this copy with a modified field in your favorite language.

```scala
// The rest of the vertices remain unmodified.
val t3: Triangle = t1.copy(p1 = Point(0.0, 0.0))
```

------

##### Using Java (with Lombok!)

```java
public interface Shape {
    void draw();
}

// A point
import lombok.Value;
import lombok.experimental.Wither;

@Value
public class Point {
    @Wither float X;
    @Wither float Y;
}
```

------

**Exercise**: try implementing this with vanilla Java :)

```java
import lombok.Value;
import lombok.experimental.Wither;

@Value
public class Triangle implements Shape {
    @Wither Point p1;
    @Wither Point p2;
    @Wither Point p3;

    @Override
    public void draw() {
        // Draw this triangle
    }
}
```

------

**Exercise**: try implementing this with vanilla Java :)

```java
import lombok.Value;
import lombok.experimental.Wither;

@Value public class Square implements Shape {
    @Wither float p1;
    @Wither float p2;
    @Wither float p3;
    @Wither float p4;

    @Override public void draw() {
        // draw this square
    }
}
```

------

###### Implementation 1 (F\#)

Complete the exercise with the following starting point.

```fsharp
open System;

module Shape =
  type IShape =
    abstract member Draw: unit
  type Point =
    { x: float; y: float }
  type Triangle =
    { p1: Point; p2: Point; p3: Point
    } interface IShape with
        member x.Draw: unit = raise (new NotImplementedException "Triangle.draw")
```

------

###### Implementation 2 (F\#)

Complete the exercise with the following starting point.

```fsharp
open System
open System.Drawing
type Triangle =
  { p1: Point; p2: Point; p3: Point }
type Square = 
  { p1: Point; p2: Point; p3: Point; p4: Point }
type Shape =
  | STriangle of Triangle
  | SSquare of Square
let draw (s: Shape): unit =
  match s with
  | STriangle t -> raise (new NotImplementedException("draw a Triangle"))
  | SSquare s -> raise (new NotImplementedException("draw a Square"))
```

------

- Haskell solution using typeclasses.
- Complete the exercise with the following starting point.

```haskell
{-# LANGUAGE DuplicateRecordFields #-}

data Point = Point { x :: Float, y :: Float }
data Triangle = Triangle
  { p1 :: Point, p2 :: Point, p3 :: Point }
data Square = Square
  { p1 :: Point, p2 :: Point
  , p3 :: Point, p4 :: Point }

class Shape a where
  draw :: a -> ()
instance Shape Triangle where
  draw t = () -- Not implemented
```

Typeclasses will be reviewed in Module 8.

------

- Other patterns can be implemented more easily in Scala than other OOP languages.
- Some of them might be innecessary o trivially implemented in Scala (e.g. Singleton).
