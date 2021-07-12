class A1
class A2
class X1
class X2
class X3

trait Foo {
  val (a1, a2) = (new A1, new A2)
  val (x1, x2, x3) = (new X1, new X2, new X3)
}

class Bar extends Foo

object Test {
  def main(args: Array[String]): Unit = {
    println(new Bar)
  }
}
