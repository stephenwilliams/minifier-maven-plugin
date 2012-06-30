package com.alta189.maven;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @goal minify
 *
 * @phase prepare-package
 */
public class MinifyMojo extends AbstractMojo {

	/**
	 * Webapp source directory.
	 *
	 * @parameter expression="${webappSourceDir}" default-value="${basedir}/src/main/webapp"
	 */
	private String webappSourceDir;

	/**
	 * Webapp target directory.
	 *
	 * @parameter expression="${webappTargetDir}" default-value="${project.build.directory}/${project.build.finalName}"
	 */
	private String webappTargetDir;

	/**
	 * CSS source directory.
	 *
	 * @parameter expression="${cssSourceDir}" default-value="css"
	 */
	private String cssSourceDir;

	/**
	 * JavaScript source directory.
	 *
	 * @parameter expression="${jsSourceDir}" default-value="js"
	 */
	private String jsSourceDir;

	/**
	 * CSS source filenames list.
	 *
	 * @parameter expression="${cssSourceFiles}" alias="cssFiles"
	 */
	private List<String> cssSourceFiles;

	/**
	 * JavaScript source filenames list.
	 *
	 * @parameter expression="${jsSourceFiles}" alias="jsFiles"
	 */
	private List<String> jsSourceFiles;

	/**
	 * CSS files to include. Specified as fileset patterns which are relative to the CSS source directory.
	 *
	 * @parameter expression="${cssSourceIncludes}" alias="cssIncludes"
	 * @since 1.2
	 */
	private List<String> cssSourceIncludes;

	/**
	 * JavaScript files to include. Specified as fileset patterns which are relative to the JavaScript source directory.
	 *
	 * @parameter expression="${jsSourceIncludes}" alias="jsIncludes"
	 * @since 1.2
	 */
	private List<String> jsSourceIncludes;

	/**
	 * CSS files to exclude. Specified as fileset patterns which are relative to the CSS source directory.
	 *
	 * @parameter expression="${cssSourceExcludes}" alias="cssExcludes"
	 * @since 1.2
	 */
	private List<String> cssSourceExcludes;

	/**
	 * JavaScript files to exclude. Specified as fileset patterns which are relative to the JavaScript source directory.
	 *
	 * @parameter expression="${jsSourceExcludes}" alias="jsExcludes"
	 * @since 1.2
	 */
	private List<String> jsSourceExcludes;

	/**
	 * <p>
	 * If a supported character set is specified, it will be used to read the input file. Otherwise, it will assume that
	 * the platform's default character set is being used. The output file is encoded using the same character set.
	 * </p>
	 * <p>
	 * See the <a href="http://www.iana.org/assignments/character-sets">IANA Charset Registry</a> for a list of valid
	 * encoding types.
	 * </p>
	 *
	 * @parameter expression="${charset}"
	 */
	private String charset;

	/**
	 * Some source control tools don't like files containing lines longer than, say 8000 characters. The linebreak
	 * option is used in that case to split long lines after a specific column. It can also be used to make the code
	 * more readable, easier to debug (especially with the MS Script Debugger). Specify 0 to get a line break after each
	 * semi-colon in JavaScript, and after each rule in CSS. Specify -1 to disallow line breaks.
	 *
	 * @parameter expression="${linebreak}" default-value="-1"
	 */
	private int linebreak;

	/**
	 * JAVASCRIPT ONLY OPTION!<br/>
	 * Minify only. Do not obfuscate local symbols.
	 *
	 * @parameter expression="${munge}" default-value="false"
	 */
	private boolean nomunge;

	/**
	 * JAVASCRIPT ONLY OPTION!<br/>
	 * Display informational messages and warnings.
	 *
	 * @parameter expression="${verbose}" default-value="false"
	 */
	private boolean verbose;

	/**
	 * JAVASCRIPT ONLY OPTION!<br/>
	 * Preserve unnecessary semicolons (such as right before a '}'). This option is useful when compressed code has to
	 * be run through JSLint (which is the case of YUI for example).
	 *
	 * @parameter expression="${preserveAllSemiColons}" default-value="false"
	 */
	private boolean preserveAllSemiColons;

	/**
	 * JAVASCRIPT ONLY OPTION!<br/>
	 * Disable all the built-in micro optimizations.
	 *
	 * @parameter expression="${disableOptimizations}" default-value="false"
	 */
	private boolean disableOptimizations;

	/**
	 * Size of the buffer used to read source files.
	 *
	 * @parameter expression="${bufferSize}" default-value="4096"
	 */
	private int bufferSize;

	/**
	 * Maven Project
	 *
	 * @parameter expression="${mavenProject}" default-value="${project}"
	 */
	private MavenProject project;

	private final FileList jsSourceFileList = new FileList();
	private final FileList cssSourceFileList = new FileList();

	public void execute() throws MojoExecutionException {
		Log log = getLog();
		log.info("Minify Plugin executing");
		MinifyTask jsTask = new JsMinifyTask(this);
		MinifyTask cssTask = new CssMinifyTask(this);
		jsTask.run();
		cssTask.run();

		// Ask War Plugin to save
		List<File> outputFiles = new ArrayList<File>();
		outputFiles.addAll(jsTask.getOutputFiles());
		outputFiles.addAll(cssTask.getOutputFiles());

		WarPlugin warPlugin = new WarPlugin(this, outputFiles);
		warPlugin.run();
	}

	public String getCharset() {
		return charset;
	}

	public int getLinebreak() {
		return linebreak;
	}

	public boolean isNomunge() {
		return nomunge;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public boolean isPreserveAllSemiColons() {
		return preserveAllSemiColons;
	}

	public boolean isDisableOptimizations() {
		return disableOptimizations;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public File getJsSourceDir() {
		return new File(getWebappSourceDir(), jsSourceDir);
	}

	public File getWebappSourceDir() {
		return new File(webappSourceDir);
	}

	public File getWebappTargetDir() {
		return new File(webappTargetDir);
	}

	public File getCssSourceDir() {
		return new File(getWebappSourceDir(), cssSourceDir);
	}

	public List<File> getCssSourceFiles() {
		if (cssSourceFileList.size() < 1) {
			processCssSourceFiles();
		}
		return cssSourceFileList;
	}

	public void processCssSourceFiles() {
		File dir = getCssSourceDir();
		for (String cssSourceInclude : cssSourceIncludes) {
			cssSourceFileList.add(dir, cssSourceInclude);
		}
		for (String cssSourceExclude : cssSourceExcludes) {
			cssSourceFileList.remove(dir, cssSourceExclude);
		}
	}

	public List<File> getJsSourceFiles() {
		if (jsSourceFileList.size() < 1) {
			processJsSourceFiles();
		}
		return jsSourceFileList;
	}

	public void processJsSourceFiles() {
		File dir = getJsSourceDir();
		for (String cssSourceInclude : jsSourceIncludes) {
			jsSourceFileList.add(dir, cssSourceInclude);
		}
		for (String cssSourceExclude : jsSourceExcludes) {
			jsSourceFileList.remove(dir, cssSourceExclude);
		}
	}

	public MavenProject getProject() {
		return project;
	}
}
