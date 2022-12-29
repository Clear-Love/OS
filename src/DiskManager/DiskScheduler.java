package DiskManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/29 15:06
 * @description TODO 磁盘调度器抽象类
 * @modified lmio
 */
public abstract class DiskScheduler {
    //磁盘终点
    private final int endPosition;
    // 磁头位置
    public int currentPosition;
    //移动总道数
    public int totalMovement;
    //请求队列
    public LinkedList<Integer> requestSequence;

    public int getEndPosition() {
        return endPosition;
    }

    public DiskScheduler(int currentPosition, int endPosition) {
        this.currentPosition = currentPosition;
        this.endPosition = endPosition;
        this.totalMovement = 0;
        // 初始化磁道请求顺序
        System.out.println("读入磁道请求顺序...");
        try (BufferedReader reader = new BufferedReader(new FileReader("src/DiskManager/visit.txt"))) {
            String line = reader.readLine();
            String[] pageNumbers = line.split(" ");
            requestSequence = new LinkedList<>();
            for (String pageNumber : pageNumbers) {
                requestSequence.add(Integer.parseInt(pageNumber));
                System.out.print(".");
            }
            System.out.println("初始化请求序列成功");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("初始化请求序列失败");
            System.exit(-1);
        }

        System.out.println("[请求队列]");
        System.out.println(Arrays.toString(requestSequence.toArray()));
    }


    public void serve(int request){
        // 计算磁头移动的道数
        System.out.println("现在服务：" + request + "磁道");
        int movement = Math.abs(request - currentPosition);
        totalMovement += movement;
        // 更新磁头位置
        currentPosition = request;
        requestSequence.remove((Integer) request);
        System.out.println("当前磁头移动步数：" + totalMovement);
    }

    abstract void schedule();
}
