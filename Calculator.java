import java.util.Scanner;

// Stack having Character as Data type
class Stack1 {
    int top;
    char[] a;

    public Stack1(String s) {
        top = -1;
        a = new char[s.length()];
    }

    void push(char ch) {
        top++;
        a[top] = ch;
    }

    char pop() {
        if (top == -1) {
            System.out.println("Stack is Empty!!!");
            return 0;
        } else {
            top--;
            return a[top + 1];
        }
    }

    char peek() {
        return a[top];
    }
}

// Stack having String as Data type
class Stack2 {
    int top;
    String[] a;
    public Stack2(String s) {
        top = -1;
        a = new String[s.length()];
    }

    void push(String ch) {
        top++;
        a[top] = ch;
    }

    String pop() {
        if (top == -1) {
            System.out.println("Stack is Empty!!!");
            return null;
        }
        else {
            top--;
            return a[top+1];
        }
    }
}

class Converter {
    Scanner sc = new Scanner(System.in);

    // To get priority of operators
    int getPriority(char ch) {
        if (ch == '+')
            return 1;
        else if (ch == '-')
            return 1;
        else if (ch == '*')
            return 2;
        else if (ch == '/')
            return 2;
        else if (ch == '^')
            return 3;
        else if (ch == '$')
            return 3;
        return 0;
    }

    String infixToPrefix(String infix) {
        infix = reverseString(infix); // Reversing given expression as per algorithm
        String prefix = ""; // To store Prefix expression
        Stack1 st = new Stack1(infix); // Creating stack with size of given expression
        char c[] = infix.toCharArray(); // Creating char array of given expression

        // Ex. 10 + 1
        for (int i = 0; i < c.length; i++) {
            char ch = c[i];
            // If ch is Letter or Digit
            if (Character.isLetter(ch) || Character.isDigit(ch)) {
                // Pointer i is pointing at ch='1' so it's added to the prefix var and loop run till i points to operator or brackets or space
                while (i < c.length && getPriority(c[i]) == 0 && c[i] != '(' && c[i] != ')' && c[i] != ' ') {
                    prefix += c[i];
                    i++;
                }
                prefix += " ";
                i--; // Pointer decrease by one because for loop increases it
            }
            // If ch is closing bracket then it is pushed into stack
            else if (ch == ')') {
                st.push(ch);
            }
            // If ch is opening bracket then elemnts are poped from stack till closing bracket and added to the prefix var
            else if (ch == '(') {
                char x = st.pop();
                while (x != ')' ) {
                    prefix += x + " ";
                    x = st.pop();
                }
            } else if (getPriority(ch) > 0) {
                //if (st.top == -1 || st.a[st.top] == -1)
                if (st.top == -1) {
                    st.push(ch);
                } else {
                    while (st.top != -1 && (getPriority(ch) < getPriority(st.a[st.top]))) {
                        prefix += st.pop() + " ";
                    }
                    st.push(ch);
                }
            }
        }
        while (st.top != 0) {
            prefix += st.pop() + " ";
        }
        prefix += st.pop();
        prefix = reverseString(prefix);
        return prefix;
    }

    boolean checkExpression(String expression,int x) {
        int count = 0;
        int countOp = 0;
        char c[] = expression.toCharArray();

        // Ex. 10 + 1
        for (int i = 0; i < c.length; i++) {
            char ch = c[i];
            // Counts the operands in given expression
            if (Character.isLetter(ch) || Character.isDigit(ch)) {
                // Poiter i increment till the next element is space or operator or bracket to consider 10 as single digit
                while (i < c.length && getPriority(c[i]) == 0 && c[i] != '(' && c[i] != ')' && c[i] != ' ') {
                    i++;
                }
                count++;
                // Pointer i is next to 10 so it should be decrease by one because i is going to increment in for loop
                i--;
            }
            // Counts the operators in given expression
            else if (getPriority(ch) > 0) {
                countOp++;
            }
        }

        if (x == 1 && (getPriority(c[0]) > 0 || getPriority(c[c.length-1]) > 0)) {
            return false;
        } else if (x == 2 && (getPriority(c[0]) <= 0 || getPriority(c[c.length - 1]) != 0)) {
            return false;
        } else if (x == 3 && (getPriority(c[0]) != 0 || getPriority(c[c.length - 1]) <= 0)) {
            return false;
        } else {
            if (countOp == 0) {
                return false;
            } else {
                // If count of operators is less by one than the count of operands
                if ((count - countOp) == 1) {
                // If parenthisis are balanced
                    if (balancedParenthisis(expression)) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
    }

    String infixToPostfix(String infix) {
        String postfix = "";
        Stack1 st = new Stack1(infix);
        char c[] = infix.toCharArray();

        for (int i = 0; i < c.length; i++) {
            char ch = c[i];
            if (Character.isLetter(ch) || Character.isDigit(ch)) {
                while (i < c.length && getPriority(c[i]) == 0 && c[i] != '(' && c[i] != ')' && c[i] != ' ') {
                    postfix += c[i];
                    i++;
                }
                postfix += " ";
                i--;
            } else if (ch == '(') {
                st.push(ch);
            } else if (ch == ')') {
                char x = st.pop();
                while (x != '(') {
                    postfix += x + " ";
                    x = st.pop();
                }
            } else if (getPriority(ch) > 0) {
                if (st.top == -1 || st.a[st.top] == '(') {
                    st.push(ch);
                } else {
                    while (st.top != -1 && (getPriority(ch) <= getPriority(st.a[st.top]))) {
                        postfix += st.pop() + " ";
                    }
                    st.push(ch);
                }
            }
        }
        while (st.top != -1) {
            postfix += st.pop() + " ";
        }
        return postfix;
    }

    String prefixToInfix(String prefix) {
        String infix = "";
        Stack2 st = new Stack2(prefix);

        // It reverses String but didn't reverse double digit, Ex. 10+1*14 to 14*1+10 not 41*1+01
        String s[] = prefix.split(" ");
        char[] c = new char[prefix.length()];
        int n = 0;
        for (int i = s.length - 1; i >= 0; i--) {
            for (int j = 0; j < s[i].length(); j++) {
                c[n] = s[i].charAt(j);
                n++;
            }
            // add space between operands and operators
            if (i != 0) {
                c[n] = ' ';
                n++;
            }
        }
        //

        for (int i = 0; i < c.length; i++) {
            String temp = "";
            char ch = c[i];
            if (Character.isLetter(ch) || Character.isDigit(ch)) {
                while (i < c.length && getPriority(c[i]) == 0 && c[i] != ' ') {
                    temp += c[i];
                    i++;
                }
                i--;
                st.push(temp);
            } else if (getPriority(ch) > 0) {
                String a = st.pop();
                String b = st.pop();
                if (i == c.length - 1) {
                    temp = a + ch + b;
                } else {
                    temp = "(" + a + ch + b + ")";
                }
                st.push(temp);
            }
        }
        infix = st.pop();
        return infix;
    }

    String postfixToInfix(String postfix) {
        String infix = "";
        Stack2 st = new Stack2(postfix);
        char c[] = postfix.toCharArray();
        for (int i = 0; i < c.length; i++) {
            String temp = "";
            char ch = c[i];
            if (Character.isLetter(ch) || Character.isDigit(ch)) {
                while (i < c.length && getPriority(c[i]) == 0 && c[i] != ' ') {
                    temp += c[i];
                    i++;
                }
                i--;
                st.push(temp);
            } else if (getPriority(ch) > 0) {
                String a = st.pop();
                String b = st.pop();
                if (i == c.length - 1) {
                    temp = b + ch + a;
                } else {
                    temp = "(" + b + ch + a + ")";
                }
                st.push(temp);
            }
        }
        infix = st.pop();
        return infix;
    }

    String prefixToPostfix(String prefix) {
        String infix = prefixToInfix(prefix);
        String postfix = infixToPostfix(infix);
        return postfix;
    }

    String postfixToPrefix(String postfix) {
        String infix = postfixToInfix(postfix);
        String prefix = infixToPrefix(infix);
        return prefix;
    }

    double infixEvaluate(String infix) {
        infix = infixToPostfix(infix);
        double ans = postfixEvaluate(infix);
        return ans;
    }

    double prefixEvaluate(String prefix) {
        Stack2 st = new Stack2(prefix);

        String s[] = prefix.split(" ");
        char c[] = new char[prefix.length()];

        int n = 0;
        for (int i = s.length - 1; i >= 0; i--) {
            for (int j = 0; j < s[i].length(); j++) {
                c[n] = s[i].charAt(j);
                n++;
            }
            if (i != 0) {
                c[n] = ' ';
                n++;
            }
        }

        for (int i = 0; i < c.length; i++) {
            String temp = "";
            char ch = c[i];
            if (Character.isDigit(ch)) {
                while (c[i] != ' ') {
                    temp += c[i];
                    i++;
                }
                i--;
                st.push(temp);
            } else if (getPriority(ch) > 0) {
                double a = Double.parseDouble(st.pop());
                double b = Double.parseDouble(st.pop());
                double ans = 0.0;
                if (ch == '+')
                    ans = a + b;
                if (ch == '-')
                    ans = a - b;
                if (ch == '*')
                    ans = a * b;
                if (ch == '/')
                    ans = a / b;
                if (ch == '^' || ch == '$')
                    ans = Math.pow(a, b);

                temp += ans;
                st.push(temp);
            }
        }
        double ans1 = Double.parseDouble(st.pop());
        return ans1;
    }

    double postfixEvaluate(String postfix) {
        Stack2 st = new Stack2(postfix);
        char c[] = postfix.toCharArray();
        for (int i = 0; i < c.length; i++) {
            String temp = "";
            char ch = c[i];
            if (Character.isDigit(ch)) {
                while (c[i] != ' ') {
                    temp += c[i];
                    i++;
                }
                i--;
                st.push(temp);
            } else if (getPriority(ch) > 0) {
                double a = Double.parseDouble(st.pop());
                double b = Double.parseDouble(st.pop());
                double ans = 0.0;
                if (ch == '+')
                    ans = b + a;
                if (ch == '-')
                    ans = b - a;
                if (ch == '*')
                    ans = b * a;
                if (ch == '/')
                    ans = b / a;
                if (ch == '^' || ch == '$')
                    ans = Math.pow(b, a);

                temp += ans;
                st.push(temp);
            }
        }
        double ans2 = Double.parseDouble(st.pop());
        return ans2;
    }

    // To Reverse a Strinh
    String reverseString(String s) {
        String reversedString = "";
        Stack1 st = new Stack1(s);
        //Push every character of given String one by one in the Stack
        for (int i = 0; i < s.length(); i++) {
            st.push(s.charAt(i));
        }

        //Pop every element from Stack and add it to reversedString variable
        for (int j = 0; j < s.length(); j++) {
            reversedString += st.pop();
        }
        return reversedString;
    }

    // To Check if Parenthisis are Balanced or not
    boolean balancedParenthisis(String s) {
        if (s == null) {
            System.out.println("\u001B[38;5;196mEnter valid String!!!");
            return false;
        } else {
            Stack1 st = new Stack1(s);
            boolean flag = true;
            for (int i = 0; i < s.length(); i++) {
                char ch = s.charAt(i);

                // If ch is opening bracket then push it to the Stack
                if (ch == '(' || ch == '{' || ch == '[') {
                    st.push(ch);
                }

                // If ch is closing bracket
                else if (ch == ')' || ch == '}' || ch == ']') {
                    // If Stack is not empty and same opening bracket as the closing bracket is on top of the stack then pop it from Stack
                    if (st.top != -1 && ((ch == ')' && st.peek() == '(') || (ch == '}' && st.peek() == '{')
                            || (ch == ']' && st.peek() == '['))) {
                        st.pop();
                        flag = true;
                    } else {
                        flag = false;
                        break;
                    }
                }
            }
            
            // After every brackets are scanned If Stack is empty then entered parenthisis are balanced
            if (st.top == -1 && flag) {
                return true;
            } else {
                return false;
            }
        }

    }

    // To Reverse Individual Words From Given String
    public String reverseStringI(String s) {
        String s1[] = s.split(" ");
        String reversed = "";

        for (int i = 0; i < s1.length; i++) {
            reversed += reverseString(s1[i]) + " ";
        }
        return reversed;
    }

}

// To Delay Output Using Thread.sleep()
class ThreadDelay{
    void delay(long milli) throws InterruptedException{
        try {
            Thread.sleep(milli);
        } catch (InterruptedException e) {
            if (Thread.interrupted())
                throw new InterruptedException();
        }
    }
}

public class Calculator {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Converter obj = new Converter();
        ThreadDelay t = new ThreadDelay();

        int i;
        int flag = 0;
        while (flag != 1) {
            System.out.println("\n\u001B[38;5;46m-: Functions :-");
            t.delay(500);
            System.out.println("\u001B[38;5;76m1. To Convert Infix Expression to Prefix Expression.");
            System.out.println("\u001B[38;5;77m2. To Convert Infix Expression to Postfix Expression.");
            System.out.println("\u001B[38;5;78m3. To Convert Prefix Expression to Infix Expression.");
            System.out.println("\u001B[38;5;79m4. To Convert Postfix Expression to Infix Expression.");
            System.out.println("\u001B[38;5;80m5. To Convert Prefix Expression to Postfix Expression.");
            System.out.println("\u001B[38;5;81m6. To Convert Postfix Expression to Prefix Expression.");
            System.out.println("\u001B[38;5;153m7. To Evaluate Infix Expression.");
            System.out.println("\u001B[38;5;152m8. To Evaluate Prefix Expression.");
            System.out.println("\u001B[38;5;151m9. To Evaluate Postfix Expression.");
            System.out.println("\u001B[38;5;150m10. To Check if given expression is valid or not.");
            System.out.println("\u001B[38;5;149m11. To Reverse a String.");
            System.out.println("\u001B[38;5;148m12. To Reverse Individual word from given String.");
            System.out.println("\u001B[38;5;184m13. To Check if given string is palindrome or not.");
            System.out.println("\u001B[38;5;220m14. To Check if parenthisis are balanced or not.");
            System.out.println("\u001B[38;5;214m15. To Exit.");
            
            System.out.print("\n\u001B[38;5;172mEnter Choice : ");
            int ch;
            try {
                ch = sc.nextInt();
            } catch (Exception e) {
                ch = 0;
            }
            sc.nextLine();
            System.out.print("\033[H\033[2J");
            System.out.flush();
            t.delay(200);

            switch (ch) {
                case 1:
                    System.out.print("\u001B[38;5;50mEnter Infix Expression : ");
                    String infix1 = sc.nextLine();

                    if (obj.checkExpression(infix1, 1)) {
                        t.delay(200);

                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(250);

                        System.out.print("\u001B[38;5;46mConverting");
                        t.delay(700);
                        for (i = 0; i < 3; i++) {
                            System.out.print(".");
                            t.delay(650);
                        }
                        t.delay(600);
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(300);

                        String ans1 = obj.infixToPrefix(infix1);
                        System.out.println("\u001B[38;5;87mInfix Expression : " + infix1);
                        System.out.println("\u001B[38;5;87mPrefix Expression : " + ans1);
                        t.delay(1500);
                    } else {
                        System.out.println("\n\u001B[38;5;196mGiven Expression is Invalid!");
                        t.delay(1000);
                    }
                    break;

                case 2:
                    System.out.print("\u001B[38;5;50mEnter Infix Expression : ");
                    String infix2 = sc.nextLine();
                    if (obj.checkExpression(infix2, 1)) {
                        t.delay(200);

                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(250);

                        System.out.print("\u001B[38;5;46mConverting");
                        t.delay(700);
                        for (i = 0; i < 3; i++) {
                            System.out.print(".");
                            t.delay(650);
                        }
                        t.delay(600);
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(300);

                        String ans2 = obj.infixToPostfix(infix2);
                        System.out.println("\u001B[38;5;87mInfix Expression : " + infix2);
                        System.out.println("\u001B[38;5;87mPostfix Expression : " + ans2);
                        t.delay(1500);
                    } else {
                        System.out.println("\n\u001B[38;5;196mGiven Expression is Invalid!");
                        t.delay(1000);
                    }
                    break;
                case 3:
                    System.out.println("[Enter spaces between operators and operands ex. - + / 10 2 * 4 5 15]");
                    System.out.print("\u001B[38;5;50mEnter Prefix Expression : ");
                    String prefix3 = sc.nextLine();
                    if (obj.checkExpression(prefix3, 2)) {
                        t.delay(200);

                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(250);

                        System.out.print("\u001B[38;5;46mConverting");
                        t.delay(700);
                        for (i = 0; i < 3; i++) {
                            System.out.print(".");
                            t.delay(650);
                        }
                        t.delay(600);
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(300);

                        String ans3 = obj.prefixToInfix(prefix3);
                        System.out.println("\u001B[38;5;87mPrefix Expression : " + prefix3);
                        System.out.println("\u001B[38;5;87mInfix Expression : " + ans3);
                        t.delay(1500);
                    } else {
                        System.out.println("\n\u001B[38;5;196mGiven Expression is Invalid!");
                        t.delay(1000);
                    }
                    break;
                case 4:
                    System.out.println("[Enter spaces between operators and operands ex. 10 2 / 4 5 * + 15 -]");
                    System.out.print("\u001B[38;5;50mEnter Postfix Expression : ");
                    String postfix4 = sc.nextLine();
                    if (obj.checkExpression(postfix4, 3)) {
                        t.delay(200);

                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(250);

                        System.out.print("\u001B[38;5;46mConverting");
                        t.delay(700);
                        for (i = 0; i < 3; i++) {
                            System.out.print(".");
                            t.delay(650);
                        }
                        t.delay(600);
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(300);

                        String ans4 = obj.postfixToInfix(postfix4);
                        System.out.println("\u001B[38;5;87mPostfix Expression : " + postfix4);
                        System.out.println("\u001B[38;5;87mInfix Expression : " + ans4);
                        t.delay(1500);
                    } else {
                        System.out.println("\n\u001B[38;5;196mGiven Expression is Invalid!");
                        t.delay(1000);
                    }
                    break;
                case 5:
                    System.out.println("[Enter spaces between operators and operands ex. - + / 10 2 * 4 5 15]");
                    System.out.print("\u001B[38;5;50mEnter Prefix Expression : ");
                    String prefix5 = sc.nextLine();
                    if (obj.checkExpression(prefix5, 2)) {
                        t.delay(200);

                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(250);

                        System.out.print("\u001B[38;5;46mConverting");
                        t.delay(700);
                        for (i = 0; i < 3; i++) {
                            System.out.print(".");
                            t.delay(650);
                        }
                        t.delay(600);
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(300);

                        String ans5 = obj.prefixToPostfix(prefix5);
                        System.out.println("\u001B[38;5;87mPrefix Expression : " + prefix5);
                        System.out.println("\u001B[38;5;87mPostfix Expression : " + ans5);
                        t.delay(1500);
                    } else {
                        System.out.println("\n\u001B[38;5;196mGiven Expression is Invalid!");
                        t.delay(1000);
                    }
                    break;
                case 6:
                    System.out.println("[Enter spaces between operators and operands ex. 10 2 / 4 5 * + 15 -]");
                    System.out.print("\u001B[38;5;50mEnter Postfix Expression : ");
                    String postfix6 = sc.nextLine();
                    if (obj.checkExpression(postfix6, 3)) {
                        t.delay(200);

                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(250);

                        System.out.print("\u001B[38;5;46mConverting");
                        t.delay(700);
                        for (i = 0; i < 3; i++) {
                            System.out.print(".");
                            t.delay(650);
                        }
                        t.delay(600);
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(300);

                        String ans6 = obj.postfixToPrefix(postfix6);
                        System.out.println("\u001B[38;5;87mPostfix Expression : " + postfix6);
                        System.out.println("\u001B[38;5;87mPrefix Expression : " + ans6);
                        t.delay(1500);
                    } else {
                        System.out.println("\n\u001B[38;5;196mGiven Expression is Invalid!");
                        t.delay(1000);
                    }
                    break;

                case 7:
                    System.out.print("\u001B[38;5;50mEnter Infix Expression : ");
                    String infix7 = sc.nextLine();

                    if (obj.checkExpression(infix7, 1)) {
                        t.delay(200);

                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(250);

                        System.out.print("\u001B[38;5;46mEvaluating");
                        t.delay(700);
                        for (i = 0; i < 3; i++) {
                            System.out.print(".");
                            t.delay(650);
                        }
                        t.delay(600);
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(300);

                        double ans7 = obj.infixEvaluate(infix7);
                        System.out.println("\u001B[38;5;87mExpression : " + infix7);
                        System.out.println("\u001B[38;5;87mAnswer : " + ans7);
                        t.delay(1500);
                    } else {
                        System.out.println("\n\u001B[38;5;196mGiven Expression is Invalid!");
                        t.delay(1000);
                    }
                    break;

                case 8:
                    System.out.println("[Enter spaces between operators and operands ex. - + / 10 2 * 4 5 15]");
                    System.out.print("\u001B[38;5;50mEnter Prefix Expression : ");
                    String prefix8 = sc.nextLine();
                    if (obj.checkExpression(prefix8, 2)) {
                        t.delay(200);

                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(250);

                        System.out.print("\u001B[38;5;46mEvaluating");
                        t.delay(700);
                        for (i = 0; i < 3; i++) {
                            System.out.print(".");
                            t.delay(650);
                        }
                        t.delay(600);
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(300);

                        double ans8 = obj.prefixEvaluate(prefix8);
                        System.out.println("\u001B[38;5;87mExpression : " + prefix8);
                        System.out.println("\u001B[38;5;87mAnswer : " + ans8);
                        t.delay(1500);
                    } else {
                        System.out.println("\n\u001B[38;5;196mGiven Expression is Invalid!");
                        t.delay(1000);
                    }
                    break;

                case 9:
                    System.out.println("[Enter spaces between operators and operands ex. 10 2 / 4 5 * + 15 -]");
                    System.out.print("\u001B[38;5;50mEnter Postfix Expression : ");
                    String postfix9 = sc.nextLine();
                    if (obj.checkExpression(postfix9, 3)) {
                        t.delay(200);

                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(250);

                        System.out.print("\u001B[38;5;46mEvaluating");
                        t.delay(700);
                        for (i = 0; i < 3; i++) {
                            System.out.print(".");
                            t.delay(650);
                        }
                        t.delay(600);
                        System.out.print("\033[H\033[2J");
                        System.out.flush();
                        t.delay(300);

                        double ans9 = obj.postfixEvaluate(postfix9);
                        System.out.println("\u001B[38;5;87mExpression : " + postfix9);
                        System.out.println("\u001B[38;5;87mAnswer : " + ans9);
                        t.delay(1500);
                    } else {
                        System.out.println("\n\u001B[38;5;196mGiven Expression is Invalid!");
                        t.delay(1000);
                    }
                    break;

                case 10:
                    System.out.print("\u001B[38;5;50mEnter Expression : ");
                    String expression = sc.nextLine();
                    t.delay(200);

                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    t.delay(250);

                    System.out.print("\u001B[38;5;46mValidating");
                    t.delay(700);
                    for (i = 0; i < 3; i++) {
                        System.out.print(".");
                        t.delay(650);
                    }
                    t.delay(600);
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    t.delay(300);

                    if (obj.checkExpression(expression, 0)) {
                        System.out.println("\u001B[38;5;87mGiven Expression is Valid \u221A");
                        t.delay(1500);
                    } else {
                        System.out.println("\n\u001B[38;5;196mGiven Expression is Invalid!");
                        t.delay(1500);
                    }
                    break;

                case 11:
                    System.out.print("\u001B[38;5;50mEnter a String : ");
                    String s = sc.nextLine();
                    t.delay(200);

                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    t.delay(250);

                    System.out.print("\u001B[38;5;46mReversing");
                    t.delay(700);
                    for (i = 0; i < 3; i++) {
                        System.out.print(".");
                        t.delay(650);
                    }
                    t.delay(600);
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    t.delay(300);

                    String reversed1 = obj.reverseString(s);
                    System.out.println("\u001B[38;5;87mEntered String : " + s);
                    System.out.println("\u001B[38;5;87mReversed String : " + reversed1);
                    t.delay(1500);
                    break;

                case 12:
                    System.out.print("\u001B[38;5;50mEnter a String : ");
                    String s3 = sc.nextLine();

                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    t.delay(250);

                    System.out.print("\u001B[38;5;46mReversing");
                    t.delay(700);
                    for (i = 0; i < 3; i++) {
                        System.out.print(".");
                        t.delay(650);
                    }
                    t.delay(600);
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    t.delay(300);

                    String reversed3 = obj.reverseStringI(s3);
                    System.out.println("\u001B[38;5;87mEntered String : " + s3);
                    System.out.println("\u001B[38;5;87mReversed String : " + reversed3);
                    t.delay(1500);
                    break;

                case 13:
                    System.out.print("\u001B[38;5;50mEnter a String : ");
                    String s1 = sc.nextLine();
                    t.delay(200);

                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    t.delay(250);

                    System.out.print("\u001B[38;5;46mChecking");
                    t.delay(700);
                    for (i = 0; i < 3; i++) {
                        System.out.print(".");
                        t.delay(650);
                    }
                    t.delay(600);
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    t.delay(300);

                    String reversed2 = obj.reverseString(s1);
                    if (s1.equals(reversed2)) {
                        System.out.println("\u001B[38;5;87mGiven String is Palindrome \u221A");
                        t.delay(1500);
                    } else {
                        System.out.println("\u001B[38;5;196mGiven String is Not Palindrome!");
                        t.delay(1000);
                    }
                    break;

                case 14:
                    System.out.print("\u001B[38;5;50mEnter Parenthisis : ");
                    String s2 = sc.nextLine();
                    t.delay(200);

                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    t.delay(250);

                    System.out.print("\u001B[38;5;46mChecking");
                    t.delay(700);
                    for (i = 0; i < 3; i++) {
                        System.out.print(".");
                        t.delay(650);
                    }
                    t.delay(600);
                    System.out.print("\033[H\033[2J");
                    System.out.flush();
                    t.delay(300);

                    boolean b = obj.balancedParenthisis(s2);
                    if (b) {
                        System.out.println("\u001B[38;5;87mParenthisis are balanced \u221A");
                    } else {
                        System.out.println("\u001B[38;5;196mParenthisis are not balanced!");
                    }
                    t.delay(1500);
                    break;

                case 15:
                    t.delay(300);
                    System.out.println("\u001B[38;5;184mThank You!");
                    flag = 1;
                    break;

                default:
                    t.delay(250);
                    System.out.println("\u001B[38;5;196mInvalid Choice!!!");
                    t.delay(1200);
                    break;
            }
        }
        sc.close();
    }
}

/*
Infix : 56 * 124 - 37 / 82 + 150 $ 18 ^ 45 * 103 - 29 / 74 + 111 * 81 / 30
Prefix : + - + - * 56 124 / 37 82 * ^ $ 150 18 45 103 / 29 74 / * 111 81 30
Postfix : 56 124 * 37 82 / - 150 18 $ 45 ^ 103 * + 29 74 / - 111 81 * 30 / +
Ans = Infinity
*/

/*
Infix : (10+2)-(((3*8)/(64^25))*(2$15))
Prefix : - + 10 2 * / * 3 8 ^ 64 25 $ 2 15
Postfix : 10 2 + 3 8 * 64 25 ^ / 2 15 $ * -
Ans = 12
*/

/*
Infix : 10/2+4*5-15
Prefix : - + / 10 2 * 4 5 15
Postfix : 10 2 / 4 5 * + 15 -
Ans = 10
*/

/*
Infix : (A+B)-(10+20*(C/D)$4)
Prefix : - + A B + 10 * 20 $ / C D 4
Postfix : A B + 10 20 C D / 4 $ * + -
*/

/*
Infix : (A + (B * C)) - (D / (E + F))
Prefix : - + A * B C / D + E F
Postfix : A B C * + D E F + / -
*/

/*
Infix : (21 * 9 + (18 - 11)) / (35 + (14 - 9)) - 12
Prefix : - / + * 21 9 - 18 11 + 35 - 14 9 12
Postfix : 21 9 * 18 11 - + 35 14 9 - + / 12 -
Ans = -7.1
*/

/*
Infix : ((18 - 13) * (22 + 11)) / (28 + 19) - 16
Prefix : - / * - 18 13 + 22 11 + 28 19 16
Postfix : 18 13 - 22 11 + * 28 19 + / 16 -
Ans = -12.48936170212766
*/