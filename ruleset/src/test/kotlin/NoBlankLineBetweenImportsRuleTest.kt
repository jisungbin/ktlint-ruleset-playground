import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider
import com.pinterest.ktlint.test.KtLintAssertThat
import kotlin.reflect.full.createInstance
import kotlin.test.Test

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
}

private inline fun <reified T : Rule> assertRule(code: String): KtLintAssertThat =
  KtLintAssertThat(
    ruleProvider = RuleProvider { T::class.createInstance() },
    code = code,
    additionalRuleProviders = LinkedHashSet(),
    editorConfigProperties = LinkedHashSet(),
  )
