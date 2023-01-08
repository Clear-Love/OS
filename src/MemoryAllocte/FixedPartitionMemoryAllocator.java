package MemoryAllocte;


/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/27 23:31
 * @description TODO 固定分区内存分配
 * @modified lmio
 */
public class FixedPartitionMemoryAllocator extends MemoryAllocator{

    public FixedPartitionMemoryAllocator(int totalSize) {
        super(totalSize);
        MemoryBlock MB1 = new MemoryBlock(4, 8);
        MemoryBlock MB2 = new MemoryBlock(12, 16);
        MemoryBlock MB3 = new MemoryBlock(28, 16);
        MemoryBlock MB4 = new MemoryBlock(44, 24);
        MemoryBlock MB5 = new MemoryBlock(68, 24);
        MemoryBlock MB6 = new MemoryBlock(92, 36);
        freeList.insert(MB1);
        freeList.insert(MB2);
        freeList.insert(MB3);
        freeList.insert(MB4);
        freeList.insert(MB5);
        freeList.insert(MB6);
        System.out.println("固定分区表初始化完成");
    }

    @Override
    public void allocate(int size, String workName) {
        MemoryBlock freeBlock = findFreeBlock(size);
        if(freeBlock == null){
            System.out.println("内存不足，分配失败，请释放内存后再试");
        }else {
            System.out.println("分配成功");
            freeBlock.allocated =true;
            freeBlock.setWorkName(workName);
            allocateList.insert(freeBlock);
            freeList.remove(freeBlock);
        }

    }

    @Override
    public void free(String workName) {
        MemoryBlock freedBlock = allocateList.remove(workName);

        if(freedBlock == null){
            System.out.println("释放内存块失败");
            return;
        }
        freedBlock.setWorkName("");
        freedBlock.allocated = false;
        freeList.insert(freedBlock);
    }

}
