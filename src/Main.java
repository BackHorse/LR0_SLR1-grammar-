package www.gzhou.compile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] arg){
        System.out.println("请输入字符串：");
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        String expression;
        try {
            expression = in.readLine();//读入输入
            LR test=new LR(expression);
            test.show();//开始分析

        } catch (IOException e) {
            System.out.println("输入出错！");
        }

    }
}
