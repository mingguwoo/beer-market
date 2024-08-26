package com.sh.beer.market.test;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author
 * @date 2023/7/11
 */
//@SpringJUnitConfi

public class Main {
        public static void main(String[] args) {
            List<String> list = new ArrayList<>();
            list.add("foo");
            list.add("hello");
            list.add("a1b");
            list.add("bar");
            list.add("ss1s");
            list.add("123");
            list.add("pp");
            list.add("A.1");

            removeStringsContainingNumbers(list);

            System.out.println("Filtered List:");
            for (String str : list) {
                System.out.println(str);
            }
        }

        public static void removeStringsContainingNumbers(List<String> list) {
            Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                String str = iterator.next();
                if (str.matches(".*\\d.*")) { // 使用正则表达式匹配含有数字的字符串
                    iterator.remove(); // 移除含有数字的字符串
                }
            }
        }
}
