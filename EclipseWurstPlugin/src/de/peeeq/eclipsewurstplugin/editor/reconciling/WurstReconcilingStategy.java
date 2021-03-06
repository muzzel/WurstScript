package de.peeeq.eclipsewurstplugin.editor.reconciling;

import java.io.StringReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;

import de.peeeq.eclipsewurstplugin.builder.ModelManager;
import de.peeeq.eclipsewurstplugin.builder.WurstNature;
import de.peeeq.eclipsewurstplugin.editor.WurstEditor;
import de.peeeq.wurstscript.ast.CompilationUnit;
import de.peeeq.wurstscript.attributes.CompileError;
import de.peeeq.wurstscript.gui.WurstGui;
import de.peeeq.wurstscript.gui.WurstGuiLogger;

public class WurstReconcilingStategy implements IReconcilingStrategy {

	private WurstEditor editor;
	private IDocument document;
	private int lastReconcileDocumentHashcode = -1;

	public WurstReconcilingStategy(WurstEditor editor) {
		this.editor = editor;
		editor.setReconciler(this);
		
	}
	
	@Override
	public void setDocument(IDocument document) {
		this.document = document;
		
	}

	@Override
	public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
		reconcile(true);
	}

	@Override
	public void reconcile(IRegion partition) {
		reconcile(true);
	}

	public CompilationUnit reconcile(boolean doTypecheck) {
		ModelManager mm = editor.getModelManager();
		WurstGui gui = new WurstGuiLogger();
		IFile file = editor.getFile();
		if (file != null) {
			// TODO handle parser-error markers
			
			String doc = document.get();
			lastReconcileDocumentHashcode = doc.hashCode();
			CompilationUnit cu = mm.parse(gui, file.getProjectRelativePath().toString(), new StringReader(doc));
			if (gui.getErrorCount() > 0) {
				// errors after parse, abort
				return cu;
			}
			if (doTypecheck) {
				mm.typeCheckModel(gui, true, false);
			}
			return cu;
		}
		return null;
	}

	public int getLastReconcileDocumentHashcode() {
		return lastReconcileDocumentHashcode;
	}


}
