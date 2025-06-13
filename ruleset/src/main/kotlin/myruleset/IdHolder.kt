package myruleset

interface HasId

class Id(
  val name: String = java.util.UUID.randomUUID().toString(),
  val id: String = "",
  val description: String = "",
  val type: String = "",
) : HasId
