---
layout: docs
title: "Sequencing computation"
section: "docs"
author: Mauricio Saavedra
---

# Module 6: Sequencing computation

As we saw in module 4, generics is a very powerful tool, but can get tricky and
it may seem to limit us, since we don't have a concrete type, and types define
the available operations. We might get the feeling we are loosing functionality or
we have to introduce too much boilerplate code in order to make something useful.
One way to take advantage of generics is sequencing computation. On this module we will
review a couple of patterns for using generic data.

## fold

In an academic definition, we can define the fold pattern as:

>
>Given an `A` datatype, fold converts it to a `B` datatype. Fold is a structural recursion with:
>
>- one function parameter for each case in A.
>- each function takes as parameters the fields for its associated class.
>- if A is recursive, any function parameters that refer to a recursive field take a parameter of type B.
>  
>The right-hand side of pattern matching cases, or the polymorphic methods as appropriate,
>consists of calls to the appropriate function.
>

This means a fold will take you from type `A` to type `B` by means of a function.
Lets take advantage of the example in 
[Essential Scala](https://books.underscore.io/essential-scala/essential-scala.pdf) to
go a bit further into `fold.
If we have created the type LinkedList[A]:

```scala
sealed trait LinkedList[A]
final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A]
final case class End[A]() extends LinkedList[A]
```

We would define `fold` as:

```scala
sealed trait LinkedList[A] {
  def fold[B](end: B, f: (A, B) => B): B =
    this match {
      case End() => end
      case Pair(hd, tl) => f(hd, tl.fold(end, f))
    }
}
final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A]
final case class End[A]() extends LinkedList[A]
```

As we can see, fold is applied over a `LinkedList[A]` and returns a `B`, by means
of a function `f:(A, B) => B`. Fold will allow us to implement the following method
avoiding boiler plate.

```scala
def len(a: LinkedList[A]) = a.fold(0, (len, list) =>list + 1)
```

Each Scala class has its own implementation of `fold`. 

<p style="text-align:center;">
<img src="../img/Fold.png" alt="Fold in different classes" style="width: 35%"/>
</p>

One cool way of using `fold` is to [work with Option](https://alvinalexander.com/scala/how-use-fold-scala-option-some-none-syntax).

For example, if we have:

```scala
val aux: Option[A] = ???
val value: A = aux.fold(default)(x => x)
``` 

## map and flatMap

`map` and `flatMap` are in certain sense very similar to `fold`, but with a small twist.
Now we want to go from a `F[A]` type to a `F[B]` type by means of a function. 
In the case of `map`, the function should be of type `A => B`. Following the example 
we used to implement `fold`, we can implement `map` as follows:

```scala
sealed trait LinkedList[A] {
  def map[B](f: A => B): LinkedList[B] =
    this match {
      case End() => End[B]()
      case Pair(hd, tl) => Pair[B](f(hd), tl.map(f))
    }
}
final case class Pair[A](head: A, tail: LinkedList[A]) extends LinkedList[A]
final case class End[A]() extends LinkedList[A]
``` 
As we can see we follow almost the same structure as in `fold` with the difference
that now we only apply the function to each individual element of the
`LinkedList`.

Now, `flatMap` has a slight difference with `map`. Again we want to go from
`F[A]` to `F[B]`, but this time with an `A => F[B]` function. In order to implement
`flatMap` on `LinkedList` we would need to implement and append method, so lets
implement it on another type called `Maybe`, which will turn familiar to you.

```scala
sealed trait Maybe[A] {
  def flatMap[B](f: A => Maybe[B]): Maybe[B] = 
    this match {
      case Full(x) => f(x)
      case Empty() => Empty[B]
    }
}
final case class Full[A](value: A) extends Maybe[A]
final case class Empty[A]() extends Maybe[A]
```

Again as in fold, each class has its own implementation.

<p style="text-align:center;">
<img src="../img/Map.png" alt="map and flatMap in different classes" style="width: 35%"/>
</p>
  
## Functional programming constructs in action

Now that we know how to use `fold`, `map` and `flatMap` we can work with them
on a very easy way by letting us be guided by types. For example, lets suppose
we have the following:

```scala
sealed trait MyOwnType[A] {
  def fold[B](param: B)(f: (A,B) => B): B = ???
  def map[B](f: A => B): MyOwnType[B] = ???
  def flatMap[B](f: A => MyOwnType[B]): MyOwnType[B] = ???
}
``` 

How would we go to:

```scala
val value1: MyOwnType[Double] = ???
val value2: String = ???
val value3: MyOwnType[Int] = ??? 
```

By using: 

```scala
def funct1[A](arg: A): MyOwnType[A] = ???
def funct2[A](arg1: A, arg2: String): String = ???
def funct3[A](arg: A): Double = ???
```

The solution is:

```scala
val subject: MyOwnType = MyOwnType("This is a String")
val value1: MyOwnType[Double] = subject.map(funct3[String](???))
val value2: String = subject.fold("Hi")(funct2[String](???, ???))
val value3: MyOwnType[Int] = subject.flatMap(funct1[Int](???))
```