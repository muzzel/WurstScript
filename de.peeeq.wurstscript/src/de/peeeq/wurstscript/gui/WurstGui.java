package de.peeeq.wurstscript.gui;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import de.peeeq.wurstscript.attributes.CompileError;
import de.peeeq.wurstscript.attributes.CompileError.ErrorType;
import de.peeeq.wurstscript.utils.Utils;

public abstract class WurstGui {
	
	private List<CompileError> errors = Lists.newArrayList();
	
	
	
	public abstract void sendProgress(String whatsRunningNow, double percent);
	
	public abstract void sendFinished();

	public abstract void showInfoMessage(String message);
	
	public void sendError(CompileError err) {
		errors.add(err);
	}
	
	public void clearErrors() {
		errors.clear();
	}

	public final int getErrorCount() {
		return getErrorList().size();
	}

	public final String getErrors() {
		return Utils.join(errors, "\n");
	}

	public final List<CompileError> getErrorList() {
		return errors.stream()
				.filter(e -> e.getErrorType() == ErrorType.ERROR)
				.collect(Collectors.toList());
	}
	
	public final List<CompileError> getWarningList() {
		return errors.stream()
				.filter(e -> e.getErrorType() == ErrorType.WARNING)
				.collect(Collectors.toList());
	}

	
}
