package MemoryManager;


/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/27 23:31
 * @description TODO 固定分区内存分配
 * @modified lmio
 */
public class FixedPartitionMemoryAllocator implements MemoryAllocator{
    private final MemoryTable MemoryList;

    public FixedPartitionMemoryAllocator() {
        MemoryList = new MemoryTable("固定分区表");
        MemoryBlock MB1 = new MemoryBlock(4, 8);
        MemoryBlock MB2 = new MemoryBlock(12, 16);
        MemoryBlock MB3 = new MemoryBlock(28, 16);
        MemoryBlock MB4 = new MemoryBlock(44, 24);
        MemoryBlock MB5 = new MemoryBlock(68, 24);
        MemoryBlock MB6 = new MemoryBlock(92, 36);
        MemoryList.insert(MB1);
        MemoryList.insert(MB2);
        MemoryList.insert(MB3);
        MemoryList.insert(MB4);
        MemoryList.insert(MB5);
        MemoryList.insert(MB6);
        System.out.println("固定分区表初始化完成");
    }

    @Override
    public void allocate(int size, String workName) {
        for (MemoryBlock block : MemoryList.blocks) {
            if(!block.allocated && block.size >= size){
                block.allocated = true;
                block.setWorkName(workName);
                System.out.println("进程" + workName + "分配成功:" + block);
                return;
            }
        }
        System.out.println("内存不足，分配失败，请释放内存后再试");
    }

    @Override
    public void free(String workName) {
        for (MemoryBlock block : MemoryList.blocks) {
            if(block.getWorkName().equals(workName) && block.allocated){
                block.allocated = false;
                block.setWorkName("");
                System.out.println("进程" + workName + "释放成功:" + block);
                return;
            }
        }
        System.out.println("释放失败! 进程可能已经释放");
    }

    @Override
    public void showAllocatedMemory() {
        if(!MemoryList.blocks.isEmpty()){
            System.out.println("[已分配表]");
            System.out.println("进程名称\t起始地址\t内存块大小\t是否分配");
        }else {
            System.out.println("已分配表为空");
        }
        for (MemoryBlock block : MemoryList.blocks) {
            if(block.allocated){
                System.out.println(block);
            }
        }
    }

    @Override
    public void showFreeMemory() {
        if(!MemoryList.blocks.isEmpty()){
            System.out.println("[未分配表]");
            System.out.println("进程名称\t起始地址\t内存块大小\t是否分配");
        }else {
            System.out.println("未分配表为空");
        }
        for (MemoryBlock block : MemoryList.blocks) {
            if(!block.allocated){
                System.out.println(block);
            }
        }
    }

    public void show(){
        MemoryList.show();
    }

}
