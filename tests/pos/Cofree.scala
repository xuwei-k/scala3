package com.example

import Main._

final case class Cofree[S[_], A](head: A, tail: S[Cofree[S, A]])

object Cofree {

  implicit def cofreeEqual[F[_], A](implicit A: Equal[A], C: Equal1[F, Cofree[F, A]]): Equal[Cofree[F, A]] =
    new Equal[Cofree[F, A]] {
      def equal(a: Cofree[F, A], b: Cofree[F, A]) = {
        val F: Equal[F[Cofree[F, A]]] = C.apply(cofreeEqual[F, A])
        A.equal(a.head, b.head) && F.equal(a.tail, b.tail)
      }
    }
}

trait Equal[A] {
  def equal(a1: A, a2: A): Boolean
}

object Equal {
  def apply[A](implicit A: Equal[A]): Equal[A] = A

  implicit def optionEqual[A]: Equal1[Option, A] = { implicit A =>
    new Equal[Option[A]] {
      def equal(x: Option[A], y: Option[A]) = (x, y) match {
        case (Some(a), Some(b)) =>
          A.equal(a, b)
        case (None, None) =>
          true
        case _ =>
          false
      }
    }
  }

  implicit val intEqual: Equal[Int] = _ == _
}

object Main {

  type Equal1[F[_], A] = (implicit Equal[A]) => Equal[F[A]]

  def main(args: Array[String]): Unit = {
    // stack overflow
    // implicit def a: Equal[Cofree[Option, Int]] = Equal[Cofree[Option, Int]]

    implicit lazy val a: Equal[Cofree[Option, Int]] = Equal[Cofree[Option, Int]]

    val x: Cofree[Option, Int] = Cofree(42, None)

    println(a) // null !!!
    println(a.equal(x, x))
  }

}
