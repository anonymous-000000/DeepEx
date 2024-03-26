package utils;

import org.eclipse.jdt.core.dom.*;

import java.util.List;

public class JavaASTUtil {
    public static String getFQN(FieldAccess node) {
        if (node.getExpression() instanceof ThisExpression) {
            IVariableBinding vb = node.resolveFieldBinding();
            if (vb != null && vb.getDeclaringClass() != null) {
                ITypeBinding tb = vb.getDeclaringClass().getTypeDeclaration();
                String qn = getQualifiedName(tb);
                if (!qn.isEmpty())
                    return qn + "." + node.getName().getIdentifier();
            }
        }
        return null;
    }
    
    public static String getFQN(SuperFieldAccess node) {
        IVariableBinding vb = node.resolveFieldBinding();
        if (vb != null && vb.getDeclaringClass() != null) {
            ITypeBinding tb = vb.getDeclaringClass().getTypeDeclaration();
            String qn = getQualifiedName(tb);
            if (!qn.isEmpty())
                return qn + "." + node.getName().getIdentifier();
        }
        return null;
    }
    
    public static String getQualifiedName(ITypeBinding tb) {
        String qn = tb.getQualifiedName();
        if (qn.isEmpty())
            qn = tb.getName();
        return qn;
    }

    public static String getFQN(ASTNode node) {
        if (node instanceof Name)
            return getFQN((Name) node);
        if (node instanceof FieldAccess)
            return getFQN((FieldAccess) node);
        if (node instanceof SuperFieldAccess)
            return getFQN((SuperFieldAccess) node);
        return null;
    }

    public static String getFQN(Name node) {
        if (node.isQualifiedName()) {
            QualifiedName qn = (QualifiedName) node;
            String qual = getFQN(qn.getQualifier());
            if (qual != null)
                return qual + "." + qn.getName().getIdentifier();
        } else {
            IBinding b = node.resolveBinding();
            if (b != null && b instanceof IVariableBinding) {
                IVariableBinding vb = (IVariableBinding) b;
                if (vb.isField()) {
                    ITypeBinding tb = vb.getDeclaringClass();
                    if (tb != null) {
                        tb = tb.getTypeDeclaration();
                        String qn = getQualifiedName(tb);
                        if (!qn.isEmpty())
                            return qn + "." + node.toString();
                    }
                }
            }
        }
        return null;
    }

    public static void replace(ASTNode oldNode, ASTNode newNode) {
        ASTNode parent = oldNode.getParent();
        StructuralPropertyDescriptor location = oldNode.getLocationInParent();
        if (location.isChildProperty()) {
            parent.setStructuralProperty(location, newNode);
        }
        if (location.isChildListProperty()) {
            List<ASTNode> list = ((List<ASTNode>) parent.getStructuralProperty(location));
            int index = list.indexOf(oldNode);
            list.set(index, newNode);
        }
    }

    public static Expression negate(Expression ex) {
        Expression e = (Expression) ASTNode.copySubtree(ex.getAST(), ex);
        return e;
    }

    public static String getSimpleType(Type type) {
        if (type.isArrayType()) {
            ArrayType t = (ArrayType) type;
            String pt = getSimpleType(t.getElementType());
            for (int i = 0; i < t.getDimensions(); i++)
                pt += "[]";
            return pt;
            //return type.toString();
        } else if (type.isParameterizedType()) {
            ParameterizedType t = (ParameterizedType) type;
            return getSimpleType(t.getType());
        } else if (type.isPrimitiveType()) {
            String pt = type.toString();
			/*if (pt.equals("byte") || pt.equals("short") || pt.equals("int") || pt.equals("long")
					|| pt.equals("float") || pt.equals("double"))
				return "number";*/
            return pt;
        } else if (type.isQualifiedType()) {
            QualifiedType t = (QualifiedType) type;
            return t.getName().getIdentifier();
        } else if (type.isSimpleType()) {
            String pt = type.toString();
            pt = FileIO.getSimpleClassName(pt);
			/*if (pt.equals("Byte") || pt.equals("Short") || pt.equals("Integer") || pt.equals("Long")
					|| pt.equals("Float") || pt.equals("Double"))
				return "number";*/
            return pt;
        } else if (type.isUnionType()) {
            UnionType ut = (UnionType) type;
            String s = getSimpleType((Type) ut.types().get(0));
            for (int i = 1; i < ut.types().size(); i++)
                s += "|" + getSimpleType((Type) ut.types().get(i));
            return s;
        } else if (type.isWildcardType()) {
            //WildcardType t = (WildcardType) type;
            System.err.println("ERROR: Declare a variable with wildcard type!!!");
            System.exit(0);
        }
        System.err.println("ERROR: Declare a variable with unknown type!!!");
        System.exit(0);
        return null;
    }

    public static String buildNameWithParameters(MethodDeclaration method) {
        return method.getName().getIdentifier() + getParameters(method);
    }

    public static String getParameters(MethodDeclaration method) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < method.parameters().size(); i++) {
            SingleVariableDeclaration d = (SingleVariableDeclaration) (method.parameters().get(i));
            String type = getSimpleType(d.getType());
            for (int j = 0; j < d.getExtraDimensions(); j++)
                type += "[]";
            if (d.isVarargs())
                type += "[]";
            sb.append(" " + type);
        }
        sb.append(" )");
        return sb.toString();
    }

    public static boolean isLiteral(int astNodeType) {
        return ASTNode.nodeClassForType(astNodeType).getSimpleName().endsWith("Literal");
    }

    public static Expression normalize(Expression e) {
        if (e instanceof PrefixExpression) {
            PrefixExpression pe = (PrefixExpression) e;
            if (pe.getOperator() == PrefixExpression.Operator.NOT)
                return negate(pe.getOperand());
        }
        return e;
    }
}
