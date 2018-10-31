---
layout: docs
title: "Idiomatic Scala"
section: "docs"
author: M.C. Oscar Vargas Torres
---

# Module 7: Idiomatic Scala

## Collections

### Lists

#### Creation of Lists

A list of `Int`s, notice that type inference works here and assigns the most obvious type here.

```tut
val x = List(1, 2, 3)
```

Notice also, that the default implementation for `List`s is from `scala.collection.immutable` namespace.

Take a look at the several methods provided by the [Scala Standard Library for Immutable Lists].

You should be able to use at least the following higher-order methods:

- `map`. Review Section 6.2.2 of the "Essential Scala" book for more on this.
- `flatMap`. Review Section 6.2.3.
- `fold` and related methods. Take a look at Section 6.2.4.
- `foreach`. Review Section 6.2.5.
- `filter`
- `collect`
- `contains`
- `length`
- `headOption`
- `take`
- `zip`

[Scala Standard Library for Immutable Lists]: https://www.scala-lang.org/api/current/scala/collection/immutable/List.html

Discuss with your instructor and your pairs the implications of covariance in the Scala implementation for `List[+A]`.

### `Map`s and `Set`s

These are generic collections also (`Map[K, +V]`, `Set[A]`), and by default, an immutable collection.

### Maps

- [Immutable Map] scaladoc.
- [Immutable Map companion object] scaladoc.

#### Avoid non-total methods

**Pro-tip**: Strive to use total variants of accessor methods.

[Immutable Map]: https://www.scala-lang.org/api/current/scala/collection/immutable/Map.html
[Immutable Map companion object]: https://www.scala-lang.org/api/current/scala/collection/immutable/Map$.html

### Sets

- [Immutable Set] scaladoc
- [Immutable Set companion object] scaladoc

If you need to remember some of your Set theory knowledge, this is an excellent opportunity.

[Immutable Set]: https://www.scala-lang.org/api/current/scala/collection/Set.html

## For-comprehension

Study Section 6.3 from [Essential Scala]. For a more thorough discussion on the topic,
study Chapter 2 of [[Functional Programming for Mortals with Scalaz]]

### Using `Option`

- Explain to your pairs the usage of for comprehensions with `Option[T]`. Practice with actual code.
- Study the basic usage patterns with optionals to avoid `null`s.
- Practice basic pattern matching with `Option[Int]`.
- Study some of the methods in the Standard Library: `headOption`, `map`, `flatMap`, `getOrElse`.
- Compare with the Java 8+ Api for `Optional<T>`, `OptionalInt`, etc.
- Review how Scala allows using primitive types with `Option[T]`.
- Review for-comprehensions with `Option[T]` values.

The patterns learned with this important Data Type will be of much value for the Scala programmer on a daily basis.

### Using `Either`

Explain to your pairs the usage of for comprehensions with `Either[+E, +A]`. Practice with actual code.

- Review basic pattern matching with `Either[E, A]`
- Review the way to build `Either` values.
- Explain that `Either` is right biased.
- Use `Either[String, Int]` to model the possible result of parsing integers from the command line.
- Review the relationship that this type with `Try`

## Pattern matching

- Review *all* of appendix A from [Essential Scala].

[Essential Scala]: https://underscore.io/books/essential-scala
[Functional Programming for Mortals with Scalaz]: https://leanpub.com/fpmortals/read#leanpub-auto-for-comprehensions