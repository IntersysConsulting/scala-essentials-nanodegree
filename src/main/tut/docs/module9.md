---
layout: docs
title: "Functional programming"
section: "docs"
author: M.C. Oscar Vargas Torres
---

# Module 9: Functional programming

## Introduction and prerequisites

### For comprehensions

The following resources could be helpful:

- Module 7 material
- [Essential Scala], chapter 5
- [Better monadic for plugin]
- [For comprehensions] from Sam Halliday (@fommil)

[Essential Scala]: https://underscore.io/books/essential-scala/
[Better monadic for plugin]: https://github.com/oleg-py/better-monadic-for
[For comprehensions]: https://leanpub.com/fpmortals/read#leanpub-auto-for-comprehensions

### Using more precise types

If you have been practicing with Scala, it is probable that you have noticed that using precise
types reduces bugs. For example, given the following `Person` definition, you could possibly define
instances that do not model reality:

```scala
case class Person(name: String, age: Int)
// Bad input
val unrealPerson = Person("   ", -30)
```

because, arguably, a person's name cannot be "   ", and all ages are non-negative, and are
reasonably (arbitrarily) bounded by 127 (or 200, if you wish).

If it is possible, strive to use types that completely eliminate trivial bugs/errors. A simple way
to define more precise/refined types, is by using [Refined]. The above example could be rewritten
with:

```scala
import eu.timepit.refined.W
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.boolean.{And, Not}
import eu.timepit.refined.char.Whitespace
import eu.timepit.refined.collection.{Forall, NonEmpty}
import eu.timepit.refined.numeric.Interval

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

Now, if we have an instance of `Person`, we are sure that it has a name that is not blank, and has a
valid age (according to above definition).

[Refined]: https://github.com/fthomas/refined

### Adding/extending types with convenient syntax through implicit classes

One possible way to define a new operation on an existing type is to create an utility method inside
a (singleton) object:

```scala
object StringUtilities {
  /** Returns true if not empty and every character is whitespace
    *
    * @note using scala APIs
    */
  def isWhitespaceOnly(s: String): Boolean =
    s.nonEmpty && s.forall(_.isWhitespace)
}
```

Then you can to use the method as usual:

```scala
import StringUtilities._

val name: String = "   "
// call site
val b = isWhiteSpaceOnly(name)
```

Calling the method with the convenient syntax `instance.method()` could be achieved with
"implicit classes":

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

Now, in the call site:

```scala
import stringsyntax.StringExtraOps

val name = "   "
// call site
val b = name.isWhitespaceOnly
```

See [Implicit classes documentation] for more details.

[Implicit classes documentation]: https://docs.scala-lang.org/overviews/core/implicit-classes.html

******

#### Note: Using Java's SAM methods from Scala

Next snippet contains an alternative (using Java's 8+ APIs) implementation for `isWhitespaceOnly`
"extension" method. Take a look at the scaladoc for more information.

```scala
object stringsyntax {
  implicit class StringExtraOps(s: String) {
    /** Returns true if not empty and every character is whitespace
      *
      * @note using Java APIs
      *
      * @note using lambda syntax for SAM types
      * @see https://www.scala-lang.org/news/2.12.0/#lambda-syntax-for-sam-types
      * @see https://www.scala-lang.org/files/archive/spec/2.12/06-expressions.html#sam-conversion
      */
    def isWhitespaceOnly: Boolean = {
      !s.isEmpty &&
        s.chars().allMatch(Character.isWhitespace(_))
    }
  }
}
```

******

## Smart constructors

Other possibility to avoid nonsensical data, and to enforce business/reality rules, is to use the
"smart constructor" pattern. We are explaining the basics here, and in future sections will get a
(alledgedlly) better encoding of the pattern. We want to **enforce correctness by construction**.

For this example, we are going to use a person's age, and enforce the two following rules:

- An age must be non-negative: zero or positive.
- An age must be upper-bounded by a reasonable max integer: 127.

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

The above code forbids the creation of `Age` instances that do not obey the above "business" rules.
For more information on the rationale for the unlikely-sounding combination of keywords
`sealed abstract case class` above, see [Case Classes w/ Smart Ctors] by Rob Norris.

An alternative encoding using macros is explained in [Enforcing invariants in Scala datatypes]. We
will stick to the simpler variant illustrated above with `Age`.

[Case Classes w/ Smart Ctors]: https://gist.github.com/tpolecat/a5cb0dc9adeacc93f846835ed21c92d2
[Enforcing invariants in Scala datatypes]: https://www.cakesolutions.net/teamblogs/enforcing-invariants-in-scala-datatypes

### `NonEmptyList` data structure

Scalaz defines a `NonEmtyList[T]` that can be useful for its non-emtyness property. In the context
of our discussion on enforcing invariants, we will use it to accumulate the errors that arise
during validation of business rules.

That is, if there were problems/validation errors, you should have at least one of them. If there
were no errors, you should be able to get a valid instance. We will use the following rules to model
a person's name:

- A name cannot be empty
- A name cannot be whitespace only

The following ADT (Algebraic Data Type) models the errors that result from violation of the
aforementioned rules:

```scala
sealed trait NameError
case object NameCannotBeEmpty extends NameError
case object NameCannotBeWhitespaceOnly extends NameError
```

So, given an input `String`, the following type models what could happen during validation of a
name:

```scala
Either[NonEmptyList[NameError], Name]
```

Validation of rules would result in either one of the following two possibilities:

1. A (non-empty) list of name errors
2. A valid name

### Modelling validation errors with `Either`

Say we want to validate both a `Person`'s `Name` and an his/her `Age` using the previously defined
rules. We are going to use the following variation for the error type

```scala
trait ValidationError

sealed trait AgeError extends ValidationError
// same age error cases as before.

sealed trait NameError extends ValidationError
// same name error casas as before
```

So, the following type could be used to model

1. all the possible things that can go wrong with both a `Name` and an `Age` (and some others that
   we haven't yet considered!), or
2. a validated `Person`

```scala
Either[NonEmptyList[ValidationError], Person]
```

The problem with `Either` is that a for comprehension using the `Either` Monad, shortcuts on the
first error, and do not report all the things that could have gone wrong during the validation
process. Study the following snippet (or skip it if you already understand which the problem is):

```scala
object EitherExample {
  type Error = String
  type Name = String
  type Age = Int
  type Validated[A] = Either[Error, A]

  val n1: Validated[Name] = Left("Name is empty")
  val n2: Validated[Name] = Left("Name is whitespace only")
  val n3: Validated[Name] = Right("Peter Parker")

  val a1: Validated[Age] = Left("Age is negative")
  val a2: Validated[Age] = Left("Age is too big")
  val a3: Validated[Age] = Right(25)

  case class Person(name: Name, age: Age)

  def p(name: Validated[Name],
        age: Validated[Age]): Validated[Person] = for {
    n <- name
    a <- age
  } yield Person(n, a)

  // Reports "Age is too big" error, OK
  val p1: Validated[Person] = p(n3, a2)

  // A valid `Person` instance, OK
  val p2: Validated[Person] = p(n3, a3)

  // Only reports "Name is empty" error
  // but ignores "Age is negative" error
  // *BUT* we want all errors reported!
  val p3: Validated[Person] = p(n1, a1)
}
```

### `Validation` can accumulate all the errors

From section *6.7.3 Validation* of the book [Functional Programming for Mortals with Scalaz]:

> The big advantage of restricting to Applicative is that `Validation` is explicitly for situations
> where we wish to report all failures, whereas `Disjunction` is used to stop at the first failure.
> To accommodate failure accumulation, a popular form of `Validation` is `ValidationNel`, having a
> `NonEmptyList[E]` in the failure position.

(Note: `Either[E, A]` presents the same problem than `Disjunction[E, A]` because both have Monad
instances).

[Functional Programming for Mortals with Scalaz]: https://leanpub.com/fpmortals/read

But to be able to fully understand the details, we need to learn some other basic concepts first.

## Monoids & Semigroups

### Warm-up and additional links

For a mostly mathless discussion of monoids, please take a look at [Monoid without tears], by Scott
Wlaschin. You will find more examples in part 2 of this series, in [Monoids in practice].

The following links could help you realize further monoid applications. These are much more than
intelectual curiosities!

- [Google’s MapReduce Programming Model — Revisited]
- [Streaming examples for Apache Spark]
- [Hourglass: a Library for Incremental Processing on Hadoop]
- [Apache Datafu Hourglass source code]

For a discussion using more math, take a look at [Category Theory for Programmers].

Finally, for the Scalaz perspective, please take a look at [Appendable Things].

### Appendable things with Scalaz

#### `MonoidExample1`

From [Appendable Things]:

> A **Semigroup** can be defined for a type if two values can be combined. The operation must be
> associative.
>
> A Monoid is a Semigroup with a zero element (also called empty or identity).

(Take a look at additional examples/explanations there!)

`MonoidExample1` uses

1. Addition over integers.
2. Multiplication over integers.

The first set of imports (inside a block comment) are more specific, and the second block, is the
easiest to write in order to make the code compile.

```scala
/*
import scalaz.Tags.Multiplication
import scalaz.std.anyVal.{intInstance, intMultiplicationNewType}
import scalaz.syntax.monoid._
import scalaz.{@@, Tag}
*/

import scalaz._
import Tags.Multiplication
import Scalaz._

object MonoidExample1 {
  // implicit val intInstance: Monoid[Int] with Enum[Int] with Show[Int]
  // https://github.com/scalaz/scalaz/blob/v7.2.26/core/src/main/scala/scalaz/std/AnyVal.scala#L234
  // Uses Arithmetic sum as the binary operation
  // https://github.com/scalaz/scalaz/blob/v7.2.26/core/src/main/scala/scalaz/std/AnyVal.scala#L237
  val x1: Int = 1 |+| 2

  // To use a different binary operation, use a tagged type
  val x2: Int @@ Multiplication = Multiplication(1) |+| Multiplication(2)
}
```

There can only be one implementation of a typeclass for a given type parameter. To avoid breaking
typeclass coherence, `MonoidExample1` uses type tags (notice `@@` and `Multiplication`).

#### `MonoidExample2`

Defining an `Option[A] @@ Multiplication` monoid:

```scala
/*
import scalaz.Tags.Multiplication
import scalaz.std.anyVal.intInstance
import scalaz.std.option.optionMonoid
import scalaz.std.option.optionSyntax._
import scalaz.syntax.monoid._
import scalaz.{@@, Monoid, Tag, Tags}
*/

import scalaz._
import Tags.Multiplication
import Scalaz._

object MonoidExample2 {

  val x1: Option[Int] = 1.some |+| 2.some

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

#### `MonoidExample3`

Leveraging Scalaz' `LastMaybe[A]` monoid:

```scala
/*
import scalaz.LastOption
import scalaz.Tags.Last
import scalaz.std.option.optionLast
import scalaz.std.option.optionSyntax._
import scalaz.syntax.monoid._
*/

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

#### `MonoidExample4`

Monoids instances using

- Disjunction (Logical Or) over `Boolean`s
- Conjunction (Logical And) over `Boolean`s

```scala
import scalaz.@@
import scalaz.Tags.{Conjunction, Disjunction}
import scalaz.std.anyVal.{booleanConjunctionNewTypeInstance, booleanDisjunctionNewTypeInstance}
import scalaz.syntax.monoid._
import scalaz.syntax.std.boolean._
//import scala.language.postfixOps

object MonoidExample4 {
  // Disjunction: Or
  val b1: Boolean @@ Disjunction =
    Disjunction(true) |+| false.disjunction

  val b2: Boolean @@ Disjunction =
    true.disjunction |+| false.disjunction

  val b3: Boolean @@ Disjunction =
    true.|\/| |+| false.|\/|

  // Conjunction: And
  val b4: Boolean @@ Conjunction =
    true.conjunction |+| false.conjunction

  val b5: Boolean @@ Conjunction =
    Conjunction(true) |+| Conjunction(false)

  val b6: Boolean @@ Conjunction =
    true.|∧| |+| false.|∧|
}
```

[Monoids without tears]: https://fsharpforfunandprofit.com/posts/monoids-without-tears/
[Monoids in practice]: https://fsharpforfunandprofit.com/posts/monoids-part2/
[Google’s MapReduce Programming Model — Revisited]: https://userpages.uni-koblenz.de/~laemmel/MapReduce/paper.pdf
[Streaming examples for Apache Spark]: https://github.com/mesos/spark/pull/480
[Hourglass: a Library for Incremental Processing on Hadoop]: https://www.slideshare.net/matthewterencehayes/hourglass-27038297
[Apache Datafu Hourglass source code]: https://github.com/apache/datafu
[Category Theory for Programmers]: https://github.com/hmemcpy/milewski-ctfp-pdf/releases/download/v1.0.0/category-theory-for-programmers.pdf
[Appendable Things]: https://leanpub.com/fpmortals/read#leanpub-auto-appendable-things

## Leveraging `Validation` and `ValidationNel`

The following trait can be used for validation of business rules:

```scala
package semigroupexamples

/*
import scalaz._
import Scalaz._
import Validation.liftNel
*/

import scalaz.Validation.liftNel
import scalaz.syntax.foldable1._
import scalaz.{Failure, IList, NonEmptyList, Reader, Semigroup, Success}

abstract class ValidationError

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

A better definition of `Validated[A]` is:

```scala
import scalaz.ValidationNel

package object semigroupexamples {
  type Validated[A] = ValidationNel[ValidationError, A]
}
```

Now, validation of `Name` with the Smart Constructor pattern is:

```scala
package semigroupexamples

import scalaz.NonEmptyList
import stringsyntax.StringExtraOps

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

Whereas `Age` now is:

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

Finally, by means of `Apply` syntax:

```scala
package semigroupexamples

import scalaz.syntax.apply._

object Person {
  def apply(name: String, age: Int): Validated[Person] =
    ^(Name(name), Age(age)){ Person.apply }
}

case class Person(name: Name, age: Age)
```

## Functional data structures and programming patterns