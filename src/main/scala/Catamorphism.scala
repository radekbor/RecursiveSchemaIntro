
object PrettyPrint {

  def print: JsonNode[String] => String = {
    case DictNode(nodes)=> nodes.toList.map {
      case (key, value) => s""""$key": $value"""
    }.mkString("{", ",", "}")
    case IntNode(int) => int.toString
    case StringNode(string) => s""""$string""""
  }

}


sealed trait JsonNode[T]

case class DictNode[T](nodes: Map[String, T]) extends JsonNode[T]

case class StringNode[T](string: String) extends JsonNode[T]

case class IntNode[T](int: Int) extends JsonNode[T]

import matryoshka._
import matryoshka.data.Fix
import matryoshka.implicits._

object Catamorphism {

  def main(args: Array[String]): Unit = {

    val tree: Fix[JsonNode] = Fix[JsonNode](DictNode(Map(
      "key1" -> Fix[JsonNode](IntNode(2)),
      "key2" -> Fix[JsonNode](StringNode("Something"))
    )))

    val fun = new scalaz.Functor[JsonNode] {
      override def map[A, B](fa: JsonNode[A])(f: A => B): JsonNode[B] = fa match {
        case DictNode(children: Map[String, A]) => DictNode[B](children.map { case (key, value) => (key, f(value)) })
        case IntNode(int) => IntNode(int)
        case StringNode(string) => StringNode(string)
      }
    }

    val result = tree.cata(PrettyPrint.print)(fun)

    println(result)
  }

}
