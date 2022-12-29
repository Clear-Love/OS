package DiskManager;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/29 16:05
 * @description TODO 电梯算法（扫描算法）
 * @modified lmio
 */
public class SCAN extends DiskScheduler{
    public SCAN(int currentPosition, int endPosition) {
        super(currentPosition, endPosition);
        System.out.println("对请求队列排序");
        requestSequence.sort(Comparator.comparingInt(p -> p));
        System.out.println("[排序后]");
        System.out.println(Arrays.toString(requestSequence.toArray()));
    }

    @Override
    void schedule() {
        // 依次服务每个磁道请求
        int index = requestVal();
        while(!requestSequence.isEmpty()){
            serve(requestSequence.get(index));
            index = Math.max(0, index - 1);
        }
        // 输出移动总道数
        System.out.println("移动总道数：" + totalMovement);
    }

    /**
     * @author lmio
     * @description TODO 查找磁头右边第一个要服务的磁道
     * @time 16:54 2022/12/29
     * @name requestVal
     * @returntype int
     **/
    int requestVal(){
        // 使用二分查找找到小与等于目标值的最大值
        int left = 0;
        int right = requestSequence.size() - 1;
        while(left < right){
            int mid = (left + right + 1) >> 1;
            if(requestSequence.get(mid) > currentPosition){
                right = mid - 1;
            }else {
                left = mid;
            }
        }
        return left;
    }
}
