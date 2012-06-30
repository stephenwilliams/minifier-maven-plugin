package com.alta189.maven;

import org.apache.maven.plugin.logging.Log;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

public class JavaScriptErrorReporter implements ErrorReporter {
	private final Log log;
   	private final String file;

	public JavaScriptErrorReporter(Log log, String file) {
		this.log = log;
		this.file = file;
	}

	@Override
	public void warning(String message, String sourceName, int line, String lineSource, int lineOffset) {
		if (line < 0) {
			log.warn(message);
		} else {
			log.warn("[" + file + ":" + line + "] " + message);
		}
	}

	@Override
	public void error(String message, String sourceName, int line, String lineSource, int lineOffset) {
		if (line < 0) {
			log.error(message);
		} else {
			log.error("[" + file + ":" + line + "] " + message);
		}
	}

	@Override
	public EvaluatorException runtimeError(String message, String sourceName, int line, String lineSource, int lineOffset) {
		error(message, sourceName, line, lineSource, lineOffset);
		return new EvaluatorException(message);
	}
}
