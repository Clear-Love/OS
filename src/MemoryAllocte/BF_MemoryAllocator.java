package MemoryAllocte;


import java.util.Comparator;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/27 18:01
 * @description TODO 最优适应算法(BF)
 * @modified lmio
 */
public class BF_MemoryAllocator extends variableMemoryAllocate{

    public BF_MemoryAllocator(int totalSize) {
        super(totalSize);
        //按内存大小从小到大排序
        this.comparator = Comparator.comparingInt(p -> p.size);
    }
}


