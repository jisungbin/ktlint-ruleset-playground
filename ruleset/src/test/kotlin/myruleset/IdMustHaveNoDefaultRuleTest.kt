package myruleset

import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider
import com.pinterest.ktlint.test.KtLintAssertThat
import kotlin.reflect.full.createInstance
import org.junit.jupiter.api.Test

class IdMustHaveNoDefaultRuleTest {
  @Test fun test() {
    val assertion = assertRule<IdMustHaveNoDefaultRule>(
      """
        package myruleset
        
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
    val assertion = assertRule<IdMustHaveNoDefaultRule>(
      """
        package myruleset
        
        interface HasId

        class Id(
          val name: String = "",
          val id: String = "",
          val description: String = "",
          val type: String = "",
        ) : HasId
      """.trimIndent(),
    )

    assertion.isFormattedAs(
      """
        package myruleset
        
        interface HasId

        class Id(
          val name: String = "",
          val id: String,
          val description: String = "",
          val type: String = "",
        ) : HasId
      """.trimIndent(),
    )
  }
//
//  @Test fun test3() {
//    val assertion = assertRule<IdMustHaveNoDefaultRule>(
//      """
//import kotlin.Unit
//
//import kotlin.String
//      """.trimIndent(),
//    )
//
//    assertion.isFormattedAs(
//      """
//import kotlin.Unit
//import kotlin.String
//      """.trimIndent(),
//    )
//  }
//
//  @Test fun test4() {
//    val assertion = assertRule<IdMustHaveNoDefaultRule>(
//      """
//import kotlin.Unit
//import kotlin.String
//      """.trimIndent(),
//    )
//
//    assertion.hasNoLintViolations()
//  }
}

private inline fun <reified T : Rule> assertRule(code: String): KtLintAssertThat =
  KtLintAssertThat(
    ruleProvider = RuleProvider { T::class.createInstance() },
    code = code,
    additionalRuleProviders = mutableSetOf(),
    editorConfigProperties = mutableSetOf(),
  )
