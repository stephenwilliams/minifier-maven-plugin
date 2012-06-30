package com.alta189.maven;

import org.apache.maven.plugin.logging.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public abstract class MinifyTask {
	protected MinifyMojo mojo;
	protected final List<File> outputFiles = new ArrayList<File>();

	public MinifyTask(MinifyMojo mojo) {
		this.mojo = mojo;
	}

	public void run() {
		minify();
	}

	public List<File> getOutputFiles() {
		return outputFiles;
	}

	public abstract void minify();

}
