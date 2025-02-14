package myruleset

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider
import com.pinterest.ktlint.test.KtLintAssertThat
import kotlin.reflect.full.createInstance
import org.junit.jupiter.api.Test

class NoBlankLineBetweenImportsRuleTest {
  @Test fun test() {
    val assertion = assertRule<NoBlankLineBetweenImportsRule>(
      """
import kotlin.Unit


     
        

import kotlin.String
      """.trimIndent(),
    )

    assertion.isFormattedAs(
      """
import kotlin.Unit
import kotlin.String
      """.trimIndent(),
    )
  }

  @Test fun test2() {
    val assertion = assertRule<NoBlankLineBetweenImportsRule>(
      """
import kotlin.Unit

    import kotlin.String
      """.trimIndent(),
    )

    assertion.isFormattedAs(
      """
import kotlin.Unit
import kotlin.String
      """.trimIndent(),
    )
  }

  @Test fun test3() {
    val assertion = assertRule<NoBlankLineBetweenImportsRule>(
      """
import kotlin.Unit

import kotlin.String
      """.trimIndent(),
    )

    assertion.isFormattedAs(
      """
import kotlin.Unit
import kotlin.String
      """.trimIndent(),
    )
  }

  @Test fun test4() {
    val assertion = assertRule<NoBlankLineBetweenImportsRule>(
      """
import kotlin.Unit
import kotlin.String
      """.trimIndent(),
    )

    assertion.hasNoLintViolations()
  }
}

private inline fun <reified T : Rule> assertRule(code: String): KtLintAssertThat =
  KtLintAssertThat(
    ruleProvider = RuleProvider { T::class.createInstance() },
    code = code,
    additionalRuleProviders = LinkedHashSet(),
    editorConfigProperties = LinkedHashSet(),
  )
