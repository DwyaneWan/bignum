package P1_bignum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;


/**
 * Created by dwyan on 9/30/2016.
 * Lack: multiplication, power; exception handling.
 */
public class Bignum {

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
        //Convert a Stack<Integer> to an int[] to represent an integer, with digits in reversed order.
        //Empty the Stack.
        return new int[0];
    }

    private static String toNormalString(int[] x){
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
            if (longer[i] >= 10){
                longer[i] -= 10;
                if (i == longer.length-1){
                    longer = Arrays.copyOf(longer, longer.length + 1);
                    longer[longer.length - 1] = 1;
                    }
                else{
                    longer[i+1] += 1;
                }
            }
        }
        return  longer;
    }

    private static int[] multiplication(int[] x, int[] y){

        //Implement multiplication.
        int[] longer = (x.length >= y.length)? x : y;
        int[] shorter = (x.length < y.length)? x : y;
        //Use each digit of the shorter to times the longer, and combine the results.
        //Do addition recursively.
        int[] result = new int[0];
        int digit = 0;
        while (shorter != new int[shorter.length]){
            while (shorter[digit] != 0){
                shorter[digit] --;
                result = addition(result,longer);
            }

        }

        return new int[0];
    }

    private static int[] power(int[] x, int[] y) {
        return new int[0];
    }



    public static void main(String[] args) throws IOException {
        char[] input = readFile("input.txt");

        Expression expression = new Expression();
        Stack<int[]> operands = new Stack<>();
        Stack<Integer> intRecorder = new Stack<Integer>();
        String operator = "+*^";
        int nonzeroShowed = 0;
        String result;
        for (int i = 0; i < input.length; i++) {
            if (input[i] == '0' & nonzeroShowed == 0) continue; //omit the leading 0s of a number
            if (input[i] != '0' & nonzeroShowed == 0)
                nonzeroShowed++; //stop omitting 0s when the first non-zero digit appears
            if ((input[i] == ' ' | input[i] == '\n') & !intRecorder.empty()) {
                //End recording when a space or break line is encountered., and store this integer into the stack of operands.
                operands.push(stackToIntArray(intRecorder));
                nonzeroShowed = 0;
            }
            intRecorder.push(input[i] - '0'); //Recording the digits of an integer in form of int[].

            //When read an operator:
            if (operator.indexOf(input[i]) >= 0) {
                // Add braces and increase the order if the order of the existing expression is lower than the current operator.
                if (operator.indexOf(input[i]) > expression.order && !Objects.equals(expression.exp, "")) {
                    expression.exp = "(" + expression.exp + ")";
                    expression.order = operator.indexOf(input[i]);
                }
                //Update the existing expression.
                int[] lastOperand = operands.pop();
                int[] penultOperand = operands.pop();
                if (!Objects.equals(expression.exp, "")) expression.exp += input[i] + toNormalString(lastOperand);
                if (!Objects.equals(expression.exp, "")) {
                    expression.exp = toNormalString(penultOperand) + input[i] + toNormalString(lastOperand);
                }
                //Do the corresponding operation and return the result to the stack of operands.
                if (input[i] == '+') {
                    int[] mulResult = addition(lastOperand, penultOperand);
                    operands.push(mulResult);
                }
                else if (input[i] == '*') {
                    int[] mulResult = multiplication(lastOperand, penultOperand);
                    operands.push(mulResult);
                }
                else {
                    int[] powResult = power(lastOperand, penultOperand);
                    operands.push(powResult);
                }
            }


            if (input[i] == '\n' & input[i - 1] == '\n') {
                result = toNormalString(operands.pop());
                System.out.println(expression.exp + " = " + result);
                expression.exp = "";
            }
        }
        System.out.println(toNormalString(new int[]{3, 2, 1}));
        System.out.println(input);
        System.out.println(input[8]);
        System.out.println(Arrays.toString(addition(new int[]{5, 6, 7}, new int[]{9, 8, 7})));

    }

}
