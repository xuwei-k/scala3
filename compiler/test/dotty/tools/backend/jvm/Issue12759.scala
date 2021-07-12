package dotty.tools.backend.jvm

import org.junit.Assert._
import org.junit.Test

import scala.tools.asm
import asm._
import asm.tree._

import scala.tools.asm.Opcodes
import scala.jdk.CollectionConverters._
import Opcodes._

class Issue12759 extends DottyBytecodeTest {
  import ASMConverters._
  @Test def test1 = {
    val src = """
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
    """.stripMargin

    checkBCode(src) { dir =>
      def aaa(name: String) = {
        val c = dir.lookupName(name + ".class", directory = false)
        val file = new java.io.File(name + ".class")
        try {
          java.nio.file.Files.write(file.toPath, c.toByteArray)
          sys.process.Process(s"javap  ${name}.class").!
        } finally {
          println(file.delete)
        }
      }

      aaa("Bar")
      aaa("Foo")

/*
        val url = f.getAbsoluteFile.getParentFile.toURI().toURL()
        val loader = new java.net.URLClassLoader(Array(url))
        val clazz: Class[?] = loader.loadClass("Foo")
//        println(clazz.getConstructors.head.newInstance())

        //Class.forName("A")
      val clsNode = loadClassNode(clsIn.input)
*/


    }
  }
}
