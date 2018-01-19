package calculator.ast;
import calculator.interpreter.Environment;
import calculator.errors.EvaluationError;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import misc.exceptions.NotYetImplementedException;
import calculator.errors.EvaluationError;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NotYetImplementedException;

/**
 * All of the static methods in this class are given the exact same parameters for
 * consistency. You can often ignore some of these parameters when implementing your
 * methods.
 *
 * Some of these methods should be recursive. You may want to consider using public-private
 * pairs in some cases.
 */
public class ExpressionManipulators {
    /**
     * Accepts an 'toDouble(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'toDouble'.
     * - The 'node' parameter has exactly one child: the AstNode to convert into a double.
     *
     * Postconditions:
     *
     * - Returns a number AstNode containing the computed double.
     *
     * For example, if this method receives the AstNode corresponding to
     * 'toDouble(3 + 4)', this method should return the AstNode corresponding
     * to '7'.
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if any of the expressions uses an unknown operation.
     */
    public static AstNode handleToDouble(Environment env, AstNode node) {
        // To help you get started, we've implemented this method for you.
        // You should fill in the TODOs in the 'toDoubleHelper' method.
        return new AstNode(toDoubleHelper(env.getVariables(), node.getChildren().get(0)));
    }
        
    private static double toDoubleHelper(IDictionary<String, AstNode> variables, AstNode node) {
        // There are three types of nodes, so we have three cases.
        if (node.isNumber()) {
            return node.getNumericValue();
        } else if (node.isVariable()) {
            if (!variables.containsKey(node.getName())) {
                throw new EvaluationError("This variable hasn't been defined");
            }
            return toDoubleHelper(variables, variables.get(node.getName()));
        } else if (node.isOperation()) {
            String name = node.getName();
            IList<AstNode> list = node.getChildren();
            //Double number = 0.0;
            if (name.equals("+")) {
                return toDoubleHelper(variables, list.get(0)) + toDoubleHelper(variables, list.get(1));
            } else if (name.equals("-")) {
                return toDoubleHelper(variables, list.get(0)) - toDoubleHelper(variables, list.get(1));
            } else if (name.equals("*")) {
                return toDoubleHelper(variables, list.get(0)) * toDoubleHelper(variables, list.get(1));
            } else if (name.equals("/")) {
                return toDoubleHelper(variables, list.get(0)) / toDoubleHelper(variables, list.get(1));    
            } else if (name.equals("^")) {
                return Math.pow(toDoubleHelper(variables, list.get(0)), toDoubleHelper(variables, list.get(1)));
            } else if (name.equals("negate")) {
                return -1 * toDoubleHelper(variables, list.get(0));
            } else if (name.equals("sin")) {
                return Math.sin(toDoubleHelper(variables, list.get(0)));
            } else if (name.equals("cos")) {
                return Math.cos(toDoubleHelper(variables, list.get(0)));
            } else {
                throw new EvaluationError("This operation is unknown or hasn't been implemented yet");
            }
            //return number;
        } else {
            throw new EvaluationError("This node type is unknown or hasn't been implemented yet");
        }
    }

    /**
     * Accepts a 'simplify(inner)' AstNode and returns a new node containing the simplified version
     * of the 'inner' AstNode.
     *
     * Preconditions:
     *
     * - The 'node' parameter is an operation AstNode with the name 'simplify'.
     * - The 'node' parameter has exactly one child: the AstNode to simplify
     *
     * Postconditions:
     *
     * - Returns an AstNode containing the simplified inner parameter.
     *
     * For example, if we received the AstNode corresponding to the expression
     * "simplify(3 + 4)", you would return the AstNode corresponding to the
     * number "7".
     *
     * Note: there are many possible simplifications we could implement here,
     * but you are only required to implement a single one: constant folding.
     *
     * That is, whenever you see expressions of the form "NUM + NUM", or
     * "NUM - NUM", or "NUM * NUM", simplify them.
     */
    public static AstNode handleSimplify(Environment env, AstNode node) {
        // Try writing this one on your own!
        // Hint 1: Your code will likely be structured roughly similarly
        //         to your "handleToDouble" method
        // Hint 2: When you're implementing constant folding, you may want
        //         to call your "handleToDouble" method in some way
        return handleSimplifyHelper(env.getVariables(), node.getChildren().get(0));
    }
    
    private static AstNode handleSimplifyHelper(IDictionary<String, AstNode> variables, AstNode node) {
        AstNode child = node;
        if (child.isNumber()) {
            return new AstNode(child.getNumericValue());
        } else if (child.isVariable()) {
            if (!variables.containsKey(child.getName())) {
                return child;
            } else if (variables.get(child.getName()).isNumber()) {
                return variables.get(child.getName());
            } else {
                child = handleSimplifyHelper(variables, variables.get(child.getName()));
            }
        } else {
            IList<AstNode> copy = new DoubleLinkedList<AstNode>();
            for (int i = 0; i < node.getChildren().size(); i++) {
                copy.add(node.getChildren().get(i));
            }
            child = new AstNode(node.getName(), copy);
            String name = child.getName();
            if (name.equals("+") || name.equals("-") || name.equals("*")) {
                if (child.getChildren().get(0).isNumber() && child.getChildren().get(1).isNumber()) {
                    return new AstNode(toDoubleHelper(variables, child));
                } else {
                    child.getChildren().set(0, handleSimplifyHelper(variables, child.getChildren().get(0)));
                    child.getChildren().set(1, handleSimplifyHelper(variables, child.getChildren().get(1)));
                }
            } else if (name.equals("/")) {
                child.getChildren().set(0, handleSimplifyHelper(variables, child.getChildren().get(0)));
                child.getChildren().set(1, handleSimplifyHelper(variables, child.getChildren().get(1)));
            } else {
                child.getChildren().set(0, handleSimplifyHelper(variables, child.getChildren().get(0)));
            }
        }
        return child;
    }

    
    
    /**
     * Accepts a 'plot(exprToPlot, var, varMin, varMax, step)' AstNode and
     * generates the corresponding plot. Returns some arbitrary AstNode.
     *
     * Example 1:
     *
     * >>> plot(3 * x, x, 2, 5, 0.5)
     *
     * This method will receive the AstNode corresponding to 'plot(3 * x, x, 2, 5, 0.5)'.
     * Your 'handlePlot' method is then responsible for plotting the equation
     * "3 * x", varying "x" from 2 to 5 in increments of 0.5.
     *
     * In this case, this means you'll be plotting the following points:
     *
     * [(2, 6), (2.5, 7.5), (3, 9), (3.5, 10.5), (4, 12), (4.5, 13.5), (5, 15)]
     *
     * ---
     *
     * Another example: now, we're plotting the quadratic equation "a^2 + 4a + 4"
     * from -10 to 10 in 0.01 increments. In this case, "a" is our "x" variable.
     *
     * >>> c := 4
     * 4
     * >>> step := 0.01
     * 0.01
     * >>> plot(a^2 + c*a + a, a, -10, 10, step)
     *
     * ---
     *
     * @throws EvaluationError  if any of the expressions contains an undefined variable.
     * @throws EvaluationError  if varMin > varMax
     * @throws EvaluationError  if 'var' was already defined
     * @throws EvaluationError  if 'step' is zero or negative
     */
    public static AstNode plot(Environment env, AstNode node) {
        // TODO: Your code here
        // Note: every single function we add MUST return an
        // AST node that your "simplify" function is capable of handling.
        // However, your "simplify" function doesn't really know what to do
        // with "plot" functions (and what is the "plot" function supposed to
        // evaluate to anyways?) so we'll settle for just returning an
        // arbitrary number.
        //
        // When working on this method, you should uncomment the following line:
        //return new AstNode(1);
        
//        IDictionary<String, AstNode> variables = env.getVariables();
//        //'plot(exprToPlot, var, varMin, varMax, step)'
//        AstNode function = child.get(0);
//        AstNode var = child.get(1);   
        
        IList<AstNode> child = node.getChildren(); //0:expr, 1: var, 2:min, 3:max,4:gap
        IDictionary<String, AstNode> variables = env.getVariables();
        double varMin = toDoubleHelper(variables, child.get(2));
        double varMax = toDoubleHelper(variables, child.get(3));
        double step = toDoubleHelper(variables, child.get(4));
        //think it should be this instead. if (child.get(1).isVariable()) {
        if (child.get(2).isVariable()) {
            if (!variables.containsKey(child.get(2).getName())) {
                throw new EvaluationError("Variable was already defined");
            }
        }
//        Need to fix this so that it will work. 
//        if (!variables.containsKey(node.getName())) {
//            throw new EvaluationError("This expression contains an undefined variable");
//        }
        
        if (varMin > varMax) {
            throw new EvaluationError("Min Variable is greater than Max Variable");
        } else if (step <= 0) {
            throw new EvaluationError("Step is zero or negative");
        } else {
            IList<AstNode> copy = new DoubleLinkedList<>();
            for (int i = 0; i < child.size(); i++) {
                copy.add(child.get(i));
            }
            AstNode exprToPlot = new AstNode(node.getName(), copy);
            IList<Double> xValues = new DoubleLinkedList<Double>();
            IList<Double> yValues = new DoubleLinkedList<Double>();
            for (double i = varMin; i <= varMax; i+= step) {
                xValues.add(i);
                variables.put(exprToPlot.getChildren().get(1).getName(), new AstNode(i));
                yValues.add(toDoubleHelper(variables, exprToPlot.getChildren().get(0)));
                
            }
            variables.remove(exprToPlot.getChildren().get(1).getName());
            env.getImageDrawer().drawScatterPlot("Plot", child.get(1).getName(), "output", xValues, yValues);

        }

        return new AstNode(1);
    }
}
