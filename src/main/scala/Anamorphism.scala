import matryoshka.Coalgebra

import matryoshka._
import matryoshka.data.Fix
import matryoshka.implicits._
import matryoshka.patterns.{ConsF, ListF, NilF}

object Anamorphism {
  type IntListF[B] = ListF[Int, B]

  val coalgebra: Coalgebra[IntListF, Int] = (n: Int) => {
    n match {
      case 0 => NilF[Int, Int]()// can be writen also as NilF()
      case o => ConsF(o, o - 1)
    }
  }

  def main(args: Array[String]): Unit = {
    val result = 7.ana.apply[Fix[IntListF]](coalgebra)
    println(result)
  }
}
