package myruleset

import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.rule.engine.core.api.ElementType.CLASS
import com.pinterest.ktlint.rule.engine.core.api.ElementType.IDENTIFIER
import com.pinterest.ktlint.rule.engine.core.api.ElementType.STRING_TEMPLATE
import com.pinterest.ktlint.rule.engine.core.api.ElementType.TYPE_REFERENCE
import com.pinterest.ktlint.rule.engine.core.api.ElementType.VALUE_PARAMETER
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleAutocorrectApproveHandler
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.children
import com.pinterest.ktlint.rule.engine.core.api.ifAutocorrectAllowed
import com.pinterest.ktlint.rule.engine.core.api.nextSibling
import com.pinterest.ktlint.rule.engine.core.api.parent
import com.pinterest.ktlint.rule.engine.core.api.replaceWith
import java.util.WeakHashMap
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtPsiFactory
import org.jetbrains.kotlin.psi.psiUtil.siblings

class IdDefaultIsUuidRule :
  Rule(
    ruleId = RuleId("${MyRuleSetProvider.RULESET_ID}:$ID"),
    about = MyRuleSetProvider.ABOUT,
  ),
  RuleAutocorrectApproveHandler {
  private val psiFactories = WeakHashMap<Project, KtPsiFactory>()

  override fun beforeVisitChildNodes(
    node: ASTNode,
    emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> AutocorrectDecision,
  ) {
    if (
      node.elementType == IDENTIFIER &&
      node.treeParent.elementType == VALUE_PARAMETER &&
      node.text == "id"
    ) {
      val typeNode = node.nextSibling { it.elementType == TYPE_REFERENCE } ?: return
      if (typeNode.children().lastOrNull()?.text != "String") return

      val defaultStringNode = node.siblings().lastOrNull()?.takeIf { it.elementType == STRING_TEMPLATE } ?: return

      val ownerClass = node.parent(CLASS)?.psi as? KtClass ?: return
      val superTypes = ownerClass.superTypeListEntries.ifEmpty { return }
      if (superTypes.any { it.text.startsWith("Message") }) {
        val project = node.psi.project
        val psiFactory = psiFactories.getOrPut(project) { KtPsiFactory(project, markGenerated = false) }
        val uuidExpression = psiFactory.createExpression("java.util.UUID.randomUUID().toString()")

        emit(defaultStringNode.startOffset, MESSAGE, true)
          .ifAutocorrectAllowed { defaultStringNode.replaceWith(uuidExpression.node) }

        stopTraversalOfAST()
      }
    }
  }

  companion object {
    const val ID = "id-default-is-uuid"
    private const val MESSAGE = "ID 필드는 기본값은 UUID여야 합니다. "
  }
}
