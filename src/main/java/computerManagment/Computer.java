/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package computerManagment;

/**
 *
 * @author W10-Portable
 */
public class Computer {
    private int cpuCores;
    private int memoryMB;
    
    public Computer(int cpuCores, int memoryMB){
        this.cpuCores = cpuCores;
        this.memoryMB = memoryMB;
    }

    /**
     * @return the cpuCores
     */
    public int getCpuCores() {
        return cpuCores;
    }

    /**
     * @param cpuCores the cpuCores to set
     */
    public void setCpuCores(int cpuCores) {
        this.cpuCores = cpuCores;
    }

    /**
     * @return the memoryMB
     */
    public int getMemoryMB() {
        return memoryMB;
    }

    /**
     * @param memoryMB the memoryMB to set
     */
    public void setMemoryMB(int memoryMB) {
        this.memoryMB = memoryMB;
    }
    
    
}
