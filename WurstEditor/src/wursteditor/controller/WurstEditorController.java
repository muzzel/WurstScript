package wursteditor.controller;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.PatternFilenameFilter;

import de.peeeq.wurstscript.attributes.CompileError;

import de.peeeq.wurstscript.utils.Utils;
import java.awt.EventQueue;
import wursteditor.WurstEditFileView;
import wursteditor.WurstEditorView;

/**
 * this class connects the gui with the underlying logic 
 */
public class WurstEditorController {
	private WurstEditorView view;

	
	public WurstEditorController(final WurstEditorView v) {
		v.getOpenProjectButton().addActionListener(onClick_openProject(v));
		v.getSaveFileButton().addActionListener(onClick_saveFile());
		v.getUndoButton().addActionListener(onclick_undo(v));
		v.getRedoButton().addActionListener(onclick_redo(v));
		
	}
        

	
	private ActionListener onclick_redo(final WurstEditorView v) {
		this.view = v;
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				v.getSyntaxCodeArea().redoLastAction();
			}
		};
	}

	private ActionListener onclick_undo(final WurstEditorView v) {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				v.getSyntaxCodeArea().undoLastAction();
			}
		};
	}

	private ActionListener onClick_openProject(final WurstEditorView v) {
		this.view = v;
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
                            EventQueue.invokeLater(new Runnable() {
                                 public void run() {
                                    int returnVal = view.getjFileChooser1().showOpenDialog(view.getFrame());
                                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                                        File currentFile = view.getjFileChooser1().getSelectedFile();
                                        System.out.println("File: " + currentFile.getName());
                                        if (currentFile.getName().endsWith(".wurst")) {
                                            openFile(currentFile);
                                        }

                                    } else {
                                        System.out.println("File access cancelled by user.");
                                    }
                                 }
                             });
			}

			
		};
	}
	
	private void openFile(File file) {
		if (!file.exists()) {
			throw new Error("File " +file.getAbsolutePath() + " does not exist.");
		}
		// check if already opened:
		for (int i = 0; i < view.getRSTASplitPane().getTabCount(); i++) {
			if (view.getRSTASplitPane().getTabComponentAt(i) instanceof WurstEditFileView) {
				WurstEditFileView we = (WurstEditFileView) view.getRSTASplitPane().getTabComponentAt(i);
				if (we.getFileName().equals(file.getAbsolutePath())) {
					view.getRSTASplitPane().setSelectedIndex(i);
					return;
				}
			}
		}
                System.out.println(view);
		System.out.println(view.getErrorList());
		System.out.println(file.getAbsolutePath());
		WurstEditFileView fileView;
		try {
			fileView = new WurstEditFileView(new RSyntaxTextArea(), file.getAbsolutePath(), view.getErrorList());
		
			String text = "";
			try {
				text = Files.toString(file, Charsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
			fileView.getSyntaxCodeArea().setText(text);
			view.getRSTASplitPane().addTab(file.getName(), fileView);
			view.getRSTASplitPane().setSelectedComponent(fileView);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	private ActionListener onClick_saveFile() {
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Component current = view.getRSTASplitPane().getSelectedComponent();
				if (current instanceof WurstEditFileView) {
					WurstEditFileView we = (WurstEditFileView) current;
					String text = we.getSyntaxCodeArea().getText();
					String fileName = we.getFileName();
					
					try {
						Files.write(text, new File(fileName), Charsets.UTF_8);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// reparse
					we.getSyntaxCodeArea().addNotify();
				} else {
					System.err.println("no file selected");
				}
			}
		};
	}

}
