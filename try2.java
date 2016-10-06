
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;

/**
 * Created by linzhou on 16-10-4.
 */
public class try2 {

    public static class Expression {
        String exp;
        int order;

        Expression() {
            order = 0;
            exp = "";
        }
    }

    private static char[] readFile(String fileName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        try {
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }
            return sb.toString().toCharArray();
        } finally {
            br.close();
        }
    }

    private static int[] stackToIntArray(Stack<Integer> intRecorder) {
        /*Convert a Stack<Integer> to an int[] to represent an integer, with digits in reversed order.
        Empty the Stack. */
        int[] integer = new int[intRecorder.size()];
//        for (int i = 0; i < integer.length; i++){
//            integer[i] = intRecorder.pop();
//        }
        int i = 0;
        while (!intRecorder.isEmpty()) {
            integer[i++] = intRecorder.pop();
        }
        return integer;
    }

    private static String intArrayToNormalString(int[] x){
        for (int i = 0; i < x.length / 2; i++) {
            int temp = x[i];
            x[i] = x[x.length - 1 - i];
            x[x.length - 1 - i] = temp;
        }
        StringBuilder builder = new StringBuilder();
        for (int i : x) {
            builder.append(i);
        }
        return builder.toString();
    }

    private static int[] addition(int[] x, int[] y){

        //In form of reversed int[], add the shorter integer to the longer and return the longer.
        int[] longer = (x.length >= y.length)? x : y;
        int[] shorter = (x.length < y.length)? x : y;

        for (int i = 0; i < shorter.length; i++){
            longer[i] += shorter[i];

            // dealing with carry
            int j = i;
            while (longer[j] >= 10){
                longer[j] -= 10;
                if (j == longer.length-1) {
                    longer = Arrays.copyOf(longer, longer.length + 1);
                    longer[longer.length - 1] = 1;
                    break;
                }
                longer[j+1] += 1;
                j++;
            }
        }
        return  longer;
    }

    private static int[] multiplication(int[] x, int[] y){

        //Use each digit of the shorter to times the longer, and combine the results.
        //Do addition recursively.
        int[] longer = (x.length >= y.length)? x : y;
        int[] shorter = (x.length < y.length)? x : y;
        int[] result = new int[longer.length];
        //i loop goes through all digits of shorter from high to low order.
        for (int i = shorter.length-1; i>= 0; i--){
            // subResult stores the product of longer and the ith digit of shorter, times 10^i.
            int[] subResult = new int[longer.length+i];
            //j loop processes the multiplication.
            for (int j = 0; j < longer.length; j++){
                subResult[j+i] += shorter[i] * longer[j];
                //Deal with carry.
                if (subResult[j+i] >= 10){
                    int carry = subResult[j+i] / 10;
                    subResult[j+i] %= 10;
                    //Check if need to expand the size of subResult.
                    if (j+i+1 >= subResult.length) subResult = Arrays.copyOf(subResult,subResult.length+1);
                    subResult[j+i+1] += carry;
                }
            }
            //Sum up the sub-results to get the final product.
            result = addition(result, subResult);
        }
        return result;
    }

    private static int[] power(int[] x, int[] y) {
        int exponent = 0;
        int[] result = x;
        for (int i = 0; i < y.length; i++) exponent += y[i] * 10 ^ i;
        while (exponent >= 2){
            result = multiplication(result, x);
            exponent--;
        }
        if (exponent == 0) {
            int [] res = new int[1];
            res[0] = 1;
            result = res;
        }
        return result;
    }



    public static void main(String[] args) throws IOException {
        /*int[] intArray = {1, 2, 3, 4, 5, 6};
        int[] intArray2 = {1, 2, 3, 4, 5, 6};
        System.out.println(Arrays.toString(addition(intArray, intArray2)));*/

        char[] input = readFile("./src/input.txt"); //Get the input string and present it as a char[].
        //System.out.println(Arrays.toString(input));

        Expression expression = new Expression();
        Stack<int[]> operands = new Stack<>();
        Stack<Integer> intRecorder = new Stack<>();
        String operator = "+*^";
        int nonzeroShowed = 0;
        String result;

        for (int i = 0; i < input.length; i++) {
            //omit the leading 0s of a number
            if (input[i] == '0' && nonzeroShowed == 0 && i < input.length-1 && input[i+1] != ' ')
                continue;
            if (input[i] != '0' & nonzeroShowed == 0)
                nonzeroShowed++; //stop omitting 0s when the first non-zero digit appears
            if ((input[i] == ' ' || input[i] == '\n') && !intRecorder.empty()) {
                /*End recording when a space or break line is encountered,
                and store this integer into the stack of operands.*/
                System.out.println("before push intRecorder");
                printStack2(intRecorder);
                operands.push(stackToIntArray(intRecorder));
                System.out.println("after push intRecorder");
                printStack2(intRecorder);
                printStack(operands);
                nonzeroShowed = 0;
            }
            if (input[i] >= 48 && input[i] <= 57) intRecorder.push(input[i] - 48); //Recording the digits of an integer in form of int[].

            //When read an operator:
            if (operator.indexOf(input[i]) >= 0) {
                /* Add braces and increase the order if the order of the existing expression
                is lower than the current operator. */
                if (operator.indexOf(input[i]) > expression.order && !Objects.equals(expression.exp, "")) {
                    expression.exp = "(" + expression.exp + ")";
                    expression.order = operator.indexOf(input[i]);
                }
                //Update the existing expression.
                int[] topOperand = operands.pop();
                int[] secondTopOperand = operands.pop();
                if (!Objects.equals(expression.exp, "")) expression.exp += input[i] + intArrayToNormalString(topOperand);
                if (!Objects.equals(expression.exp, "")) {
                    expression.exp = intArrayToNormalString(secondTopOperand) + input[i] + intArrayToNormalString(topOperand);
                }
                //Do the corresponding operation and return the result to the stack of operands.
                if (input[i] == '+') {
                    //System.out.println(Arrays.toString(secondTopOperand) + " " + Arrays.toString(topOperand));
                    int[] addResult = addition(secondTopOperand, topOperand);
                    operands.push(addResult);
                }
                else if (input[i] == '*') {
                    int[] mulResult = multiplication(secondTopOperand, topOperand);
                    operands.push(mulResult);
                }
                else {
                    int[] powResult = power(secondTopOperand, topOperand);
                    operands.push(powResult);
                }
            }
            /* Output the result the an expression when two line break encountered,
            or the last char of the input reached. */
            if (i >= input.length-1|| (input[i] == '\n' && input[i - 1] == '\n')) {

                printStack(operands);
                //System.out.println("No." + i + ": " + operands.size());
                result = intArrayToNormalString(operands.pop());

                System.out.println("No." + i + ": " + result.length());
                System.out.println(expression.exp + " = " + result);
                expression.exp = "";
            }
        }
    }

    private static void printStack(Stack<int[]> stack) {
        Stack<int[]> temp = new Stack<>();
        while (!stack.isEmpty()) {
            int[] a = stack.pop();
            System.out.println(Arrays.toString(a));
            temp.push(a);
        }
        while (!temp.isEmpty()) {
            stack.push(temp.pop());
        }
    }

    private static void printStack2(Stack<Integer> stack) {
        if (stack.isEmpty()) {
            System.out.println("Empty Stack");
            return;
        }
        Stack<Integer> temp = new Stack<>();
        while (!stack.isEmpty()) {
            Integer a = stack.pop();
            System.out.println(a);
            temp.push(a);
        }
        while (!temp.isEmpty()) {
            stack.push(temp.pop());
        }
    }

}
