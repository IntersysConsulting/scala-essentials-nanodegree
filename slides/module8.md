---
layout: docs
title: "Typeclasses"
section: "slides"
author: M.C. Oscar Vargas Torres
---

### M.C. Oscar Vargas Torres

@oscarvarto  
Scala Developer and  
(dys)functional programmer

------

### Training objectives

- Understand the problem the typeclass pattern solves
- Learn *at least* one way to implement the typeclass pattern.
- Make exercises and practice!

------

### What skills will the trainee obtain?

- EPIC DECOUPLING!

------

### Agenda

- Implicits reminder
- Context bound syntax
- Typeclass examples/exercises

------

## Implicits (reminder)

Different definitions of `add`:

```scala
def add1(a: Int, b: Int) = a + b
// curried
def add2(a: Int)(b: Int) = a + b
// Last parameter list can have implicit values
// pulled from scope
def add3(a: Int)(implicit b: Int) = a + b

// *If* an implicit Int value is in scope...
implicit val one: Int = 1
// Second argument provided implicitly
def addOne(a: Int) = add3(a)
```

------

### Context bound syntax

`Ordering[T]` from the Scala Standard Library.

```scala
import scala.math.Ordering.Implicits._

// Typeclass constraint
def myMax[T : Ordering](x: T, y: T): T =
  if (x > y) x else y

// Equivalent to
def myMax[T](x: T, y: T)(implicit ev: Ordering[T]): T =
  if (x > y) x else y
```

------

## Example

Let's say we have the following:

![Expression ADT]

[Expression ADT]: diagrams/expression.png

------

with the following encoding:

```scala
sealed trait Expression

case class Number(value: Int) extends Expression

case class Plus(lhs: Expression,
                rhs: Expression) extends Expression

case class Minus(lhs: Expression,
                 rhs: Expression) extends Expression
```

------

### `JsonValue` ADT definition

```scala
sealed trait JsonValue
case class JsonObject (entries: Map[String, JsonValue])
  extends JsonValue
case class JsonArray  (entries: Seq[JsonValue])
  extends JsonValue
case class JsonString (value: String)
  extends JsonValue
case class JsonNumber (value: BigDecimal)
  extends JsonValue
case class JsonBoolean(value: Boolean)
  extends JsonValue
case object JsonNull
  extends JsonValue
```

We are defining this because we plan to serialize `Expression`s to `JsonValue`s.

------

### Serializing `Expression` to Json

If we use this trait...

```scala
trait ConvertibleToJson {
  def json: JsonValue
}
```

. . .

... we have to implement `json` method in every subclass of `Expression`.

------

And we have created the following dependency (Unnecessary Coupling!):

![Expression-Json coupling]

[Expression-Json coupling]: diagrams/expression-json.png

------

Let's define `Json[A]` typeclass:

```scala
// Using the simplest & uniform typeclass encoding
// from a user's point of view
import simulacrum._

/**
  * `A` has an instance of Json[A] if `A` can be converted to
  * a JsonValue
  */
@typeclass trait Json[A] {
  def json(value: A): JsonValue
}
```

**Goal**: *EPIC DECOUPLING*
See [Simulacrum].

[Simulacrum]: https://bit.ly/2ROJvXH

------

Possible encoding of `Json[Expression]`:

```scala
object JsonImplicits {
  implicit val jsonExpression: Json[Expression] =
    new Json[Expression] {
      def json(expr: Expression): JsonValue =
        expr match {
          case Number(value)   => // Code Elided
          case Plus(lhs, rhs)  => // Code Elided
          case Minus(lhs, rhs) => // Code Elided
        }
    }
}
```

------

#### `case Number(value)`

```scala
case Number(value) => JsonNumber(value)
```

------

#### `case Plus(lhs, rhs)`

```scala
case Plus(lhs, rhs) => JsonObject(
  Map(
    "op" -> JsonString("+"),
    "lhs" -> json(lhs),
    "rhs" -> json(rhs)
  )
)
```

------

#### `case Minus(lhs, rhs)`

```scala
case Minus(lhs, rhs) => JsonObject(
  Map(
    "op" -> JsonString("-"),
    "lhs" -> json(lhs),
    "rhs" -> json(rhs)
  )
)
```

------

### Typeclass usage

(Assuming implementation uses `simulacrum`)

```scala
import JsonImplicits._
import Json.ops._ // Specific to `simulacrum`

object TypeclassExample extends App {
  val exp1: Expression = Plus(Number(1), Number(2))
  
  // Notice "extension" functionality
  // and familiar object.method syntax
  val json1: JsonValue = exp1.json
}
```

------

**Remark**: [Typeclass pattern]

[Typeclass pattern]: https://www.youtube.com/watch?v=sVMES4RZF-8

------
