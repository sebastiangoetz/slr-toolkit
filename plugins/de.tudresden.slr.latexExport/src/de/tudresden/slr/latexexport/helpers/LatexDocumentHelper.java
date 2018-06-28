package de.tudresden.slr.latexexport.helpers;

public class LatexDocumentHelper {
	private static final String EXAMPLE1 = "\\documentclass{article}\r\n" + 
			"\\usepackage{graphicx}\r\n" + 
			"\r\n" + 
			"\\begin{document}\r\n" + 
			"\r\n" + 
			"\\title{Introduction to \\LaTeX{}}\r\n" + 
			"\\author{Author's Name}\r\n" + 
			"\r\n" + 
			"\\maketitle\r\n" + 
			"\r\n" + 
			"\\begin{abstract}\r\n" + 
			"The abstract text goes here.\r\n" + 
			"\\end{abstract}\r\n" + 
			"\r\n" + 
			"\\section{Introduction}\r\n" + 
			"Here is the text of your introduction.\r\n" + 
			"\r\n" + 
			"\\begin{equation}\r\n" + 
			"    \\label{simple_equation}\r\n" + 
			"    \\alpha = \\sqrt{ \\beta }\r\n" + 
			"\\end{equation}\r\n" + 
			"\r\n" + 
			"\\subsection{Testinsert}\r\n";
	
	private static final String EXAMPLE2 = "\\subsection{Subsection Heading Here}\r\n" + 
			"Write your subsection text here.\r\n" + 
			"\r\n" + 
			"\\begin{figure}\r\n" + 
			"    \\centering\r\n" + 
			"    \\caption{Simulation Results}\r\n" + 
			"    \\label{simulationfigure}\r\n" + 
			"\\end{figure}\r\n" + 
			"\r\n" + 
			"\\section{Conclusion}\r\n" + 
			"Write your conclusion here.\r\n" + 
			"\r\n" + 
			"\\end{document}";
	
	public static String getExample1() {
		return EXAMPLE1;
	}
	
	public static String getExample2() {
		return EXAMPLE2;
	}
}
