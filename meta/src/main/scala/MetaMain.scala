import scala.meta._

object MetaMain {
  def main(args: Array[String]): Unit = {
    val p = implicitly[parsers.Parse[Source]]
    val d = dialects.Scala3
      .withAllowCaptureChecking(true)
      .withAllowPureFunctions(true)
      .withAllowErasedDefs(true)
    java.nio.file.Files.find(
      new java.io.File("../library").getAbsoluteFile.toPath,
      100,
      (p, _) => p.toFile.isFile && p.toFile.getName.endsWith(".scala")
    ).forEach {
      path =>
        val input = scala.meta.Input.File(path)
        try {
          p.apply(input, d).get.collect { case t: Type.Wildcard =>
            t.tokens.find(_.is[scala.meta.tokens.Token.Underscore]).foreach {
              x =>
                val src = scala.io.Source.fromFile(path.toFile).getLines().mkString("\n")
                val (a1, a2) = src.splitAt(x.pos.start)
                java.nio.file.Files.writeString(
                  path,
                  s"$a1?${a2.drop(1)}\n"
                )
            }
          }
        } catch {
          case e: Throwable =>
            println((path, e))
        }
    }
  }
}
