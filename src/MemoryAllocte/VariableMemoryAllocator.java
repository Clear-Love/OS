package MemoryAllocte;


/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/27 18:01
 * @description TODO 最优适应算法(BF)
 * @modified lmio
 */
public class VariableMemoryAllocator implements MemoryAllocator{

    private final MemoryTable freeList;  // 未分配内存块表
    private final MemoryTable allocateList;  // 分配内存块表
    private final int totalSize;  // 内存总大小

    public VariableMemoryAllocator(int totalSize) {
        this.freeList = new MemoryTable("未分配表");
        this.allocateList = new MemoryTable("已分配表");
        //分配一个内存区，前4k被系统占用
        //系统占用4k
        this.freeList.insert(new MemoryBlock(4, totalSize));
        this.totalSize = totalSize;
    }

    @Override
    public void allocate(int size, String workName) {
        // 找到一个空闲块，并将其分割成两个部分
        MemoryBlock block = findFreeBlock(size);
        if (block == null) {
            System.out.println("Not enough memory!");
            return;
        }

        // 将空闲块分成两个部分，一部分分配给用户，另一部分保留为空闲块
        MemoryBlock allocatedBlock = new MemoryBlock(block.startAddress, size);
        allocatedBlock.allocated = true;
        allocatedBlock.setWorkName(workName);
        block.startAddress += size;
        block.size -= size;
        System.out.println("进程" + workName + "分配成功:" + allocatedBlock);

        // 将分配的内存块插入链表中 使用插入排序，确保空闲表有序
        allocateList.insert_sort(allocatedBlock);
    }

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

        freeList.insert(freedBlock);

        // 找到内存块的前后节点
        MemoryBlock prevBlock = freeList.findPrevBlock(freedBlock);
        MemoryBlock nextBlock = freeList.findNextBlock(freedBlock);

        // 将内存块与前后节点合并
        if (prevBlock != null && nextBlock != null) {
            prevBlock.size += freedBlock.size + nextBlock.size;
            freeList.remove(freedBlock);
            freeList.remove(nextBlock);
        } else if (prevBlock != null) {
            prevBlock.size += freedBlock.size;
            freeList.remove(freedBlock);
        } else if (nextBlock != null) {
            nextBlock.startAddress = freedBlock.startAddress;
            nextBlock.size += freedBlock.size;
            freeList.remove(freedBlock);
        }

    }

    @Override
    public void showAllocatedMemory() {
        allocateList.show();
    }

    @Override
    public void showFreeMemory() {
        freeList.show();
    }

    public MemoryBlock findFreeBlock(int size) {
        if(size > totalSize || size <= 0){
            System.out.println("out of index!");
        }
        //从空闲区找到对应内存区
        for (MemoryBlock block : freeList.blocks) {
            if (block.size >= size) {
                return block;
            }
        }
        return null;
    }
}


