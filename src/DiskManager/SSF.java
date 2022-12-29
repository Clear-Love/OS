package DiskManager;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/29 15:13
 * @description TODO 最短寻道时间优先 优先选择从当前磁头位置所需寻道时间最短的请求
 * @modified lmio
 */
public class SSF extends DiskScheduler {
    public SSF(int currentPosition, int endPosition) {
        super(currentPosition, endPosition);
        // 把请求队列按从小到大排序
        requestSequence.sort(Comparator.comparingInt(p -> p));
        System.out.println("[排序后]");
        System.out.println(Arrays.toString(requestSequence.toArray()));
    }

    @Override
    void schedule() {
        while(!requestSequence.isEmpty()){
            int request = removeReq();
            serve(request);
        }
    }

    /**
     * @author lmio
     * @description TODO 找到磁头最近的磁道，移除并返回
     * @time 15:43 2022/12/29
     * @name removeReq
     * @returntype int
     **/
    private int removeReq(){
        // 使用二分查找找到大于等于目标值的最小值
        int left = 0;
        int right = requestSequence.size() - 1;
        while(left < right){
            int mid = (left + right) >> 1;
            if(requestSequence.get(mid) < currentPosition){
                left = mid + 1;
            }else {
                right = mid;
            }
        }

        left = Math.max(0, right - 1);
        //找到距离最近的
        int req = (requestSequence.get(right) + requestSequence.get(left))
                > 2 * currentPosition ? left : right;
        return requestSequence.remove(req);
    }
}
