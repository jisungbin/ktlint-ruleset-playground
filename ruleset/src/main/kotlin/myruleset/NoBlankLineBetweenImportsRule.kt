package myruleset

import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.rule.engine.core.api.ElementType.IMPORT_LIST
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleAutocorrectApproveHandler
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.ifAutocorrectAllowed
import com.pinterest.ktlint.rule.engine.core.api.replaceWith
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl
import org.jetbrains.kotlin.com.intellij.psi.tree.TokenSet.WHITE_SPACE

class NoBlankLineBetweenImportsRule :
  Rule(
    ruleId = RuleId("${MyRuleSetProvider.RULESET_ID}:$ID"),
    about = MyRuleSetProvider.ABOUT,
  ),
  RuleAutocorrectApproveHandler {

  override fun beforeVisitChildNodes(
    node: ASTNode,
    emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> AutocorrectDecision,
  ) {
    if (node.elementType == IMPORT_LIST) {
      node.getChildren(WHITE_SPACE)
        .filter { it.text.contains("\n\n") }
        .forEach { node ->
          emit(node.startOffset, MESSAGE, true)
            .ifAutocorrectAllowed { node.replaceWith(PsiWhiteSpaceImpl("\n")) }
        }
      stopTraversalOfAST()
    }
  }

  companion object {
    const val ID = "no-blank-line-between-imports-rule"
    private const val MESSAGE = "There should be no blank line between imports"
  }
}
