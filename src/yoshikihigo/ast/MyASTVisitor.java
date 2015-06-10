package yoshikihigo.ast;

import java.util.SortedSet;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.internal.core.dom.NaiveASTFlattener;

public class MyASTVisitor extends NaiveASTFlattener {

	final SortedSet<String> identifiers;

	public MyASTVisitor(final SortedSet<String> identifiers) {
		this.identifiers = identifiers;
	}

	@Override
	public boolean visit(final ImportDeclaration node) {
		return false;
	}

	@Override
	public boolean visit(final MethodDeclaration node) {

		for (final Object parameter : node.parameters()) {
			((ASTNode) parameter).accept(this);
		}

		if (null != node.getBody()) {
			node.getBody().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final MethodInvocation node) {

		for (final Object argument : node.arguments()) {
			((ASTNode) argument).accept(this);
		}

		if (null != node.getExpression()) {
			node.getExpression().accept(this);
		}

		return false;
	}

	@Override
	public boolean visit(final SimpleName node) {
		final String name = node.getIdentifier();
		if (Character.isLowerCase(name.charAt(0))) {
			this.identifiers.add(name);
		}
		return false;
	}

	@Override
	public boolean visit(final SimpleType node) {
		return false;
	}

	@Override
	public boolean visit(final TypeDeclaration node) {

		for (final Object body : node.bodyDeclarations()) {
			((ASTNode) body).accept(this);
		}

		return false;
	}
}
