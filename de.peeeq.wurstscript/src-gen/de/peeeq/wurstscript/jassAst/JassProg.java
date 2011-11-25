//generated by parseq
package de.peeeq.wurstscript.jassAst;

public interface JassProg extends JassAstElement {
	JassAstElement getParent();
	void setGlobals(JassVars globals);
	JassVars getGlobals();
	void setFunctions(JassFunctions functions);
	JassFunctions getFunctions();
	JassProg copy();
	public abstract void accept(JassProg.Visitor v);
	public interface Visitor {
		void visit(JassOpMult jassOpMult);
		void visit(JassOpLessEq jassOpLessEq);
		void visit(JassStmtSetArray jassStmtSetArray);
		void visit(JassSimpleVar jassSimpleVar);
		void visit(JassStmtReturnVoid jassStmtReturnVoid);
		void visit(JassExprVarAccess jassExprVarAccess);
		void visit(JassStmtCall jassStmtCall);
		void visit(JassOpPlus jassOpPlus);
		void visit(JassOpMinus jassOpMinus);
		void visit(JassExprBinary jassExprBinary);
		void visit(JassOpUnequals jassOpUnequals);
		void visit(JassExprRealVal jassExprRealVal);
		void visit(JassExprUnary jassExprUnary);
		void visit(JassExprFuncRef jassExprFuncRef);
		void visit(JassFunction jassFunction);
		void visit(JassExprVarArrayAccess jassExprVarArrayAccess);
		void visit(JassExprlist jassExprlist);
		void visit(JassArrayVar jassArrayVar);
		void visit(JassVars jassVars);
		void visit(JassSimpleVars jassSimpleVars);
		void visit(JassExprIntVal jassExprIntVal);
		void visit(JassStmtLoop jassStmtLoop);
		void visit(JassOpGreaterEq jassOpGreaterEq);
		void visit(JassOpGreater jassOpGreater);
		void visit(JassStmtIf jassStmtIf);
		void visit(JassStmtReturn jassStmtReturn);
		void visit(JassExprNull jassExprNull);
		void visit(JassProg jassProg);
		void visit(JassOpOr jassOpOr);
		void visit(JassStmtSet jassStmtSet);
		void visit(JassStatements jassStatements);
		void visit(JassExprStringVal jassExprStringVal);
		void visit(JassExprBoolVal jassExprBoolVal);
		void visit(JassOpNot jassOpNot);
		void visit(JassFunctions jassFunctions);
		void visit(JassOpAnd jassOpAnd);
		void visit(JassOpEquals jassOpEquals);
		void visit(JassOpDiv jassOpDiv);
		void visit(JassOpLess jassOpLess);
		void visit(JassStmtExitwhen jassStmtExitwhen);
		void visit(JassExprFunctionCall jassExprFunctionCall);
	}
	public static abstract class DefaultVisitor implements Visitor {
		@Override public void visit(JassOpMult jassOpMult) {}
		@Override public void visit(JassOpLessEq jassOpLessEq) {}
		@Override public void visit(JassStmtSetArray jassStmtSetArray) {}
		@Override public void visit(JassSimpleVar jassSimpleVar) {}
		@Override public void visit(JassStmtReturnVoid jassStmtReturnVoid) {}
		@Override public void visit(JassExprVarAccess jassExprVarAccess) {}
		@Override public void visit(JassStmtCall jassStmtCall) {}
		@Override public void visit(JassOpPlus jassOpPlus) {}
		@Override public void visit(JassOpMinus jassOpMinus) {}
		@Override public void visit(JassExprBinary jassExprBinary) {}
		@Override public void visit(JassOpUnequals jassOpUnequals) {}
		@Override public void visit(JassExprRealVal jassExprRealVal) {}
		@Override public void visit(JassExprUnary jassExprUnary) {}
		@Override public void visit(JassExprFuncRef jassExprFuncRef) {}
		@Override public void visit(JassFunction jassFunction) {}
		@Override public void visit(JassExprVarArrayAccess jassExprVarArrayAccess) {}
		@Override public void visit(JassExprlist jassExprlist) {}
		@Override public void visit(JassArrayVar jassArrayVar) {}
		@Override public void visit(JassVars jassVars) {}
		@Override public void visit(JassSimpleVars jassSimpleVars) {}
		@Override public void visit(JassExprIntVal jassExprIntVal) {}
		@Override public void visit(JassStmtLoop jassStmtLoop) {}
		@Override public void visit(JassOpGreaterEq jassOpGreaterEq) {}
		@Override public void visit(JassOpGreater jassOpGreater) {}
		@Override public void visit(JassStmtIf jassStmtIf) {}
		@Override public void visit(JassStmtReturn jassStmtReturn) {}
		@Override public void visit(JassExprNull jassExprNull) {}
		@Override public void visit(JassProg jassProg) {}
		@Override public void visit(JassOpOr jassOpOr) {}
		@Override public void visit(JassStmtSet jassStmtSet) {}
		@Override public void visit(JassStatements jassStatements) {}
		@Override public void visit(JassExprStringVal jassExprStringVal) {}
		@Override public void visit(JassExprBoolVal jassExprBoolVal) {}
		@Override public void visit(JassOpNot jassOpNot) {}
		@Override public void visit(JassFunctions jassFunctions) {}
		@Override public void visit(JassOpAnd jassOpAnd) {}
		@Override public void visit(JassOpEquals jassOpEquals) {}
		@Override public void visit(JassOpDiv jassOpDiv) {}
		@Override public void visit(JassOpLess jassOpLess) {}
		@Override public void visit(JassStmtExitwhen jassStmtExitwhen) {}
		@Override public void visit(JassExprFunctionCall jassExprFunctionCall) {}
	}
}