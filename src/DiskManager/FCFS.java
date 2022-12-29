package DiskManager;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/29 14:57
 * @description TODO 先来先服务（FCFS）磁盘调度算法 先到来的请求，先被服务
 * @modified lmio
 */
public class FCFS extends DiskScheduler {

    public FCFS(int currentPosition, int endPosition) {
        super(currentPosition, endPosition);
    }

    @Override
    void schedule(){
        // 依次服务每个磁道请求
        while (!requestSequence.isEmpty()){
            serve(requestSequence.get(0));
        }

        // 输出移动总道数
        System.out.println("移动总道数：" + totalMovement);
    }
}
