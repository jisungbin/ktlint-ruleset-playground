package myruleset

interface HasId

class Id(
  val name: String = "",
  val id: String = "",
  val description: String = "",
  val type: String = "",
) : HasId
