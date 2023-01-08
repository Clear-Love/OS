package VirtualMemoryManager;

import java.util.Scanner;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/28 20:25
 * @description TODO 虚拟内存管理的测试
 * @modified lmio
 */
public class VirtualMemoryTest {


    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("0. 退出\t1. 先进先出\t2. 最不常用\t3. 最近最久未使用" +
                    "\t4. 时钟页面置换");
            int choice = input.nextInt();
            switch (choice) {
                case 0 -> System.exit(0);
                case 1 -> new FIFO(3, 10).run();
                case 2 -> new LFU(3, 10).run();
                case 3 -> new LRU(3, 10).run();
                case 4 -> new Clock(3, 10).run();
                default -> System.out.println("输入有误，请重新输入");

            }
        }
    }
}
