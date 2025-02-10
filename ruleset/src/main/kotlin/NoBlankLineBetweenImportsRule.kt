import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.rule.engine.core.api.ElementType.IMPORT_LIST
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleAutocorrectApproveHandler
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.children
import com.pinterest.ktlint.rule.engine.core.api.ifAutocorrectAllowed
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.PsiWhiteSpace
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl
import org.jetbrains.kotlin.psi.psiUtil.astReplace
import org.jetbrains.kotlin.psi.psiUtil.startOffset

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
      node.children()
        .filterIsInstance<PsiWhiteSpace>()
        .filter { it.text.contains("\n\n") }
        .forEach { node ->
          emit(node.startOffset, MESSAGE, true)
            .ifAutocorrectAllowed { node.astReplace(PsiWhiteSpaceImpl("\n")) }
        }
      stopTraversalOfAST()
    }
  }

  companion object {
    const val ID = "no-blank-line-between-imports-rule"
    private const val MESSAGE = "There should be no blank line between imports"
  }
}
