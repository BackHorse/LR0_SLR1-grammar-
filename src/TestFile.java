package www.gzhou.compile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TestFile {

    private static List<String> list1 = new ArrayList<>();
    private static List<Integer> list2 = new ArrayList<>();
    private static List<String> list3 = new ArrayList<>();
    public int[][] ReadTxt() throws IOException {
        String[][] a = readFile();
        StringBuilder st = new StringBuilder();
      for (String g : list1)
        {
            g = g.trim();
            st.append(g);
            if("".equals(g)) {
                list3.add(st.toString());
                st = new StringBuilder();
            }
        }
        for (String g : list3){ //分五种情况
            g = g.trim();
            if (g.startsWith("-")) {
                list2.add(-3);
            }
            else if (g.startsWith("a")) {
                list2.add(0);
            }
            else if (g.startsWith("s")) {
                list2.add(Integer.parseInt(g.substring(1, g.length())));
            }
            else if (g.startsWith("r")) {
                list2.add(100 + Integer.parseInt(g.substring(1, g.length())));
            }
            else if(g != " "){
                list2.add(Integer.parseInt(g.substring(0, g.length())));
            }
        }

        int[][] table = new int[14][10];
        for (int i = 0 ; i < 14; i++){
            for (int j = 0; j < 10; j++){
                table[i][j] = list2.get(i*10+j);
            }
        }
        return table;
    }
    public static String[][] readFile() throws IOException{
        File file = new File("E:\\桌面文件路径\\编译原理\\compileLR-master\\table.txt");
        if (!file.exists())
            throw new RuntimeException("文件不存在");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String str;
        List<String[]> list = new ArrayList<>();
        str = br.readLine();
        while ((str = br.readLine()) != null) {
            int s = 0;
            String[] arr = str.split("");
            String[] strs = new String[arr.length];
            for (String s1 : arr) {
                if (s1 != null) {
                    strs[s++] = s1;
                }
            }
            list.add(strs);
        }
        int max = 0;
        for (int i = 0; i < list.size(); i++) {
            if (max < list.get(i).length)
                max = list.get(i).length;
        }
        String[][] array = new String[list.size()][max];
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < list.get(i).length; j++) {
                array[i][j] = list.get(i)[j];
                list1.add(list.get(i)[j]);

            }
        }
        return array;
    }
}
