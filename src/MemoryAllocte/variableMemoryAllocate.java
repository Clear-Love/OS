package MemoryAllocte;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * @author lmio
 * @version 1.0
 * @time 2023/1/1 22:54
 * @description TODO 可变分区父类
 * @modified lmio
 */
public abstract class variableMemoryAllocate extends MemoryAllocator{

    Comparator<MemoryBlock> comparator;
    public variableMemoryAllocate(int totalSize) {
        super(totalSize);
        //分配一个内存区，前4k被系统占用
        //系统占用4k
        this.freeList.insert(new MemoryBlock(4, totalSize - 4));
    }

    @Override
    public void allocate(int size, String workName) {
        // 找到一个空闲块，并将其分割成两个部分
        MemoryBlock block = findFreeBlock(size);
        if (block == null) {
            System.out.println("内存不足！");
            return;
        }

        // 将空闲块分成两个部分，一部分分配给用户，另一部分保留为空闲块
        MemoryBlock allocatedBlock = new MemoryBlock(block.startAddress, size);
        allocatedBlock.setWorkName(workName);
        block.startAddress += size;
        block.size -= size;
        System.out.println("进程" + workName + "分配成功:" + allocatedBlock);

        // 将分配的内存块插入链表中
        allocatedBlock.allocated = true;
        allocateList.insert(allocatedBlock);
    }

    /**
     * @author lmio
     * @description TODO 插入排序
     * @time 23:00 2023/1/1
     * @name free
     * @returntype void
     **/
    @Override
    public void free(String name) {
        // 找到要释放的内存块
        MemoryBlock freedBlock = allocateList.remove(name);
        if (freedBlock == null) {
            System.out.println("Invalid address!");
            return;
        }
        freedBlock.setWorkName("");
        freedBlock.allocated = false;
        //合并空闲区
        if(!marge(freedBlock)){
            //排序
            insert_sort(freedBlock);
        }

    }

    /**
     * @author lmio
     * @description TODO 使用插入排序保持空闲表有序
     * @time 0:48 2023/1/2
     * @name insert_sort
     * @returntype void
     **/
    public void insert_sort(MemoryBlock block){
        LinkedList<MemoryBlock> blocks = freeList.blocks;
        ListIterator<MemoryBlock> it = blocks.listIterator();
        while (it.hasNext()) {
            //按内存大小排序
            if (comparator.compare(it.next(), block) > 0) {
                it.previous();
                it.add(block);
                return;
            }
        }
        blocks.add(block);
    }
}
