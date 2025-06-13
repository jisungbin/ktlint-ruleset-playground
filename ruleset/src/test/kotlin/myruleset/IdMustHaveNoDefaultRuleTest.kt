package myruleset

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider
import com.pinterest.ktlint.test.KtLintAssertThat
import kotlin.reflect.full.createInstance
import org.junit.jupiter.api.Test

class IdMustHaveNoDefaultRuleTest {
  @Test fun test() {
    val assertion = assertRule<IdDefaultIsUuidRule>(
      """
        class Id(
          val name: String = "",
          val id: String = "",
          val description: String = "",
          val type: String = "",
        )
      """.trimIndent(),
    )

    assertion.hasNoLintViolations()
  }

  @Test fun test2() {
    val assertion = assertRule<IdDefaultIsUuidRule>(
      """
        class Id(
          val name: String = "",
          val id: String = "",
          val description: String = "",
          val type: String = "",
        ) : Message<Int, Int>(1, 2)
      """.trimIndent(),
    )

    assertion.isFormattedAs(
      """
        class Id(
          val name: String = "",
          val id: String = java.util.UUID.randomUUID().toString(),
          val description: String = "",
          val type: String = "",
        ) : Message<Int, Int>(1, 2)
      """.trimIndent(),
    )
  }

  @Test fun test3() {
    val assertion = assertRule<IdDefaultIsUuidRule>(
      """
        class Id(
          val name: String = "",
          val id: Int = "",
          val description: String = "",
          val type: String = "",
        ) : Message<Int, Int>(1, 2)
      """.trimIndent(),
    )

    assertion.hasNoLintViolations()
  }
}

private inline fun <reified T : Rule> assertRule(code: String): KtLintAssertThat =
  KtLintAssertThat(
    ruleProvider = RuleProvider { T::class.createInstance() },
    code = code,
    additionalRuleProviders = mutableSetOf(),
    editorConfigProperties = mutableSetOf(),
  )
