package rpn;

import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

public class RPN {
	static Scanner input = new Scanner(System.in);

	public static void main(String[] args) {
		System.out.println("Welcome to the RPN Calculator!");
		while(true){
			System.out.println("Please input a math formula, or press (q) to quit:");
			String formula = input.nextLine();
			if (formula.equals("q")){
				return;
			}
			else{
				console(formula);
			}
			
		}
	}
	
	public static void console(String formula){
		try{
			String translation = translate(formula);
			//System.out.println(translation);
			double solution = calculator(translation);
			System.out.println(translation);
			System.out.println(solution);
		}
		catch (Exception e){
			System.out.println("Exception thrown  :" + e);
			System.out.println("Not a valid equation!");
		}
	}
	
	public static String translate(String formula) throws OperandException{
		String translation = "";
		Stack<String> list = new Stack<String>();
		StringTokenizer ops = new StringTokenizer(formula,"()+-/*^ ",true);
		String op = null;
		int parenthesis = 0;
	    while (ops.hasMoreTokens()) {
	        op = ops.nextToken();
	        if (!op.equals(" ")) {
	        	if (precedence(op) == 0){
	        		if (op.equals("(")){
	        			list.push(op);
	        			parenthesis++;
	        		}
	        		else if (op.equals(")")){
	        			while (!list.empty() && !(list.peek().equals("("))){
	        				translation += list.pop() + " ";
	        			}
	        			list.pop();
	        			parenthesis--;
	        		}
	        		else{
	        			translation += op + " ";
	        		}
	        	}
	        	else{
	        		while (!list.empty() && precedence(op) <= precedence(list.peek())){
	        			translation += list.pop() + " ";
	        		}
	        		list.push(op); //push when precedence(list.peek()) < precedence(op)
	        	}
	        }
		}
		while(!list.empty()){
			if (!list.peek().equals("(")){
				translation += list.pop() + " ";
			}
			else{
				list.pop();
			}
		}
		if (parenthesis != 0){
			throw new OperandException("Dissatisfied parentheses!");
		}
		return translation;
		
	}
	
	public static int precedence(String operator){
		switch (operator) {
		case "+":
		case "-":
			return 1;
		case "*":
		case "/":
			return 2;
		case "^":
			return 3;
		default:
			return 0; //parenthesis will be addressed
		}
	}
	
	public static double calculator(String formula) throws OperandException{
		String[] ops = formula.split("\\s+");
		Stack<Double> list = new Stack<Double>();
		double arg2 = 0;

		for (String op: ops){
			switch (op){
			case "+":
				list.push(list.pop()+list.pop());
				break;
			case "-":
				arg2 = list.pop();
				list.push(list.pop() - arg2);
				break;
			case "*":
				list.push(list.pop() * list.pop());
				break;
			case "/":
				arg2 = list.pop();
				list.push(list.pop() / arg2);
				break;
			case "^":
				double power = list.pop();
				list.push(Math.pow(list.pop(), power));
				break;
			default:
				int number = Integer.parseInt(op);
				if ((number < 0) || (number > 99)){
					throw new OperandException("Not an integer within bounds");
				}
				list.push((double) number);
				break;
			}
		}
		
		if (list.size() != 1){
			throw new OperandException("Too many operands");
		}
		else{
			return list.pop();
		}
		
	}

}

