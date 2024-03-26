package evaluation;

import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.List;

public class APICallVisitor extends ASTVisitor {
	public List<APICallSpec> apiCallSpecList = new ArrayList<>();
	private CompilationUnit compilationUnit;

	public APICallVisitor(CompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}
	
	@Override
	public boolean visit(MethodInvocation node) {
		int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition());
		int nodeLength = node.getLength();
		int endLineNumber = compilationUnit.getLineNumber(node.getStartPosition() + nodeLength);
		apiCallSpecList.add(new APICallSpec(node, startLineNumber, endLineNumber));
		return true;
	}

	@Override
	public boolean visit(SuperMethodInvocation node) {
		int startLineNumber = compilationUnit.getLineNumber(node.getStartPosition());
		int nodeLength = node.getLength();
		int endLineNumber = compilationUnit.getLineNumber(node.getStartPosition() + nodeLength);
		apiCallSpecList.add(new APICallSpec(node, startLineNumber, endLineNumber));
		return true;
	}
}
