//Constraining types in generic functions:

sealed trait Shape
case class Square(l: Double) extends Shape
case class Circle(radius: Double) extends Shape

// Constraining T to be a subtype of Shape
// def is still a generic method
def area[T <: Shape](t: T) = ???

// Constraint: Super Type Example ?
// T >: Shape

// Type class Constraint
import scala.math.Ordering.Implicits._

def myMax[T : Ordering](x: T, y: T): T =
  if (x > y) x else y;