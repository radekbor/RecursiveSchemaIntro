
object FixedPointTypeSteps {

  object V1 {
    def print[T](arg: JsonNode): Unit = {
      println(arg)
      println()
    }
    sealed trait JsonNode

    case class DictNode(nodes: Map[String, JsonNode]) extends JsonNode

    case class StringNode[T](string: String) extends JsonNode

    case class IntNode(int: Int) extends JsonNode
  }

  object V2 {

    def print[T](arg: JsonNode[T]): Unit = {
      println(arg) // right now we can consume everything
      println()
    }

    sealed trait JsonNode[T]

    case class DictNode[T](nodes: Map[String, T]) extends JsonNode[T]

    case class StringNode[T](string: String) extends JsonNode[T]

    case class IntNode[T](int: Int) extends JsonNode[T]
  }

  object V3 {

    case class JsonWrapper(value: JsonNode[JsonWrapper])

    def print[T](arg: JsonNode[JsonWrapper]): Unit = {
      println(arg)
      println()
    }

    sealed trait JsonNode[T]

    case class DictNode[T](nodes: Map[String, T]) extends JsonNode[T]

    case class StringNode[T](string: String) extends JsonNode[T]

    case class IntNode[T](int: Int) extends JsonNode[T]
  }

  object V4 {
    case class Wrapper[F[_]](value: F[Wrapper[F]])

    def print[T](arg: JsonNode[Wrapper[JsonNode]]): Unit = {
      println(arg)
      println()
    }

    sealed trait JsonNode[T]

    case class DictNode[T](nodes: Map[String, T]) extends JsonNode[T]

    case class StringNode[T](string: String) extends JsonNode[T]

    case class IntNode[T](int: Int) extends JsonNode[T]
  }

  def main(args: Array[String]): Unit = {

    val dictV1 = V1.DictNode(Map(
      "arg1" -> V1.IntNode(2),
      "arg2" -> V1.StringNode("2"),
      "arg3" -> V1.DictNode(Map("arg3.1" -> V1.IntNode(2)))
    ))
    println("V1.print(dictV1)")
    V1.print(dictV1)

    val dictV2A: V2.JsonNode[V2.JsonNode[Nothing]] = V2.DictNode(Map(
      "arg1" -> V2.IntNode(2),
      "arg2" -> V2.StringNode("2")
    ))
    println("V2.print(dictV2A)")
    V2.print(dictV2A)

    val dictV2B: V2.JsonNode[V2.JsonNode[V2.JsonNode[Nothing]]] = V2.DictNode(Map(
      "arg1" -> V2.IntNode(2),
      "arg2" -> V2.StringNode("2"),
      "arg3" -> V2.DictNode(Map("arg3.1" -> V2.IntNode(2)))
    ))

    println("V2.print(dictV2B)")
    V2.print(dictV2B)

    val dictV3: V3.JsonNode[V3.JsonWrapper] = V3.DictNode(Map(
      "arg1" -> V3.JsonWrapper(V3.IntNode(2)),
      "arg2" -> V3.JsonWrapper(V3.StringNode("2")),
      "arg3" -> V3.JsonWrapper(V3.DictNode(Map("arg3.1" -> V3.JsonWrapper(V3.IntNode(2)))))
    ))

    println("V3.print(dictV3)")
    V3.print(dictV3)

    val dictV4 = V4.Wrapper[V4.JsonNode](
      V4.DictNode(Map(
        "arg1" -> V4.Wrapper[V4.JsonNode](V4.IntNode(2)),
        "arg2" -> V4.Wrapper[V4.JsonNode](V4.StringNode("2")),
        "arg3" -> V4.Wrapper[V4.JsonNode](V4.DictNode(Map("arg3.1" -> V4.Wrapper[V4.JsonNode](V4.IntNode(2)))))
      ))
    )

    V4.print(dictV4.value)

  }
}
