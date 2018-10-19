---
layout: docs
title: "Functions"
section: "slides"
author: M.C. Oscar Vargas Torres
---

### M.C. Oscar Vargas Torres

@oscarvarto  
Scala Developer and  
(dys)functional programmer

------

### Training objectives

- Understanding Function Types
- Expressing Type Constraints with Type Classes and Upper/Lower Bounds
- Composition
- Practical examples using Functional Programming abstractions based on *functions*

**FUNCTIONS ALL THE WAY!**

------

### What skills will the trainee obtain?

- Experience expressing programs with simple functions.
- Exploiting different kinds of generics programming available in Scala.

------

### Agenda

1. Function Types
2. Composition
3. Currying
4. Generics and parametric polymorphism
5. Functions and dependency injection

------

**Note**: The [Scala Language Specification]
can be used as an authoritative reference in case of doubts.

[Scala Language Specification]: https://www.scala-lang.org/files/archive/spec/2.12/

------

## Function types

The following is an example of a simple function:

```{.scala .listing}
val triple = (n: Int) => n * 3
```

The *type* of this function literal is: `Int => Int`.

------

We could have used the *type annotation* to define triple:

```{.scala .listing}
val triple: Int => Int = n => n * 3
```

That can be read like:

> `triple` is a function that accepts an `Int` parameter and returns an `Int`

------

In general, to declare a **function type**, write:

```{.scala .listing}
(A, B, ...) => C
```

where

* `A`, `B`, ... are the types of the input parameters; and
* `C` is the type of the result.

------

### Note: Naming conventions

Scala names are case sensitive. See [Naming conventions for constants, values, variables
and methods].

Can you tell why the following would *not* be a good
name?

```{.scala .listing .admonition .danger}
val Triple = (n: Float) => n * 3.0f
```

[Naming conventions for constants, values, variables and methods]: https://docs.scala-lang.org/style/naming-conventions.html#constants-values-variable-and-methods

------

According to [Function Types]:

- Function types associate to the right, e.g.  
  `S => T => U` is the same as `S => (T => U)`
- An argument type of the form `=> T` represents a call-by-name parameter of type `T`.

[Function Types]: https://www.scala-lang.org/files/archive/spec/2.12/03-types.html#function-types

------

### Relationship between methods and functions: [Eta--expansion]

```{.scala .listing}
def f1(n: Int): Int = n * 2
# Placeholder syntax
val f2: Int => Int = f1 _
# eta-expansion: equivalent to placeholder syntax
val f3: Int => Int = n => f1(n)
```

Given this close relationship, you will find methods referred to as "functions", although **they are
not strictly the same**.

[Eta--expansion]: https://www.scala-lang.org/files/archive/spec/2.12/06-expressions.html#method-values

------

### Return value of a block

The last expression of a block becomes the value that the function returns. For example, the
following method returns the value of `r` after the `for` loop (no need for the `return`{.scala} keyword):

```{.scala .listing}
def fac(n: Int) = {
  var r = 1
  for (i <- 1 to n)
    r = r * i
  r
}
```

------

### Recursive functions/methods must specify the return type

```{.scala .listing}
def fac(n: Int): Int =
  if (n <= 0) 1
  else n * fac(n - 1)
```

------

### Varargs syntax

```{.scala .listing}
def sum(args: Int*): Int = {
  var result = 0
  for (arg <- args)
    result += arg
  result

val s = sum(1 to 5: _*)
```

------

### Procedures have `Unit` return value

```{.scala .listing}
def box(s : String) { // Look carefully: no =
  // contents elided
}

// (Equivalent) Explicit return type
def box(s : String): Unit = {
  // contents elided
}
```

------

### Scaladoc for `Function1`

Open [Current Scala Standard Library Scaladoc] and search documentation for trait
`Function1`.

Search the following methods:

- `apply`
- `andThen` and
- `compose`.

[Current Scala Standard Library Scaladoc]: https://www.scala-lang.org/api/current/

------

#### `apply`

The definition of `succ` is a shorthand for the anonymous class definition `anonfun1`:

```{.scala .listing}
object Main extends App {
   val succ = (x: Int) => x + 1
   val anonfun1 = new Function1[Int, Int] {
     def apply(x: Int): Int = x + 1
   }
   assert(succ(0) == anonfun1(0))
}
```

------

#### `compose`

It has the following signature

```{.scala .listing}
def compose[A](g: (A) => T1): (A) => R
```

It models the mathematical function composition. For example, if $f(x) = x + 1$ and $g(x) = 2x$,

\begin{align}
(f \cdot g)(x) &= f(g(x)) = 2x + 1 \\
(g \cdot f)(x) &= g(f(x)) = 2(x + 1)
\end{align}

------

Using Scala:

```{.scala .listing}
val f: Int => Int = x => x + 1
val g: Int => Int = x => 2 * x

// f "after" g, or g "then" f
// fg(x) = 2x + 1
val fg: Int => Int = f compose g

// g "after" f, or f "then" g
// gf1(x) = 2(x + 1)
val gf1: Int => Int = f andThen g
gf1(3)
// equivalently
val gf2: Int => Int = g compose f
gf2(3)
```

------

#### DANGER: Scala "Universal" `toString` method

Scala creates a `toString()`{.scala} method automatically for all classes. Functions also get a
useless implementation for `toString()`{.scala}.

**Note**: `FunctionN` traits are not sealed:
[you can `override`{.scala} it's implementation]{.smallcaps}.

------

## Composition

[A wonderful tool to get complex solutions from smaller building blocks]{.smallcaps}.

`Atto`, is a parsing library that uses `andThen` to build a new parser from
smaller parsers. Spend some time studying the section on [basic parsers].

[basic parsers]: http://tpolecat.github.io/atto/docs/first-steps.html

------

### Exercises

- Discuss your understanding on Basic Parsers with others.
- Following [Parsing log entries] tutorial, replicate the example given in your development machine.

[Parsing log entries]: http://tpolecat.github.io/atto/docs/next-steps.html

------

## Currying

Let's take a look at `Functions2` trait scaladoc. It is used for functions that accept *two*
arguments. For example:

```{.tut .scala .listing}
val add1: (Int, Int) => Int = _ + _
```

A *curried* version of the above is:

```{.tut .scala .listing}
val add2: Int => Int => Int = a => b => a + b
```

------

## [Currying]{.smallcaps}

```{.scala .listing}
// Convert
(A, B) => C
// into
A => (B => C)
```

- Turning a function that takes two arguments into a function that takes one argument.
- That function returns a function that consumes the second argument.

([Scala for the Impatient, 2nd Edition])

[Scala for the Impatient, 2nd Edition]: http://www.informit.com/store/scala-for-the-impatient-9780134540634?ranMID=24808

------

You can get a curried version of a `FunctionN` by calling `curried`:

```{.tut .scala .listing}
add1.curried // returns an equivalent of add2
```

On the other hand, you can call `Function.uncurried` to "uncurry":

```{.tut .scala .listing}
Function.uncurried(add2) // returns an equivalent of add1
```

------

Currying buys us (at least) 2 things in Scala:

1. Partial application
2. Improved type inference

------

### [Partial application]{.smallcaps}

The ability to apply only some of a function's arguments

```{.tut .scala .listing}
val addTen: Int => Int = add2(10)
```

------

### [Improved type inference]{.smallcaps}

From the Scala Standard Library, `List.foldRight()`:

```{.scala .listing}
def foldRight[B](z: B)(op: (A, B) => B): B
```

- The *type inferencer* can figure out what `B` is from the type of `z`, and
- then it can use that information when analyzing the function that is passed for `op`.

------

## Generics and parametric polymorphism

------

### [Generics]{.smallcaps}

- Generics is a powerful tool from modern programming languages
- "[For academics only]"?

------

Generic collections in mainstream languages

- [Java Collections API]
- [C\# generics collections]

[For academics only]: https://blogs.msdn.microsoft.com/dsyme/2011/03/15/netc-generics-history-some-photos-from-feb-1999/
[Java Collections API]: https://download.java.net/java/early_access/jdk11/docs/api/java.base/java/util/package-summary.html
[C\# generics collections]: https://docs.microsoft.com/en-us/dotnet/csharp/programming-guide/concepts/collections

------

[Scala has higher level of support for generic programming]{.smallcaps}, than Java and C\#.
</br></br>

Oliveira, Bruno CdS, Adriaan Moors,
and Martin Odersky.
"Type Classes as Objects and Implicits."
In ACM Sigplan Notices, 45:341–360. ACM, 2010.

------

### Polymorphism

- [Polymorphic]{.smallcaps}: made of many forms
- [Monomorphic]{.smallcaps}: made of one form

------

#### Kinds of polymorphism (in Scala)

- Subtyping/dynamic polymorphism
- Parametric polymorphism
- Ad hoc (or constrained) polymorphism

------

#### Subtyping/dynamic polymorphism

Constraining type parameters with the [upper type bound] `T <: Shape`

```{.scala .listing}
sealed trait Shape
case class Square(l: Double) extends Shape
case class Circle(radius: Double) extends Shape

// Constraining T to be a subtype of Shape
// area is still a generic method
def area[T <: Shape](t: T) = ???
```

Documentation on [lower type bounds].

[upper type bound]: https://docs.scala-lang.org/tour/upper-type-bounds.html
[lower type bounds]: https://docs.scala-lang.org/tour/lower-type-bounds.html

------

#### Parametric polymorphism

Param. Polym. $\approx$ [Unconstrained type variables]{.smallcaps}

From `List.map`'s type signature:

```{.scala .listing}
final def map[B](f: (A) => B): List[B]
```

- `List[A]` is a polymorphic/generic collection.
- `List.map` is a polymorphic method.

------

Making `type A = Int`, `type B = String`:

```{.scala .listing}
val ns = List(1, 2, 3, 4)
val ss = ns.map(_.toString)
```

------

[Classes can be parametric also]{.smallcaps}, for example:

```{.scala .listing}
case class Pair[A, B](first: A, second: B) {
  def swap: Pair[B, A] = Pair(second, first)
}
```

------

#### Ad hoc polymorphism

**Prerequisite**: [Context Bounds]

[Context Bounds]: https://docs.scala-lang.org/tutorials/FAQ/context-bounds.html

------

*Types variables can be constrained* in generic functions, by using **[typeclasses]{.smallcaps}**.

`Ordering[T]` from the Scala Standard Library.

```{.scala .listing}
// Type class Constraint
import scala.math.Ordering.Implicits._

def myMax[T : Ordering](x: T, y: T): T =
  if (x > y) x else y;
```

------

##### Typeclass usage example

An excerpt from an implementation of the *k-means* algorithm ([Spire]):

[Spire]: https://github.com/non/spire

```{.scala .listing}
/**
  * Returns a collection of k points which are the centers
  * of k clusters of `points0`.
  */
def kMeans[V, @sp(Double) A, CC[V] <: Iterable[V]]
  (points0: CC[V], k: Int)
  (implicit vs: NormedVectorSpace[V, A],
            order: Order[A],
            cbf: CanBuildFrom[Nothing, V, CC[V]],
            ct: ClassTag[V]): CC[V] = {
```

------

###### Main loop of the k-means algorithm

```{.scala .listing}
@tailrec
def loop(assignments0: Array[Int],
         clusters0: Array[V]): Array[V] = {
  val assignments = assign(clusters0)
  if (assignments === assignments0) {
    clusters0
  } else {
    // ***** CODE ELIDED *****
    loop(assignments, clusters)
  }
}
```

------

## Functions and Dependency Injection

**Prerequisite**: [Dead-Simple Dependency Injection]

[Dead-Simple Dependency Injection]: https://www.youtube.com/watch?v=ZasXwtTRkio

------

### `Reader[A]` monad abstraction

```{.scala .listing}
object Reader {
  implicit def reader[A, B](f: A => B): Reader[A, B] =
    Reader(f)
  def pure[C, A](a: A): Reader[C, A] =
    (_: C) => a
}
case class Reader[C, A](g: C => A) {
  import Reader._
  def apply(c: C): A = g(c) // or "run"
  def map[B](f: A => B): Reader[C, B] =
    (c: C) => f(g(c))
  def flatMap[B](f: A => Reader[C, B]): Reader[C, B] =
    (c: C) => f(g(c))(c)
}
```

------

Using cats' `Reader[A]` implementation:

```{.scala .listing}
import java.sql.{Connection, DriverManager, PreparedStatement}
import cats.data.Reader

object DB {
  type DB[A] = Reader[Connection, A]
  type UserId = String
  type Pwd = String

  implicit def reader[A](g: Connection => A): DB[A] = Reader(g)
  def pure[A](a: A): DB[A] = Reader(_ => a)
  // Code elided
}
```

------

Some primitive operations that are delaying the definition of a `Connection` database:

```{.scala .listing}
def setUserPwd(id: UserId,
               pwd: Pwd): Connection => Unit =
  c => {
    val stmt: PreparedStatement =
      c.prepareStatement("update users set pwd = ? where id  = ?")
    stmt.setString(1, pwd)
    stmt.setString(2, id)
    stmt.executeUpdate()
    stmt.close()
  }
```

------

```{.scala .listing}
def getUserPwd(id: UserId): Connection => Pwd =
  c => {
    val stmt: PreparedStatement =
      c.prepareStatement("select pwd where id = ?")
    stmt.setString(1, id)
    val resultSet = stmt.executeQuery()
    val pwd = resultSet.getString(0)
    stmt.close()
    pwd
  }
```

------

Now we are able to express database "actions", without referring to a database `Connection` at all:

```{.scala .listing}
def changePwd(userId: UserId,
              oldPwd: Pwd,
              newPwd: Pwd): DB[Boolean] =
  for {
    pwd <- getUserPwd(userId)
    eq <- if (pwd == oldPwd) for {
      _ <- setUserPwd(userId, newPwd)
    } yield true
    else pure(false)
  } yield eq
```

------

And we can define different database connection providers: one for testing and some other for
production purposes:

```{.scala .listing}
abstract class ConnProvider {
  def apply[A](f: DB[A]): A
}

lazy val sqliteTestDB: ConnProvider =
  mkProvider("org.sqlite.JDBC", "jdbc:sqlite::memory:")

lazy val mysqlProdDB: ConnProvider =
  mkProvider(
    "org.gjt.mm.mysql.Driver",
    "jdbc:mysql://prod:3306/?user=one&password=two")
```

------

We can define *interpreters* to process our `DB[A]` actions:

```{.scala .listing}
def mkProvider(driver: String, url: String): ConnProvider =
  new ConnProvider {
    override def apply[A](f: DB[A]): A = {
      //Class.forName(driver)
      val conn = DriverManager.getConnection(url)
      try {
        f(conn)
      }
      finally {
        conn.close()
      }
    }
  }
```

------

And "programs" or descriptions of the computations on our `DB[A]` actions.

```{.scala .listing}
def myProgram(userId: UserId): ConnProvider => Unit =
  r => {
    println("Enter old password")
    val oldPwd = readLine()
    println("Ender new password")
    val newPwd = readLine()
    r(changePwd(userId, oldPwd, newPwd))
  }
```

------

The actual "injection" of (Testing Vs. Production) of a `ConnectionProvider`:

```{.scala .listing}
def runInTest[A](f: ConnProvider => A): A =
  f(sqliteTestDB)

def runInProduction[A](f: ConnProvider => A): A =
  f(mysqlProdDB)

def main(args: Array[String]) =
  runInTest(myProgram(args(0)))
```