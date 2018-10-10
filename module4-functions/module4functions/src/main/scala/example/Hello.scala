package example

object Hello extends Greeting with App {
  val triple = (n: Int) => 3 * n
  val n1 = triple(4)
  println(n1)
}

trait Greeting {
  lazy val greeting: String = "hello"
}
