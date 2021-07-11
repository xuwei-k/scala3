class A[X](val x: X)

object Test {
  def main(args: Array[String]): Unit = {
    val List(constructor) = classOf[A[?]].getConstructors().toList
    println(constructor.toGenericString())
  }
}
