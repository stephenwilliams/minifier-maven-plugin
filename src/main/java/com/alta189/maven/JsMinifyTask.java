package com.alta189.maven;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class JsMinifyTask extends MinifyTask {

	public JsMinifyTask(MinifyMojo mojo) {
		super(mojo);
	}

	@Override
	public void minify() {
		mojo.getLog().info("JavaScript Minifying Started");
		for (File file : mojo.getJsSourceFiles()) {
			String path = file.getAbsolutePath().substring(mojo.getWebappSourceDir().getAbsolutePath().length());
			if (path.startsWith("\\") || path.startsWith("/")) {
				path = path.substring(1);
			}
			File outputFile = new File(mojo.getWebappTargetDir(), path);
			outputFile.getParentFile().mkdirs();
			mojo.getLog().info("Minifying '" + file.getName() + "'");

			InputStream in = null;
			OutputStream out = null;
			InputStreamReader reader = null;
			OutputStreamWriter writer= null;

			try {
				in = new FileInputStream(file);
				out = new FileOutputStream(outputFile);

				if (mojo.getCharset() == null) {
					reader = new InputStreamReader(in);
					writer = new OutputStreamWriter(out);
				} else {
					reader = new InputStreamReader(in, mojo.getCharset());
					writer = new OutputStreamWriter(out, mojo.getCharset());
				}

				JavaScriptCompressor compressor = new JavaScriptCompressor(reader, new JavaScriptErrorReporter(mojo.getLog(), file.getName()));
				compressor.compress(writer, mojo.getLinebreak(), mojo.isNomunge(), mojo.isVerbose(), mojo.isPreserveAllSemiColons(), mojo.isDisableOptimizations());

				if (outputFile.exists()) {
					outputFiles.add(outputFile);
				}
			} catch (Exception e) {
				mojo.getLog().error(e);
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException ignored) {
					}
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (IOException ignored) {
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException ignored) {
					}
				}
				if (in != null) {
					try {
						in.close();
					} catch (IOException ignored) {
					}
				}
			}
		}
	}
}
