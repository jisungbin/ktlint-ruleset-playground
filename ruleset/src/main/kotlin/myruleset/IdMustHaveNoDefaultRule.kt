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
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.psiUtil.siblings

class IdMustHaveNoDefaultRule :
  Rule(
    ruleId = RuleId("${MyRuleSetProvider.RULESET_ID}:$ID"),
    about = MyRuleSetProvider.ABOUT,
  ),
  RuleAutocorrectApproveHandler {

  override fun beforeVisitChildNodes(
    node: ASTNode,
    emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> AutocorrectDecision,
  ) {
    if (
      node.elementType == IDENTIFIER &&
      node.treeParent.elementType == VALUE_PARAMETER &&
      node.text == "id"
    ) {
      val typeNode =
        node.nextSibling {
          it.elementType == TYPE_REFERENCE && it.children().lastOrNull()?.text == "String"
        } ?: return
      val typeNodeNextSibling = typeNode.nextSibling() ?: return
      val stringClosingNode =
        typeNode.siblings().lastOrNull()?.takeIf { it.elementType == STRING_TEMPLATE } ?: return

      val ownerClass = node.parent(CLASS)?.psi as? KtClass ?: return
      val superTypes = ownerClass.superTypeListEntries.ifEmpty { return }

      if (
        superTypes.any {
          // [it.typeAsUserType.qualifier == null].. why??
          it.text == "HasId" &&
            (it.containingFile as? KtFile)?.packageFqName?.asString() == "myruleset"
        }
      ) {
        emit(typeNode.startOffset, MESSAGE, true)
          // TODO https://github.com/JetBrains/intellij-community/blob/0e3531db4a7cf254d91311f911be73a2daee89c0/plugins/kotlin/refactorings/kotlin.refactorings.common/src/org/jetbrains/kotlin/idea/refactoring/rename/commonRenameUtils.kt#L116
          .ifAutocorrectAllowed { node.treeParent.removeRange(typeNodeNextSibling, stringClosingNode) }

        stopTraversalOfAST()
      }
    }
  }

  companion object {
    const val ID = "id-must-have-no-default"
    private const val MESSAGE = "ID 필드는 기본값이 없어야 합니다."
  }
}
