package DiskManager;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/29 17:51
 * @description TODO 循环扫描算法 只有磁头朝请求队列右边（已经排序）移动时，才处理磁道访问请求
 * @modified lmio
 */
public class SCS extends SCAN{
    public SCS(int currentPosition, int endPosition) {
        super(currentPosition, endPosition);
    }

    @Override
    void schedule() {
        // 依次服务每个磁道请求
        int index = requestVal();
        while(!requestSequence.isEmpty()){
            if(index < requestSequence.size()){
                serve(requestSequence.get(index));
            }else {
                serve(requestSequence.get(0));
            }
        }
        // 输出移动总道数
        System.out.println("移动总道数：" + totalMovement);
    }
}
