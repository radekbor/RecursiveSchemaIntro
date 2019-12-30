import matryoshka.{Algebra, Coalgebra}
import matryoshka.data.Fix
import matryoshka.implicits._
import matryoshka.patterns.{ConsF, ListF, NilF}

object Hylomorphism {
  trait TreeF[T]
  case class Leaf[T](a: List[Int]) extends TreeF[T]
  case class NodeF[T](a: T, b: T) extends TreeF[T]
  case class EmptyF[T]() extends TreeF[T]

  val coalg = new Coalgebra[TreeF, List[Int]] {
    override def apply(v1: List[Int]): TreeF[List[Int]] = {
      v1 match {
        case Nil => EmptyF()
        case head::Nil => Leaf(head::Nil)
        case l =>
          l.splitAt(l.length / 2) match {
            case (a, b) => NodeF(a, b)
          }
      }
    }
  }

  val alg = new Algebra[TreeF, List[Int]] {
    override def apply(t: TreeF[List[Int]]): List[Int] = {
      t match {
        case Leaf(l)     => l
        case EmptyF()    => Nil
        case NodeF(l, r) => merge(l, r)
      }
    }
  }

  implicit val fun = new scalaz.Functor[TreeF] {
    override def map[A, B](fa: TreeF[A])(f: A => B): TreeF[B] = {
      fa match {
        case Leaf(a) => Leaf(a)
        case NodeF(a, b) => NodeF(f(a), f(b))
        case EmptyF() => EmptyF()
      }
    }
  }

  def merge(left: List[Int], right: List[Int]): List[Int] =
    (left, right) match {
      case (leftValue, Nil) => leftValue
      case (Nil, rightValue) => rightValue
      case(leftHead :: leftTail, rightHead :: rightTail) =>
        if (leftHead < rightHead) leftHead::merge(leftTail, right)
        else rightHead :: merge(left, rightTail)
    }


  def main(args: Array[String]): Unit = {
    val result = List(3, 4, 1, 2).hylo(alg, coalg)
    println(result)
  }
}
