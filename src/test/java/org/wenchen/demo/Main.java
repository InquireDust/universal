package org.wenchen.demo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Author: wen-chen
 * Date: 2024/9/26
 */
public class Main {
    /**
     * 计算两个日期之间的天数差（前闭后开）
     *
     * @param startDate 开始日期（前闭）
     * @param endDate   结束日期（后开）
     * @return 相差的天数
     */
    public static long calculateDaysDifference(LocalDate startDate, LocalDate endDate) {
        // 确保结束日期在开始日期之后
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("结束日期必须在开始日期之后");
        }
        return ChronoUnit.DAYS.between(startDate, endDate);
    }


    public static LocalDate getNextMonthFirstDay(LocalDate date) {
        return date.plusMonths(1).withDayOfMonth(1);
    }


    public static void main(String[] args) {
//        LocalDate startDate = LocalDate.of(2024, 9, 1);
//        LocalDate endDate = LocalDate.of(2025, 10, 10);
//        startDate.getYear();
//        startDate.getMonth();
//        int dayOfMonth = startDate.getDayOfMonth();
//
//        long daysDifference = calculateDaysDifference(startDate, endDate);
//        System.out.println("两个日期之间的天数差（前闭后开）: " + daysDifference);
        LocalDate now = LocalDate.now();
        LocalDate start = LocalDate.now();
        for (int i = 0; i < 100; i++) {
            System.out.println(start.isEqual(now));
            start.isSupported(ChronoUnit.DAYS);
            now = getNextMonthFirstDay(now);
            System.out.println(now);
        }
    }
}
