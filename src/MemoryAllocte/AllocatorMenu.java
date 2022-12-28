package MemoryAllocte;

import java.util.Scanner;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/27 13:00
 * @description TODO 内存管理菜单界面
 * @modified lmio
 */
public class AllocatorMenu {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        mainMenu();
    }

    private static void mainMenu() {
        System.out.println("主菜单：");
        System.out.print("1. 固定分区\t");
        System.out.print("2. 可变分区\t");
        System.out.println("3. 退出");
        System.out.print("请输入选择：");
        int choice = sc.nextInt();
        switch (choice) {
            case 1 -> fixedPartitionMenu();
            case 2 -> variablePartitionMenu();
            case 3 -> System.exit(0);
            default -> {
                System.out.println("无效的选择，请重试。");
                mainMenu();
            }
        }
    }

    private static void fixedPartitionMenu() {
        int choice;
        FixedPartitionMemoryAllocator fixedAllocator = new FixedPartitionMemoryAllocator();
        fixedAllocator.allocate(10, "1");
        fixedAllocator.allocate(20, "2");
        fixedAllocator.allocate(30, "3");
        fixedAllocator.allocate(40, "4");
        fixedAllocator.allocate(50, "5");
        while (true){
            System.out.print("固定分区菜单：");
            choice = menuChoice();
            switch (choice) {
                // 调用固定分区中分配空间的函数
                case 1 -> Allocate(fixedAllocator);

                // 调用固定分区中释放空间的函数
                case 2 -> FreeBlock(fixedAllocator);

                case 3 -> fixedAllocator.show();
                case 4 -> mainMenu();
                case 0 -> System.exit(0);
                default -> {
                    System.out.println("无效的选择，请重试。");
                    fixedPartitionMenu();
                }
            }
        }
    }

    //固定分区分配空间
    private static void Allocate(MemoryAllocator allocator) {
        System.out.print("请输入要分配的内存大小：");
        int size = sc.nextInt();
        System.out.print("请输入作业名称：");
        String workName = sc.next();
        allocator.allocate(size, workName);
    }

    private static void variablePartitionMenu() {
        int choice;
        VariableMemoryAllocator varAllocator = new VariableMemoryAllocator(128);
        varAllocator.allocate(10, "1");
        varAllocator.allocate(20, "2");
        varAllocator.allocate(30, "3");
        varAllocator.allocate(40, "4");
        varAllocator.allocate(50, "5");
        while(true){
            System.out.print("可变分区菜单：");
            choice = menuChoice();

            switch (choice) {
                // 调用可变分区中分配空间的函数
                case 1 -> Allocate(varAllocator);

                // 调用可变分区中释放空间的函数
                case 2 -> FreeBlock(varAllocator);
                case 3 ->{
                    varAllocator.showAllocatedMemory();
                    varAllocator.showFreeMemory();
                }
                case 4 -> mainMenu();
                case 0 -> System.exit(0);
                default -> {
                    System.out.println("无效的选择，请重试。");
                    variablePartitionMenu();
                }
            }
        }
    }

    private static int menuChoice() {
        System.out.print("1. 分配空间\t");
        System.out.print("2. 释放空间\t");
        System.out.print("3. 查看分区表\t");
        System.out.println("4. 返回主菜单");
        System.out.println("0. 退出");
        System.out.print("请输入选择：");
        return sc.nextInt();
    }

    private static void FreeBlock(MemoryAllocator allocator) {
        System.out.print("请输入作业名称：");
        String workName = sc.next();
        allocator.free(workName);
    }
}
