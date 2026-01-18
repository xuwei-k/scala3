package dotty.tools.dotc.core.tasty

import dotty.tools.tasty.TastyBuffer.Addr

opaque type AddrMap[A] = collection.mutable.LongMap[A]

object AddrMap {
  def empty[A]: AddrMap[A] = collection.mutable.LongMap.empty[A]

  extension [A](self: AddrMap[A]) {
    def apply(key: Addr): A =
      self(key.index)

    def update(key: Addr, value: A): Unit =
      self.update(key.index, value)

    def remove(key: Addr): Option[A] =
      self.remove(key.index)

    def getOrElseUpdate(key: Addr, default: => A): A =
      self.getOrElseUpdate(key.index, default)

    def getOrElse(key: Addr, default: => A): A =
      self.getOrElse(key.index, default)

    def get(key: Addr): Option[A] =
      self.get(key.index)

    def iterator: Iterator[(Addr, A)] =
      self.iterator.map { (k, v) => (Addr(k.toInt), v) }
  }
}
