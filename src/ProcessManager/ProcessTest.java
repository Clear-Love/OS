package ProcessManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/24 21:16
 * @description TODO 测试各种进程调用算法
 * @modified lmio
 */



public class ProcessTest {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Scheduler scheduler = null;
        while (true) {
            System.out.println("0. 退出\t1. 先来先服务\t2. 短作业优先\t3. 高优先级优先" +
                    "\t4. 高响应比优先\t5. 时间片轮换调度算法\t6. 多级反馈队列");
            int choice = input.nextInt();
            switch (choice) {
                case 0 -> System.exit(0);
                case 1 -> scheduler = new FCFS_Schedule(new Vector<>());
                case 2 -> scheduler = new SJF_Schedule(new Vector<>());
                case 3 -> scheduler = new HPF_Schedule(new Vector<>());
                case 4 -> scheduler = new HRRN_Schedule(new Vector<>());
                case 5 -> scheduler = new RR_Schedule(new Vector<>());
                case 6 -> scheduler = new MFQ_Schedule(new Vector<>());
                default -> {
                    System.out.println("输入有误，请重新输入");
                    continue;
                }
            }
            schedule_start(scheduler);
        }
    }

    public static void schedule_start(Scheduler sd) {
        Vector<PCB> newList = newPCBlist();
        Thread th = new Thread(sd);
        th.setDaemon(true);
        th.start();
        Timer insertPCB = new Timer(true);
        insertPCB.schedule(new TimerTask() {
            @Override
            public void run() {
                Iterator<PCB> it = newList.iterator();
                while (it.hasNext()) {
                    PCB pcb = it.next();
                    if (sd.currentTime >= pcb.getArrivalTime()) {
                        sd.insertProcess(pcb);
                        it.remove();
                    }
                }
            }
        }, 0, PCB.period);

        try {
            th.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static Vector<PCB> newPCBlist() {
        // 读取文件
        File file = new File("src/ProcessManager/process.txt");
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        // 使用正则表达式匹配每一行的参数
        Pattern pattern = Pattern.compile("(\\d+)\\s+(\\S+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");

        Vector<PCB> newList = new Vector<>();
        // 读取每一行，并使用构造函数创建对象
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Matcher matcher = pattern.matcher(line);
            if (matcher.matches()) {
                int id = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);
                int priority = Integer.parseInt(matcher.group(3));
                int arrivalTime = Integer.parseInt(matcher.group(4));
                int burstTime = Integer.parseInt(matcher.group(5));
                PCB pcb = new PCB(id, name, priority, arrivalTime, burstTime);
                newList.add(pcb);
            }
        }
        scanner.close();
        return newList;
    }
}
