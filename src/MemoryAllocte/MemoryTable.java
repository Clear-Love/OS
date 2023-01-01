package MemoryAllocte;

import java.util.LinkedList;

import java.util.ListIterator;

/**
 * @author lmio
 * @version 1.0
 * @time 2022/12/27 17:36
 * @description TODO 内存分配表
 * @modified lmio
 */
public class MemoryTable{
    public final LinkedList<MemoryBlock> blocks;  // 空间块列表

    private final String name;

    public MemoryTable(String name) {
        this.blocks = new LinkedList<>();
        this.name = name;
    }


    public void show(){
        if(!blocks.isEmpty()){
            System.out.println("[" + name +"]");
            System.out.println("进程名称\t起始地址\t内存块大小");
        }else {
            System.out.println(name + "为空");
        }
        for (MemoryBlock block : blocks) {
            System.out.println(block);
        }
    }

    public void insert(MemoryBlock block){
        blocks.add(block);
    }


    public void remove(MemoryBlock mb) {
        ListIterator<MemoryBlock> it = blocks.listIterator();
        while (it.hasNext()) {
            MemoryBlock block = it.next();
            if (block.equals(mb)) {
                it.remove();
                return ;
            }
        }
    }

    public MemoryBlock remove(String name) {
        ListIterator<MemoryBlock> it = blocks.listIterator();
        while (it.hasNext()) {
            MemoryBlock block = it.next();
            if (block.getWorkName().equals(name)) {
                it.remove();
                return block;
            }
        }
        return null;
    }

    //找到内存区相连的上一个结点
    public MemoryBlock findPrevBlock(MemoryBlock mb) {
        for (MemoryBlock block : blocks) {
            if (block.startAddress + block.size == mb.startAddress) {
                return block;
            }
        }
        return null;
    }

    //找到内存区相连的下一个结点
    public MemoryBlock findNextBlock(MemoryBlock mb) {
        for (MemoryBlock block : blocks) {
            if (block.startAddress == mb.startAddress + mb.size) {
                return block;
            }
        }
        return null;
    }
}
