package MemoryAllocte;

/**
 * @author lmio
 * @version 1.0
 * @time 2023/1/1 22:12
 * @description TODO 最坏适应算法
 * @modified lmio
 */
public class WF_MemoryAllocator extends variableMemoryAllocate{
    public WF_MemoryAllocator(int totalSize) {
        super(totalSize);
        //按内存大小递减排序
        this.comparator = (p1, p2) ->(p2.size - p1.size);
    }
}
