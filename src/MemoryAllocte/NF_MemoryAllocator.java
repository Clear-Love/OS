package MemoryAllocte;

import java.util.Comparator;
import java.util.ListIterator;

/**
 * @author lmio
 * @version 1.0
 * @time 2023/1/1 23:07
 * @description TODO 邻近适应算法（Next Fit）
 * @modified lmio
 */
public class NF_MemoryAllocator extends variableMemoryAllocate{

    // 当前遍历位置
    MemoryBlock now;
    public NF_MemoryAllocator(int totalSize) {
        super(totalSize);
        //按地址递增的方式排列
        this.comparator = Comparator.comparingInt(p -> p.startAddress);
        now = freeList.blocks.get(0);
    }

    @Override
    public MemoryBlock findFreeBlock(int size) {
        if(size > getTotalSize() || size <= 0){
            System.out.println("out of index!");
            return null;
        }
        ListIterator<MemoryBlock> it = freeList.blocks.listIterator();
        while(it.hasNext()){
            if(it.next() == now)
                break;
        }
        //从空闲区找到对应内存区，用迭代器模拟循环链表
        MemoryBlock mb;
        do {
            if(!it.hasNext())
                it = freeList.blocks.listIterator();
            mb = it.next();
            if (mb.size >= size) {
                now = mb;
                return mb;
            }
        }while (mb != now);
        System.out.println("未找到该作业");
        return null;
    }
}
