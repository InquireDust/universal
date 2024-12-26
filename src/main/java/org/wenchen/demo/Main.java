package org.wenchen.demo;

import cn.hutool.core.io.FileUtil;

/**
 * Author: wen-chen
 * Date: 2024/11/11
 */
public class Main {
    public static void main(String[] args) {
        String string = "回答重点 \\n\\n HashMap 是基于哈希表的数据结构，用于存储键值对（key-value）。其核心是将键的哈希值映射到数组索引位置，通过数组 + 链表（在 Java 8 及之后是数组 + 链表 + 红黑树）来处理哈希冲突。\\n\\nHashMap 使用键的 hashCode() 方法计算哈希值，并通过 indexFor 方法（JDK 1.7 及之后版本移除了这个方法，直接使用 (n - 1) & hash]）确定元素在数组中的存储";
        // 把string写入到 test.md文中,\n\n替换为\n\n
        string = string.replace("\\n\\n", "\n\n");
        System.out.println(string);
        FileUtil.writeUtf8String(string, "./test.md");
    }
}
