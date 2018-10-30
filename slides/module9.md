---
layout: docs
title: "FP in Scala"
section: "slides"
author: M.C. Oscar Vargas Torres
---

### M.C. Oscar Vargas Torres

@oscarvarto  
Scala Developer and  
(dys)functional programmer

------

## Training objectives

- For comprehension
- Simple Refined types
- Implicit classes as an extension mechanism
- Smart Constructors
- Monoids & Semigroups
- Validation Pattern

------

## What skills will the trainee obtain?

- Using the most basic typeclasses in Scala, to write correct code.

------

## Agenda

- Introduction and prerequisites
- Refined
- Implicit classes
- Smart constructor pattern
- Monoids & Semigroups
- Validation

------

## Review of prerequisites

- For comprehension. Module 7
- [For comprehensions]

[For comprehensions]: https://leanpub.com/fpmortals/read#leanpub-auto-for-comprehensions

------

## The need to use more precise types

```scala
case class Person(name: String, age: Int)
// Bad input
val unrealPerson = Person("   ", -30)
```

Take a look at [Refined] documentation

[Refined]: https://github.com/fthomas/refined

------

```scala
// A person must have a NonEmpty name and a non-negative age.
object RefinedExample {
  type AgeAux = Interval.Closed[W.`0`.T, W.`127`.T]
  case class Age(n: Int Refined AgeAux)

  type NameAux = NonEmpty And Not[Forall[Whitespace]]
  case class Name(value: String Refined NameAux)

  case class Person(name: Name, age: Age)
  val spiderman = Person(Name("Peter Parker"), Age(25))
}
```

------

## Implicit classes enable extension syntax

```scala
import scalaz.syntax.std.string.ToStringOpsFromString

object stringsyntax {
  implicit class StringExtraOps(s: String) {
    /** Returns true if not empty and every character is whitespace
      *
      * @note using scala APIs
      */
    def isWhitespaceOnly: Boolean =
      s.nonEmpty && s.forall(_.isWhitespace)
  }
}
```

------

Now, in the call site:

```scala
import stringsyntax.StringExtraOps

val name = "   "
// call site
val b = name.isWhitespaceOnly
```

See [Implicit classes documentation]

[Implicit classes documentation]: https://docs.scala-lang.org/overviews/core/implicit-classes.html

------

## Smart constructors

```scala
package semigroupexamples

object Age {
  val MaxAge: Int = 127

  sealed trait AgeError
  case object AgeIsNegative extends AgeError
  case object AgeIsTooBig extends AgeError

  def apply(age: Int): Either[AgeError, Age] = age match {
    case a if a < 0 => Left(AgeIsNegative)
    case a if a > MaxAge => Left(AgeIsTooBig)
    case a => Right(new Age(a){})
  }
}

sealed abstract case class Age private(value: Int)
```

------

For details check:

- [Case Classes w/ Smart Ctors] by Rob Norris
- [Enforcing invariants in Scala datatypes]

[Case Classes w/ Smart Ctors]: https://gist.github.com/tpolecat/a5cb0dc9adeacc93f846835ed21c92d2
[Enforcing invariants in Scala datatypes]: https://www.cakesolutions.net/teamblogs/enforcing-invariants-in-scala-datatypes

------

### Name rules

- A name cannot be empty
- A name cannot be whitespace only

```scala
sealed trait NameError
case object NameCannotBeEmpty extends NameError
case object NameCannotBeWhitespaceOnly extends NameError
```

------

### Possible type to describe possibilities

```scala
Either[NonEmptyList[NameError], Name]
```

------

```scala
trait ValidationError

sealed trait AgeError extends ValidationError
// same age error cases as before.
sealed trait NameError extends ValidationError
// same name error casas as before
```

------

```scala
Either[NonEmptyList[ValidationError], Person]
```

Could model

1. the errors with both a `Name` and an `Age` (and others!), or
2. a validated `Person`

------

### But Either shortcuts on the first error

```scala
type Validated[A] = Either[Error, A]

def p(name: Validated[Name],
        age: Validated[Age]): Validated[Person] = for {
    n <- name
    a <- age
  } yield Person(n, a)
```

------

> The big advantage of restricting to Applicative is that `Validation` is explicitly for 
> situations where we wish to report all failures

*6.7.3 Validation* of the book [Functional Programming for Mortals with Scalaz]

[Functional Programming for Mortals with Scalaz]: https://leanpub.com/fpmortals/read

------

Monoid are more than intellectual curiosities.

**Make sure to read the contents for this module**.

------

> A **Semigroup** can be defined for a type if two values can be combined. The operation must be
> associative.
>
> A Monoid is a Semigroup with a zero element (also called empty or identity).

------

`MonoidExample1` uses

1. Addition over integers.
2. Multiplication over integers.

------

## Monoid Example 1

```scala
import scalaz._
import Tags.Multiplication
import Scalaz._

object MonoidExample1 {
  val x1: Int = 1 |+| 2

  // To use a different binary operation, use a tagged type
  val x2: Int @@ Multiplication =
   Multiplication(1) |+| Multiplication(2)
}
```

------

## Monoid Example 2

`Option[A] @@ Multiplication` monoid

```scala
import scalaz._
import Tags.Multiplication
import Scalaz._

object MonoidExample2 {
  type MultOption[A] = Option[A] @@ Multiplication

  implicit def optionMult[A]
  (implicit ev: Monoid[Option[A]]): Monoid[MultOption[A]] =
    new Monoid[MultOption[A]] {
      def zero: MultOption[A] = Tag(None)

      def append(f1: MultOption[A],
                 f2: => MultOption[A]): MultOption[A] =
        Tag(Tag.unwrap(f1) |+| Tag.unwrap(f1))
    }

  val x2: MultOption[Int] =
    Multiplication(1.some) |+| Multiplication(2.some)
}
```

------

## Monoid Example 3

Leveraging Scalaz' `LastMaybe[A]` monoid:

```scala
import scalaz._
import Maybe._
import Scalaz._

object MonoidExample3 {
  val x1: LastMaybe[Int] =
    1.just.last |+| 2.just.last

  val ns: EphemeralStream[LastMaybe[Int]] =
    (1 |=> 5).map(_.just.last)

  val lastN: LastMaybe[Int] =
    ns.fold

  val x2: Maybe[Int] @@ Tags.Last =
    empty.last
}
```

------

Monoids instances using

- Disjunction (Logical Or) over `Boolean`s
- Conjunction (Logical And) over `Boolean`s

------

```scala
object MonoidExample4 {
  // Disjunction: Or
  val b2: Boolean @@ Disjunction =
    true.disjunction |+| false.disjunction

  // Conjunction: And
  val b4: Boolean @@ Conjunction =
    true.conjunction |+| false.conjunction
}
```

------

## Validation of business rules

```scala
trait ValidationRules[A] {
  type E <: ValidationError
  type Rule = Reader[A, Validated[A]]

  def must(predicate: A => Boolean, error: E): Rule =
    Reader { a =>
      if (predicate(a)) Success(a)
      else Failure(NonEmptyList.nel(error, IList.empty))
    }

  def mustNot(predicate: A => Boolean, error: E): Rule =
    Reader { liftNel(_)(predicate, error) }

  def rules: NonEmptyList[Rule]

  implicit val firstSemigroup: Semigroup[A] =
    Semigroup.firstSemigroup

  def validate(candidate: A): Validated[A] =
    rules.sumr1.run(candidate)
}
```

------

### A better definition of `Validated[A]`

```scala
import scalaz.ValidationNel

package object semigroupexamples {
  type Validated[A] = ValidationNel[ValidationError, A]
}
```

------

## Name validation

```scala
object Name extends ValidationRules[Name] {
  type E = NameError

  sealed trait NameError extends ValidationError
  case object NameCannotBeEmpty extends NameError
  case object NameCannotBeWhitespaceOnly extends NameError

  override def rules: NonEmptyList[Rule] =
    NonEmptyList(
      mustNot(_.value.isWhitespaceOnly,
        NameCannotBeWhitespaceOnly),
      mustNot(_.value.isEmpty, NameCannotBeEmpty)
    )

  def apply(s: String): Validated[Name] =
    validate(new Name(s) {})
}

sealed abstract case class Name private(value: String)
```

------

### Age validation

```scala
package semigroupexamples

import scalaz.NonEmptyList

object Age extends ValidationRules[Age] {
  type E = AgeError

  sealed trait AgeError extends ValidationError
  case object AgeNegative extends AgeError
  case object AgeTooBig extends AgeError

  val MaxAge: Int = Byte.MaxValue.toInt // 127

  override def rules: NonEmptyList[Rule] =
    NonEmptyList(
      mustNot(_.value < 0, AgeNegative),
      mustNot(_.value > MaxAge, AgeTooBig)
    )

  def apply(age: Int): Validated[Age] =
    validate(new Age(age) {})
}

sealed abstract case class Age private(value: Int)
```

------

## Using Applicative syntax

```scala
package semigroupexamples

import scalaz.syntax.apply._

object Person {
  def apply(name: String, age: Int): Validated[Person] =
    ^(Name(name), Age(age)){ Person.apply }
}

case class Person(name: Name, age: Age)
```