val triple = (n: Int) => n * 3
//def triple(n: Int): Int = n * 3
val n1 = triple(3)
val n2 = triple(4)

val family = List(
  "Oscar",
  "Beatriz",
  "Alex",
  "Joshua"
)

// val names = family.mkString(", ")
// Infix syntax
val names = family mkString ", "

// Single precision number
// This example is only to illustrate that capitalization matters
val Triple = (n: Float) => n * 3.0f

// This serves as an example of bad name. Take a look at
// https://docs.scala-lang.org/style/naming-conventions.html#constants-values-variable-and-methods

// Parts of a function:
// Scala Language Specification: Section 3.2.9
// https://www.scala-lang.org/files/archive/spec/2.12/03-types.html#function-types

// Focus on:
// * simple notation
// * right associativity

// Do not let covariance/contravariance concepts make your explanation more complex than necessary at this stage.

// Provide examples of functions

// Use portions of Scala for the Impatient, 2nd. Edition, sections:
// * 2.7 To mention that there is a close relationship between def methods and functions.
// Explain that you can get a function from a method
def f1(n: Int): Int = n * 2

val f1p = f1 _
f1p(3)

// Explain that the last expression of a block of code is what Scala uses as the value that the function returns.

// Explain that you don't use the `return` keyword to specify the "result" of the function.

// Explain that you must specify the return type for recursive functions

// Edge case of functions/methods: variable number of arguments (varargs)

// Explain that "procedures" have `Unit` return type

// Use Scaladoc for Function1 to explain important operators/methods:
// * apply

// Use javap to show the generated code for a Function1 or show people what the Scala spec teaches about it on Section 3.2.9 (cited above).

// * compose

// Explain `compose` before `andThen`. Use simple example from arithmetic to explore the concept using scala repl.

// Use this opportunity to show people how they can get information from repl, e.g. type. Teach people to reason based on type signature.

// * andThen
// Compare with type signature provided for andThen.
// Make people solve a simple exercise using both `compose` and `andThen`.

// * toString
// TODO: is there a good usage for toString default implementation?
// Maybe this is a good opportunity to show people one of the warts of Scala (universal toString method in every object. Sometimes it doesn't make sense...)

// Use Function2 scaladoc to introduce people to important concept of curry/uncurry
// Explain that tupled/curried methods are useful.

// Use concepts from Section 5.4 of "Haskell programming from First Principles" to explain Currying. Adapt examples to Scala.

/* Parametric polymorphism: Refer people to Scala Specification, section 3.2.4 Parameterized types for an explanation of a well/ill formed parameterized type */

// Don't forget to *Start simple*. Take them slow ... with lots of examples
// Refer people to section 3 of the Scala specification to study and learn about the different "Types" supported by Scala.

// Put a lot of enthusiasm on Parametric Polymorphism. Explain people that it can help reduce bugs and the number of correct implementation.
// Refer people to literature on the subject, so that advanced students can study it without getting bored about a "well known/basic" subject.

// This might be material for more advanced students.
// Use section 5.5 from "Haskell book" for good concepts/explanations.
// Review Exercises about parametricity. Try to implement those for Scala.

// Simpler material: use ideas from Section 18.2 Generic Functions of "Scala for the Impatient" to help people understand the basics. Very little material on this section :(

// Select/Design examples of Parametric Polymorphism from ML/Unsupervised Learning/ or numerical problems.
// Use an inferior language like C/Go to explain that lack of generics leads to repetition/boilerplate and more bugs.
// Examples of parametric polymorphism:

case class Pair[A, B](first: A, second: B) {
  def swap: Pair[B, A] = Pair(second, first)
}

// Valuable links on generics:
// https://blogs.msdn.microsoft.com/dsyme/2011/03/15/netc-generics-history-some-photos-from-feb-1999/  <== Don Syme remember how he was told
// by product team members that "generics is for academics only"
// For example, one possible alternative to generics in go is "Copy & Paste":
// https://appliedgo.net/generics/

// https://docs.oracle.com/javase/10/docs/api/java/util/package-summary.html  <== Java Collections API. Uses generics
// https://docs.microsoft.com/en-us/dotnet/csharp/programming-guide/concepts/collections#systemcollections-classes <== Advice from Microsoft to prefer generic collections instead of legacy types in the `System.Collections` namespace

// Use scaladoc for Immutable List:
// https://www.scala-lang.org/api/current/scala/collection/immutable/List.html to explain some generic/parametric algorithm/method implementations:

// Example of a method that uses ad-hoc polymorphism
// https://github.com/non/spire/blob/master/examples/src/main/scala/spire/example/kmeans.scala#L48-L73

// Generics is not the same than Parametric Polymorphism
// Parametric Polymorphism refers to type variables, or parameters, that are fully polymorphic. When unconstrained by a typeclass, their final, concrete type can be anything.

// Support for generics in Scala: https://softwareengineering.stackexchange.com/a/215383
// http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.176.5300&rep=rep1&type=pdf
// "Type Classes as Objects and Implicits" by Oliveira, Moors, Odersky
// (stored in Documents -- Mac)
// See page 17, for a table with some of the features supported in Scala for Generic Programming.