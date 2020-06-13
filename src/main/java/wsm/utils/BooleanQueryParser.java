package wsm.utils;

import wsm.engine.auxiliaryIndex.IndexConsts;
import wsm.exception.QueryFormatException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BooleanQueryParser {

    /**
     * convert a mid eqn string to rear eqn (inverse Poland equation)
     * @param queryString query string, of mid eqn
     * @return List<String>, containing the sequence of rear eqn
     */
    public static List<String> convertMidEqnToRearEqn(String queryString) {

        if (queryString == null || queryString.length() == 0) {
            System.out.println("Cannot parse null or empty string");
            throw new QueryFormatException(0, "Cannot parse null or empty string");
        }

        // the stack for operations
        Stack<Character> stack = new Stack<>();
        // the inverse Poland query String
        List<String> feedback = new ArrayList<>();

        int index = 0;
        while (index < queryString.length()) {
            Character current = queryString.charAt(index);
            if (current == '('){
                // found "("
                stack.push('(');
                index += 1;
            } else if (current == ')'){
                // found ")"
                Character c;
                while (true){
                    // pop until a "("
                    c = stack.pop();
                    if (c == '('){
                        break;
                    } else {
                        feedback.add(c + "");
                    }
                    if (stack.empty()){
                        System.out.println("Query String Error, does not match ( and )");
                        throw new QueryFormatException(1, "Query String Error, does not match ( and )");
                    }
                }
                index += 1;
            } else if (opPriority(current) > 0){
                // found an OP
                if (stack.empty()) {
                    stack.push(current);
                } else {
                    while (true) {
                        if (stack.empty()){
                            stack.push(current);
                            break;
                        }
                        Character lastOp = stack.pop();
                        // keep an ascending sequence in terms of priority for operations
                        if (lastOp == '(' || opPriority(current) > opPriority(lastOp)){
                            stack.push(lastOp);
                            stack.push(current);
                            break;
                        } else {
                            feedback.add("" + lastOp);
                        }
                    }
                }
                index += 1;
            } else {
                // found other characters
                int startIndex = index;
                while (index < queryString.length()){
                    current = queryString.charAt(index);
                    if (opPriority(current) == 0) {
                        index += 1;
                    } else {
                        break;
                    }
                }
                // don't forget to trim the string
                feedback.add(queryString.substring(startIndex, index).trim());
            }

        }
        while (!stack.empty()){
            Character c = stack.pop();
            feedback.add(c + "");
        }
        return feedback;
    }

    /**
     * get priority of operation
     * @param c the op character
     * @return priority
     */
    public static int opPriority(char c){
        switch (c) {
            case '|':
                return 4;
            case '&':
                return 3;
            case '\\':
            case '^':
                return 2;
            case '(':
            case ')':
                return -1;
            default:
                return 0;
        }
    }

    /**
     * split a query instance into key and value, e.g. qk<iname> --> {qk,iname}
     * @param queryString a query instance, format "value<key>"
     * @return A list of key and value, [0] for value, [1] for key (optional)
     */
    public static List<String> splitValueAndKey(String queryString) {

        // first filter the query instance
        for (Map.Entry<String, String> entry : IndexConsts.replaceMap.entrySet()) {
            queryString = queryString.replace(entry.getKey(), entry.getValue());
        }

        List<String> feedback = new ArrayList<>();

        // use regex to match the value and key
        if (!queryString.isBlank()){
            String pattern = "<.*>$";
            Pattern regex = Pattern.compile(pattern);
            Matcher match = regex.matcher(queryString);
            if (match.find()){
                feedback.add(queryString.substring(0, match.start()).trim());
                feedback.add(queryString.substring(match.start() + 1, match.end() - 1));
            } else {
                feedback.add(queryString);
                feedback.add("all");
            }
            return feedback;
        }
        throw new QueryFormatException(3, "Split Key value from OPinstance fails");
    }

    public static void main(String[] arg){

        // boolean query parse test1
        String testQuery1 = "jzm<iname> | less<id> & 9225";
        List<String> parseResult1 = BooleanQueryParser.convertMidEqnToRearEqn(testQuery1);
        System.out.println(parseResult1);
        // boolean query parse test2
        String testQuery2 = "jzm<iname> & less<id> | 9225 ^ 4000";
        List<String> parseResult2 = BooleanQueryParser.convertMidEqnToRearEqn(testQuery2);
        System.out.println(parseResult2);
        // boolean query parse test 3
        String testQuery3 = "12|34|56|78";
        List<String> parseResult3 = BooleanQueryParser.convertMidEqnToRearEqn(testQuery3);
        System.out.println(parseResult3);

        // KV split test 1
        String testSplit1 = "qk{性别}";
        List<String> splitResult1 = BooleanQueryParser.splitValueAndKey(testSplit1);
        System.out.println(splitResult1);
        // KV split test 2
        String testSplit2 = "qk  ";
        List<String> splitResult2 = BooleanQueryParser.splitValueAndKey(testSplit2);
        System.out.println(splitResult2);
        // KV split test 3
        String testSplit3 = "qk<ina 9225";
        List<String> splitResult3 = BooleanQueryParser.splitValueAndKey(testSplit3);
        System.out.println(splitResult3);
    }

}
