# 回答重点

## HashMap 是基于哈希表的数据结构，用于存储键值对（key-value）。其核心是将键的哈希值映射到数组索引位置，通过数组 + 链表（在 Java 8 及之后是数组 + 链表 + 红黑树）来处理哈希冲突。

## HashMap 使用键的 hashCode() 方法计算哈希值，并通过 indexFor 方法（JDK 1.7 及之后版本移除了这个方法，直接使用 (n - 1) & hash]）确定元素在数组中的存储