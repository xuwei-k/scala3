package dotty.tools.backend.jvm

import org.junit.Assert._
import org.junit.Test

import scala.tools.asm
import asm._
import asm.tree._

import scala.tools.asm.Opcodes
import scala.jdk.CollectionConverters._
import Opcodes._

class Hoge extends DottyBytecodeTest {
  import ASMConverters._
  @Test def jsig = {
    val scalaSource = """
                 |class A[X](val x: X)
                 """.stripMargin
    val javaSource = """
      public class Main {
        public static void main(String[] args) {
          A<Integer> a = new A<>("x");
          int x = a.x();
        }
      }
    """

    checkBCode(scalaSource :: Nil, javaSource :: Nil) { dir =>
      val clsIn = dir.lookupName("A.class", directory = false)
      val f = new java.io.File("A.class")
      try {
        java.nio.file.Files.write(f.toPath, clsIn.toByteArray)
        sys.process.Process("javap A.class").!
      } finally {
        f.delete
      }

      val clsNode = loadClassNode(clsIn.input)
    }
  }
}
