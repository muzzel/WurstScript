package de.peeeq.wurstscript.jassprinter;

import java.util.List;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.peeeq.wurstscript.jassAst.JassArrayVar;
import de.peeeq.wurstscript.jassAst.JassAstElement;
import de.peeeq.wurstscript.jassAst.JassConstantVar;
import de.peeeq.wurstscript.jassAst.JassFunction;
import de.peeeq.wurstscript.jassAst.JassFunctions;
import de.peeeq.wurstscript.jassAst.JassInitializedVar;
import de.peeeq.wurstscript.jassAst.JassNative;
import de.peeeq.wurstscript.jassAst.JassNatives;
import de.peeeq.wurstscript.jassAst.JassOp;
import de.peeeq.wurstscript.jassAst.JassOpAnd;
import de.peeeq.wurstscript.jassAst.JassOpDiv;
import de.peeeq.wurstscript.jassAst.JassOpEquals;
import de.peeeq.wurstscript.jassAst.JassOpGreater;
import de.peeeq.wurstscript.jassAst.JassOpGreaterEq;
import de.peeeq.wurstscript.jassAst.JassOpLess;
import de.peeeq.wurstscript.jassAst.JassOpLessEq;
import de.peeeq.wurstscript.jassAst.JassOpMinus;
import de.peeeq.wurstscript.jassAst.JassOpMult;
import de.peeeq.wurstscript.jassAst.JassOpNot;
import de.peeeq.wurstscript.jassAst.JassOpOr;
import de.peeeq.wurstscript.jassAst.JassOpPlus;
import de.peeeq.wurstscript.jassAst.JassOpUnequals;
import de.peeeq.wurstscript.jassAst.JassProg;
import de.peeeq.wurstscript.jassAst.JassSimpleVar;
import de.peeeq.wurstscript.jassAst.JassStatement;
import de.peeeq.wurstscript.jassAst.JassStmtSet;
import de.peeeq.wurstscript.jassAst.JassTypeDef;
import de.peeeq.wurstscript.jassAst.JassTypeDefs;
import de.peeeq.wurstscript.jassAst.JassVar;
import de.peeeq.wurstscript.jassAst.JassVars;
import de.peeeq.wurstscript.utils.Utils;

public class JassPrinter {

	private boolean withSpace;
	private JassProg prog;
   

	public JassPrinter(boolean withSpace) {
		this.withSpace = withSpace;
	}
	
	public void printProg(StringBuilder sb, JassProg prog) {
		Preconditions.checkNotNull(sb);
		Preconditions.checkNotNull(prog);
		this.prog = prog;
		
		printTypes(sb, prog.getDefs());
		printGlobals(sb, prog.getGlobals());
		printNatives(sb, prog.getNatives());
		printFunctions(sb, prog.getFunctions());
	}
	
	private String additionalNewline() {
		return withSpace ? "\n" : "";
	}

	static void printIndent(StringBuilder sb, int i, boolean withSpace) {
		if (withSpace) {
			Utils.printIndent(sb, i);
		}
	}

	static String comma(boolean withSpace) {
		return withSpace ? ", " : ",";
	}
	
	static String assign(boolean withSpace) {
		return withSpace ? " = " : "=";
	}


	private void printGlobals(StringBuilder sb, JassVars globals) {
		sb.append("globals\n");
		for (JassVar g : globals) {
			printJassGlobalVar(sb, g);
		}
		sb.append("endglobals\n");
	}
	
	private void printTypes(StringBuilder sb, JassTypeDefs defs) {
		for (JassTypeDef d : defs) {
			printTypeDef(d, sb, false);
		}
	}
	
	private void printNatives(StringBuilder sb, JassNatives natives) {
		for (JassNative n : natives) {
			printNative(n, sb, false);
		}
	}

	private void printJassGlobalVar(final StringBuilder sb, JassVar g) {
		if (prog.attrIgnoredVariables().contains(g)) {
			return;
		}
		g.match(new JassVar.MatcherVoid() {
			
			@Override
			public void case_JassSimpleVar(JassSimpleVar v) {
				sb.append(v.getType() + " " + v.getName() + "\n");
			}
			
			@Override
			public void case_JassArrayVar(JassArrayVar v) {
				sb.append(v.getType() + " array " + v.getName() + "\n");
			}

			@Override
			public void case_JassInitializedVar(JassInitializedVar jassInitializedVar) {
				sb.append(jassInitializedVar.getType() + " " + jassInitializedVar.getName() + "=");
				 jassInitializedVar.getVal().print(sb, withSpace);
				 sb.append("\n");
				// TODO check if right
			}

			@Override
			public void case_JassConstantVar(JassConstantVar jassConstantVar) {
				sb.append(jassConstantVar.getType() + " " + jassConstantVar.getName() + "=" );
				jassConstantVar.getVal().print(sb, withSpace); 
				 sb.append("\n");
				// TODO check if right
			}
		});
	}
	

	private void printFunctions(StringBuilder sb, JassFunctions functions) {
		for (JassFunction f : functions) {
			printFunction(sb, f);
		}
	}


	private void printFunction(StringBuilder sb, JassFunction f) {
		if (prog.attrIgnoredFunctions().contains(f)) {
			return;
		}
		printComment(sb, f, 0);
		sb.append("function ");
		sb.append(f.getName());
		sb.append(" takes ");
		if (f.getParams().size() == 0) {
			sb.append("nothing");
		} else {
				Utils.printSep(sb, comma(withSpace), f.getParams(), new Function<JassSimpleVar, String>() {

					@Override
					public String apply(JassSimpleVar input) {
						return input.getType() + " " + input.getName();
					}
				});				
		}
		sb.append(" returns ");
		sb.append(f.getReturnType());
		sb.append("\n");
		
		List<JassStatement> body = Lists.newLinkedList(f.getBody());
		Set<JassVar> locals = Sets.newLinkedHashSet(); 
		
		
		// first print all the initalized vars:
		for (JassVar v : f.getLocals()) {
			if (v instanceof JassInitializedVar) {
				printIndent(sb, 1, withSpace);
				sb.append("local ");
				sb.append(v.getType());
				sb.append(" ");
				sb.append(v.getName());

				JassInitializedVar ji = (JassInitializedVar) v;
				sb.append(" = ");
				ji.getVal().print(sb, withSpace);
				sb.append("\n");
			} else {
				// if var is not initialized, remember for next step:
				locals.add(v);
			}
		}
		
		
		// set statements at the beginning are merged with local variable declarations...
		while (body.size() > 0 && body.get(0) instanceof JassStmtSet) {
			// get first set statement
			JassStmtSet set = (JassStmtSet) body.get(0);
			JassVar localVar = null;
			// look if there is an uninitialized local var 
			for (JassVar v : locals) {
				if (set.getLeft().equals(v.getName()))  {
					localVar = v;
					break; 
				}
			}
			if (localVar == null) {
				break;
			}
			// there is a local var ==> merge
			printIndent(sb, 1, withSpace);
			sb.append("local ");
			sb.append(localVar.getType());
			sb.append(" ");
			sb.append(localVar.getName());
			sb.append(" = ");
			set.getRight().print(sb, withSpace);
			sb.append("\n");
			
			locals.remove(localVar);
			body.remove(0);
		}
		
		// print the remaining locals
		for (JassVar v : locals) {
			printIndent(sb, 1, withSpace);
			sb.append("local ");
			sb.append(v.getType());
			if (v instanceof JassArrayVar) {
				sb.append(" array");
			}
			sb.append(" ");
			sb.append(v.getName());
			sb.append("\n");
		}
		printStatements(sb, 1, body, withSpace);
		sb.append("endfunction\n" + additionalNewline());
	}

	private void printComment(StringBuilder sb, JassAstElement f, int indent) {
		if (withSpace) {
			if (prog.attrComments().containsKey(f)) {
				printIndent(sb, indent, withSpace);
				sb.append("// " + prog.attrComments().get(f) + "\n");
			}
		}
	}


	

	static void printStatements(StringBuilder sb, int indent, List<JassStatement> statements, boolean withSpace) {
		for (JassStatement s : statements) {
			printIndent(sb, indent, withSpace);
			s.print(sb, indent, withSpace);
			sb.append("\n");
		}
	}


	protected static int precedence(JassOp op) {
		// 5 not
		// 4 * /
		// 3 + -
		// 2 <= >= > < == !=
		// 1 and 
		// 0 or
		return op.match(new JassOp.Matcher<Integer>() {

			@Override
			public Integer case_JassOpDiv(JassOpDiv jassOpDiv) {
				return 4;
			}

			@Override
			public Integer case_JassOpLess(JassOpLess jassOpLess) {
				return 2;
			}

			@Override
			public Integer case_JassOpAnd(JassOpAnd jassOpAnd) {
				return 1;
			}

			@Override
			public Integer case_JassOpUnequals(JassOpUnequals jassOpUnequals) {
				return 2;
			}

			@Override
			public Integer case_JassOpGreaterEq(JassOpGreaterEq jassOpGreaterEq) {
				return 2;
			}

			@Override
			public Integer case_JassOpMinus(JassOpMinus jassOpMinus) {
				return 3;
			}

			@Override
			public Integer case_JassOpMult(JassOpMult jassOpMult) {
				return 4;
			}

			@Override
			public Integer case_JassOpGreater(JassOpGreater jassOpGreater) {
				return 2;
			}

			@Override
			public Integer case_JassOpPlus(JassOpPlus jassOpPlus) {
				return 3;
			}

			@Override
			public Integer case_JassOpLessEq(JassOpLessEq jassOpLessEq) {
				return 2;
			}

			@Override
			public Integer case_JassOpOr(JassOpOr jassOpOr) {
				return 0;
			}

			@Override
			public Integer case_JassOpEquals(JassOpEquals jassOpEquals) {
				return 2;
			}

			@Override
			public Integer case_JassOpNot(JassOpNot jassOpNot) {
				return 5;
			}
		});
	}


	protected void printOp(final StringBuilder sb, JassOp op, boolean parLeft, boolean parRight) {
		String opString = op.match(new JassOp.Matcher<String>() {

			@Override
			public String case_JassOpUnequals(JassOpUnequals jassOpUnequals) {
				return "!=";
			}
			
			@Override
			public String case_JassOpPlus(JassOpPlus jassOpPlus) {
				return "+";
			}
			
			@Override
			public String case_JassOpOr(JassOpOr jassOpOr) {
				return "or";
			}
			
			@Override
			public String case_JassOpNot(JassOpNot jassOpNot) {
				return "not";
			}
			
			@Override
			public String case_JassOpMult(JassOpMult jassOpMult) {
				return "*";
			}
			
			@Override
			public String case_JassOpMinus(JassOpMinus jassOpMinus) {
				return "-";
			}
			
			@Override
			public String case_JassOpLessEq(JassOpLessEq jassOpLessEq) {
				return "<=";
			}
			
			@Override
			public String case_JassOpLess(JassOpLess jassOpLess) {
				return "<";
			}
			
			@Override
			public String case_JassOpGreaterEq(JassOpGreaterEq jassOpGreaterEq) {
				return ">=";
			}
			
			@Override
			public String case_JassOpGreater(JassOpGreater jassOpGreater) {
				return ">";
			}
			
			@Override
			public String case_JassOpEquals(JassOpEquals jassOpEquals) {
				return "==";
			}
			
			@Override
			public String case_JassOpDiv(JassOpDiv jassOpDiv) {
				return "/";
			}
			
			@Override
			public String case_JassOpAnd(JassOpAnd jassOpAnd) {
				return "and";
			}
		});
		if (withSpace || !parLeft && Character.isLetter(opString.charAt(0))) {
			// if we have no parantheses on the left and an operator like and/or we need a space:
			sb.append(" ");
		}
		sb.append(opString);
		if (withSpace || !parRight && Character.isLetter(opString.charAt(0))) {
			// if we have no parantheses on the right and an operator like and/or we need a space:
			sb.append(" ");
		}
	}


	public String printProg(JassProg prog) {
		Preconditions.checkNotNull(prog);
		StringBuilder sb = new StringBuilder();
		printProg(sb, prog);
		return sb.toString();
	}

	public static void printTypeDef(JassTypeDef jassTypeDef,
			StringBuilder sb, boolean withSpace2) {
		sb.append("type ");
		sb.append(jassTypeDef.getName());
		sb.append(" extends ");
		sb.append(jassTypeDef.getExt());
		sb.append("\n");
	}

	public static void printNative(JassNative jassNative,
			StringBuilder sb, boolean withSpace) {
		sb.append("native ");
		sb.append(jassNative.getName());
		sb.append(" takes ");
		if (jassNative.getParams().size() == 0) {
			sb.append("nothing");
		} else {
				Utils.printSep(sb, comma(withSpace), jassNative.getParams(), new Function<JassSimpleVar, String>() {

					@Override
					public String apply(JassSimpleVar input) {
						return input.getType() + " " + input.getName();
					}
				});				
		}
		sb.append(" returns ");
		sb.append(jassNative.getReturnType());
		sb.append("\n");
		
	}
	
}
