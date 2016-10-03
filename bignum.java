package P1_bignum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Stack;


/**
 * Created by dwyan on 9/30/2016.
 */
public class bignum {

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

    private static int[] Addition(int[] x, int[] y){

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

    private static int[] Multi(int[] x, int[] y){

        //Implement multiplication.
        for (int i = 0; i < x.length; i++){
            for (int j = 0; j < y.length; j++){
                y[j] *= x[i]*Math.pow(10,i);
            }
        }
        return new int[0];
    }

    private static int[] Power(int[] x, int[] y) {
        return new int[0];
    }



    public static void main(String[] args) throws IOException {
        char[] input = readFile("input.txt");

        //Iterate through the input expression, store operands into a stack with extra 0s trimmed off.
        //Do operation when an operation mark is encountered.
        String expression = "";
        Stack<int[]> operands = new Stack<>();
        Stack<Integer> intRecorder = new Stack<Integer>();
        int nonzeroShowed = 0;
        String result = "";
        for (int i = 0; i < input.length; i++) {
            if (input[i] == '0' & nonzeroShowed == 0) continue;
            if (input[i] != '0' & nonzeroShowed == 0) nonzeroShowed++;
            if ((input[i] == ' ' | input[i] == '\n') & !intRecorder.empty()) {
                operands.push(stackToIntArray(intRecorder));
                nonzeroShowed = 0;
            }
            intRecorder.push(input[i] - '0');
            if (input[i] == '+') {
                int[] addResult = Addition(operands.pop(), operands.pop());
                operands.push(addResult);
            }
            if (input[i] == '*') {
                int[] mulResult = Multi(operands.pop(), operands.pop());
                operands.push(mulResult);
            }
            if (input[i] == '^') {
                int[] powResult = Power(operands.pop(), operands.pop());
                operands.push(powResult);
            }
            if (input[i] == '\n' & input[i - 1] == '\n') {
                result = toNormalString(operands.pop());
                System.out.println(expression + " = " + result);
                expression = "";
            }
        }
        System.out.println(toNormalString(new int[]{3, 2, 1}));
        System.out.println(input);
        System.out.println(input[8]);
        System.out.println(Arrays.toString(Addition(new int[]{5, 6, 7}, new int[]{9, 8, 7})));

    }

}
