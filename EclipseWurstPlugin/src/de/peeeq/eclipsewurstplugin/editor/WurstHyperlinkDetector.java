package de.peeeq.eclipsewurstplugin.editor;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.hyperlink.IHyperlink;
import org.eclipse.jface.text.hyperlink.IHyperlinkDetector;

import de.peeeq.wurstscript.WLogger;
import de.peeeq.wurstscript.ast.AstElement;
import de.peeeq.wurstscript.ast.AstElementWithSource;
import de.peeeq.wurstscript.ast.CompilationUnit;
import de.peeeq.wurstscript.ast.ConstructorDef;
import de.peeeq.wurstscript.ast.ExprBinary;
import de.peeeq.wurstscript.ast.ExprNewObject;
import de.peeeq.wurstscript.ast.ExprVarAccess;
import de.peeeq.wurstscript.ast.FuncRef;
import de.peeeq.wurstscript.ast.FunctionDefinition;
import de.peeeq.wurstscript.ast.ModuleDef;
import de.peeeq.wurstscript.ast.ModuleUse;
import de.peeeq.wurstscript.ast.NameDef;
import de.peeeq.wurstscript.ast.NameRef;
import de.peeeq.wurstscript.ast.TypeDef;
import de.peeeq.wurstscript.ast.TypeExpr;
import de.peeeq.wurstscript.ast.TypeExprSimple;
import de.peeeq.wurstscript.ast.WImport;
import de.peeeq.wurstscript.ast.WPackage;
import de.peeeq.wurstscript.utils.Utils;

public class WurstHyperlinkDetector implements IHyperlinkDetector {

	private static final IHyperlink[] NONE = new IHyperlink[] {};
	private WurstEditor editor;

	public WurstHyperlinkDetector(WurstEditor editor) {
		this.editor = editor;
	}

	@Override
	public IHyperlink[] detectHyperlinks(ITextViewer textViewer, IRegion region, boolean canShowMultipleHyperlinks) {
		int offset = region.getOffset();
		return getHyperlinks(offset);
	}

	public IHyperlink[] getHyperlinks(int offset) {
		boolean useMouse = true;
		return calculateHyperlinks(offset, useMouse);
	}

	public IHyperlink[] calculateHyperlinks(int offset, boolean useMouse) {
		System.out.println("calc hyperlinks @ " + useMouse);
		CompilationUnit cu = editor.getCompilationUnit();
		if (cu != null) {
			AstElement e = Utils.getAstElementAtPos(cu, offset, useMouse);
			System.out.println(offset + "	hover: " + e.getClass().getSimpleName());
			if (e instanceof FuncRef) {
				FuncRef funcRef = (FuncRef) e;
				FunctionDefinition decl = funcRef.attrFuncDef();
				return linkTo(decl, e.attrSource().getLeftPos(), e.attrSource().getRightPos());
			} else if (e instanceof NameRef) {
				NameRef nameRef = (NameRef) e;
				NameDef decl = nameRef.attrNameDef();
				return linkTo(decl, e.attrSource().getLeftPos(), e.attrSource().getRightPos()-1);
			} else if (e instanceof TypeExpr) {
				TypeExpr typeExpr = (TypeExpr) e;
				TypeDef decl = typeExpr.attrTypeDef();
				return linkTo(decl, e.attrSource().getLeftPos(), e.attrSource().getRightPos()-1);
			} else if (e instanceof WImport) {
				WImport wImport = (WImport) e;
				WPackage p = wImport.attrImportedPackage();
				if (p == null) {
					return null;
				}
				return linkTo(p, e.attrSource().getLeftPos(), e.attrSource().getRightPos()-1);
			} else if (e instanceof ExprNewObject) {
				ExprNewObject exprNew = (ExprNewObject) e;
				ConstructorDef def = exprNew.attrConstructorDef();
				return linkTo(def, e.attrSource().getLeftPos(), e.attrSource().getRightPos()-1);
			} else if (e instanceof ModuleUse) {
				ModuleUse use = (ModuleUse) e;
				ModuleDef def = use.attrModuleDef();
				return linkTo(def, e.attrSource().getLeftPos(), e.attrSource().getRightPos()-1);
			} else if (e instanceof ExprBinary) {
				ExprBinary eb = (ExprBinary) e;
				FunctionDefinition def = eb.attrFuncDef();
				if (def != null) {
					return linkTo(def, eb.getLeft().attrSource().getRightPos(), eb.getRight().attrSource().getLeftPos()-1);
				}
			}
		}
		return null;
	}

	private IHyperlink[] linkTo(AstElementWithSource decl, int start, int end) {
		if (decl == null) return null;
		return new IHyperlink[] {
			new WurstHyperlink(editor.getProject(), decl, start, end)
		};
	}

}
