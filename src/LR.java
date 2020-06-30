package www.gzhou.compile;

import java.util.HashMap;
import java.util.Stack;

public class LR {
    private int[][] table =
            {{-1, -1, 2, -2, 3, -1, 1},
                    {4, 5, -3, -2, -3, 0, -10},
                    {-1, -1, 2, -3, 3, -1, 6},
                    {104, 104, -3, 104, -3, 104, -10},
                    {-1, -1, 2, -2, 3, -1, 7},
                    {-1, -1, 2, -2, 3, -1, 8},
                    {4, 5, -3, 9, -3, -4, -10},
                    {101, 5, -3, 101, -3, 101, -10},
                    {102, 102, -3, 102, -3, 102, -10},
                    {103, 103, -3, 103, -3, 103, -10}};
    // <-1代表
    // 0代表接受 acc
    // 1~100 代表移进
    // >101 代表规约

    private Stack<Character> symbol_stack = new Stack<>();//符号栈
    private Stack<Integer> state_stack = new Stack<>();//状态栈
    private String input; //输入串,初始化时默认加上#结尾

    private int index = 0;//记录当前读到字符串第几个字符
    private HashMap<String, Integer> keywords = new HashMap<>();

    private int state = 0;//记录一次状态
    private int act = -1;//记录对应的表格内的数字

    private int count = 0;//步骤
    //输出各个容器中的元素
    public void printPro(){
        System.out.println("--步骤"+count++);
        System.out.println("输入串:"+input.substring(index,input.length()));
        System.out.print("符号栈从栈底到顶：");
        for(Character c:symbol_stack){
            System.out.print(c);
        }
        System.out.println();
        System.out.print("状态栈从栈底到顶：");
        for (Integer i:state_stack){
            System.out.print(i);
        }
        System.out.println();

    }

    public LR(String input) {//初始化分析
        this.input = input + "#";//输入串加上'#' 初始化输入串
        init_keywords();
        try {
            find();//将初始输入转换成标准格式
            state = 0;
            state_stack.push(state);
            symbol_stack.push('#');//初始化栈
            index = 0;//指向输入串第一个,输入串初始化完成
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        printPro();
    }

    private int init_keywords() {//将输入符号 转换成 LR分析表中的对应列数
        keywords.put("+", 0);
        keywords.put("*", 1);
        keywords.put("(", 2);
        keywords.put(")", 3);
        keywords.put("i", 4);
        keywords.put("#", 5);
        keywords.put("E", 6);
        return 0;//正常
    }

    public void show() {

        try {
            while ((act = table[state_stack.peek()][keywords.get(input.charAt(index) + "")]) != 0) {
                process();
            }
            System.out.println("表达式正确,分析成功!");

        } catch (Exception e) {
            if(e.getMessage()!=null) System.out.println(e.getMessage());
        }
    }


    private void process() throws Exception {//处理不同的移进或规约
        try {
            if (act < 0) {
                errorMessage();
            } else if (act > 100) {//规约
                convention();
                System.out.println("----------------------------规约-----------------------------");
                printPro();
            } else if (act > 0) {//移进
                symbol_stack.push(input.charAt(index++));//符号栈进栈一个输入,输入串指向下个字符
                state_stack.push(act);//状态栈进栈要移进的状态
                System.out.println("---------------------------移进-------------------------------");
                printPro();
            }

        } catch (Exception e) {
            throw e;
        }
    }


    private void convention() {//规约
        switch (act) {
            case 104: {
                //出栈,弹出一个符号和一个状态
                symbol_stack.pop();
                state_stack.pop();
                //加入规约'E'
                symbol_stack.push('E');
                //将GOTO的数字推入状态栈
                state = state_stack.peek();
                act = table[state][keywords.get("E")];
                state_stack.push(act);
                break;
            }
            case 103: {
            }//这里正巧101-103处理都是一样的
            case 102: {
            }
            case 101: {
                //弹出(E)
                symbol_stack.pop();
                symbol_stack.pop();
                symbol_stack.pop();
                //弹出状态
                state_stack.pop();
                state_stack.pop();
                state_stack.pop();
                //加入规约的'E'
                symbol_stack.push('E');
                //GOTO处理
                state = state_stack.peek();
                act = table[state][keywords.get("E")];
                state_stack.push(act);
                break;
            }
        }
    }

    private void errorMessage() throws Exception {//显示出错原因
        switch (act) {
            case -1:
                throw new Exception("处于状态:" + state + " ,缺少运算对象,即id或左括号,而实际遇到的是'" + input.charAt(index) + "'");
            case -2:
                throw new Exception("处于状态:" + state + " ,右括号不配对");
            case -3:
                throw new Exception("处于状态:" + state + " ,缺少操作符," + "而实际遇到的是'" + input.charAt(index) + "'");//当遇到i或者)时用
            case -4:
                throw new Exception("处于状态:" + state + " ,缺少操作符或右括号," + "而实际遇到的是'" + input.charAt(index) + "'");
        }
        throw new Exception("error");
    }

    /*
    将初始输入转换成标准格式
     */
    private void find() throws Exception {//找到之后是"+","*",...中的哪一个,都不属于则报错
        String out = "";//暂存,每次处理后先存到out,最后让input=out
        while (index < input.length()) {
            if (isDigit(input.charAt(index))) {
                index++;
                while (isDigit(input.charAt(index))) {
                    index++;
                }
                out += "i";
            } else if (isLetter(input.charAt(index))) {
                String tmp = "";
                int flag = 0;

                while (isLetter(input.charAt(index))) {
                    tmp += input.charAt(index);
                    flag++;
                    index++;
                }
                if (flag != 0) {
                    while (isDigit(input.charAt(index))) {
                        tmp += input.charAt(index);
                        index++;
                    }
                }
                if (keywords.containsKey(tmp)) {
                    out += tmp;
                } else {
                    out += "i";
                }

            } else if (keywords.containsKey(input.charAt(index) + "")) {
                index++;
                out += input.charAt(index - 1) + "";
            } else {
                //因为如果非法输入了,上面也会报错,要把前面报错的屏蔽掉
                throw new ArrayStoreException("第" + (index + 1) + "个输入字符" + input.charAt(index) + "为非法输入!");
            }
        }
        input = out;
    }


    //判断是否是字母
    boolean isLetter(char letter) {
        return (letter >= 'a' && letter <= 'z') || (letter >= 'A' && letter <= 'Z');
    }

    //判断是否是数字
    boolean isDigit(char digit) {
        return digit >= '0' && digit <= '9';
    }
}
