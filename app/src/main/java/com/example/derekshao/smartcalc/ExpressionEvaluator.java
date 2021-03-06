package com.example.derekshao.smartcalc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

/**
 * Created by Derek Shao on 2017-06-30.
 */

public class ExpressionEvaluator {

    private HashMap<String, Integer> prec = new HashMap<>();

    public ExpressionEvaluator() {

        addOperatorPrec();
    }

    private void addOperatorPrec() {

        //initialize operator precedence
        prec.put("^", 5);
        prec.put("x", 4);
        prec.put("/", 3);
        prec.put("-", 2);
        prec.put("+", 1);
    }

    private boolean isHigher(String c, String topStack) {

        //if the operator on top of stack is higher or equal than operator scanned, return true
        return prec.get(topStack) >= prec.get(c);
    }


    public ArrayList<String> infix(String infixString) {

        //remove all white spaces
        infixString = infixString.replaceAll("\\s", "");

        //split each character from string into an array
        String [] infixStringArray = infixString.split("(?!^)");

        Stack<String> stack= new Stack<String>();
        ArrayList<String> postfix = new ArrayList<String>();

        String curNumber = "";

        for (String c : infixStringArray) {

            //if character c is an operator
            if (prec.containsKey(c)) {

                //if curNumber == "0", the expression contains an operator as its first character
                if (curNumber.equals("")) {
                    curNumber = "0";
                }

                //curNumber currently contains set of numbers, add to postfix string
                postfix.add(curNumber);
                //reset curNumber string for next set of numbers after the operator
                curNumber = "";

                while (!stack.isEmpty() && isHigher(c, stack.peek())) {
                    //if the operator at top of stack is higher precedence than the new operator

                    postfix.add(stack.pop());
                }
                stack.push(c);
            }
            else {
                //c is an operand or decimal, concat with curNumber string
                curNumber = curNumber.concat(c);
            }
        }

        //add the remaining number
        if (!curNumber.isEmpty()) {
            postfix.add(curNumber);
        }

        //add the remaining operators
        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }

        return postfix;
    }

    public String postfix_evaluate(ArrayList<String> postfix) {
        //postfix evaluator

        Stack<String> post = new Stack<String>();

        for (String n : postfix) {

            if (prec.containsKey(n)) {

                String topStack = post.pop();

                double retVal = 0;

                switch(n) {
                    case "^":
                        retVal = Math.pow(Double.parseDouble(post.pop()), Double.parseDouble(topStack));
                        break;
                    case "x":
                        retVal = Double.parseDouble(post.pop()) * Double.parseDouble(topStack);
                        break;
                    case "+":
                        retVal = Double.parseDouble(post.pop()) + Double.parseDouble(topStack);
                        break;
                    case "-":
                        retVal = Double.parseDouble(post.pop()) -  Double.parseDouble(topStack);
                        break;
                    case "/":
                        retVal = Double.parseDouble(post.pop()) / Double.parseDouble(topStack);
                        break;
                    default:
                        break;
                }

                post.push(Double.toString(retVal));
            }
            else {
                //operand
                post.push(n);
            }
        }

        //final calculated value
        String finalValue = post.peek();

        //removes decimal point if .0
        if (finalValue.charAt(finalValue.length() - 1) == '0' && finalValue.charAt(finalValue.length() - 2) == '.') {
            finalValue = finalValue.substring(0, finalValue.length() - 2);
        }

        return finalValue;
    }

}
