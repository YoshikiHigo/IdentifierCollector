package yoshikihigo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import yoshikihigo.ast.MyASTVisitor;

public class CollectFromProject {

	public static void main(final String[] args) {
		final CollectFromProject collector = new CollectFromProject(new File(
				args[0]));
		collector.collect();
	}

	final private File root;
	final public SortedSet<String> identifiers;

	public CollectFromProject(final File root) {
		this.identifiers = new TreeSet<>();
		this.root = root;
	}

	public void collect() {
		final List<File> files = getFiles(root);
		for (final File file : files) {
			final CompilationUnit unit = createAST(file);
			final MyASTVisitor visitor = new MyASTVisitor(identifiers);
			unit.accept(visitor);
		}

		System.out.print(Integer.toString(identifiers.size()));
		System.out.print(" identifiers were found: ");
//		for (final String identifier : identifiers) {
//			System.out.print(identifier);
//			System.out.print(", ");
//		}
		System.out.println();
	}

	private List<File> getFiles(final File file) {

		final List<File> files = new ArrayList<File>();
		if (file.isDirectory()) {
			if (!file.getName().equalsIgnoreCase("test")
					&& !file.getName().equalsIgnoreCase("testfiles")) {
				for (final File child : file.listFiles()) {
					files.addAll(getFiles(child));
				}
			}
		}

		else if (file.isFile()) {
			if (file.getName().endsWith(".java")) {
				files.add(file);
			}
		}

		return files;
	}

	private CompilationUnit createAST(final File file) {

		final String lineSeparator = System.getProperty("line.separator");
		final StringBuffer text = new StringBuffer();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(file), "JISAutoDetect"))) {

			while (reader.ready()) {
				final String line = reader.readLine();
				text.append(line);
				text.append(lineSeparator);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		final ASTParser parser = ASTParser.newParser(AST.JLS4);
		parser.setSource(text.toString().toCharArray());
		return (CompilationUnit) parser.createAST(new NullProgressMonitor());
	}
}
