package MemoryAllocte;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/27 17:30
 * @description TODO 内存调度器抽象类
 * @modified lmio
 */
public abstract class MemoryAllocator {
    public final MemoryTable freeList;  // 未分配内存块表
    public final MemoryTable allocateList;  // 分配内存块表
    private final int totalSize;  // 内存总大小

    public int getTotalSize() {
        return totalSize;
    }

    public MemoryAllocator(int totalSize) {
        this.freeList = new MemoryTable("未分配表");
        this.allocateList = new MemoryTable("已分配表");
        this.totalSize = totalSize;
    }

    // 分配内存空间
    abstract void  allocate(int size, String workName);

    // 释放内存空间
    abstract void free(String name);

    // 获取已分配的内存空间信息
    public void showAllocatedMemory() {
        allocateList.show();
    }

    public void showFreeMemory() {
        freeList.show();
    }

    /**
     * @author lmio
     * @description TODO 合并空闲区
     * @time 20:42 2023/1/1
     * @name marge
     * @returntype void
     **/
    public boolean marge(MemoryBlock freedBlock){
        // 找到内存块的前后节点
        MemoryBlock prevBlock = freeList.findPrevBlock(freedBlock);
        MemoryBlock nextBlock = freeList.findNextBlock(freedBlock);

        // 将内存块与前后节点合并
        if (prevBlock != null && nextBlock != null) {
            prevBlock.size += freedBlock.size + nextBlock.size;
            freeList.remove(nextBlock);
            return true;
        } else if (prevBlock != null) {
            prevBlock.size += freedBlock.size;
            return true;
        } else if (nextBlock != null) {
            nextBlock.startAddress = freedBlock.startAddress;
            nextBlock.size += freedBlock.size;
            return true;
        }
        //不能与队列中的元素合并
        return false;
    }

    public MemoryBlock findFreeBlock(int size) {
        if(size > getTotalSize() || size <= 0){
            System.out.println("out of index!");
            return null;
        }
        //从空闲区找到对应内存区
        for (MemoryBlock block : freeList.blocks) {
            if (block.size >= size) {
                return block;
            }
        }
        System.out.println("未找到该作业");
        return null;
    }
}
