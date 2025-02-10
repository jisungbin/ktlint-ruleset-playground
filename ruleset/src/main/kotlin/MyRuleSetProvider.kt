import com.pinterest.ktlint.cli.ruleset.core.api.RuleSetProviderV3
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleProvider
import com.pinterest.ktlint.rule.engine.core.api.RuleSetId

class MyRuleSetProvider : RuleSetProviderV3(RuleSetId(RULESET_ID)) {
  override fun getRuleProviders(): Set<RuleProvider> =
    setOf(RuleProvider { NoBlankLineBetweenImportsRule() })

  companion object {
    const val RULESET_ID = "my-ruleset"
    val ABOUT = Rule.About(
      maintainer = "Ji Sungbin",
      repositoryUrl = "https://github.com/jisungbin/ktlint-ruleset-playground",
      issueTrackerUrl = "https://github.com/jisungbin/ktlint-ruleset-playground/issues",
    )
  }
}
