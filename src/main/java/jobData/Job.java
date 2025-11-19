/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jobData;

/**
 *
 * @author W10-Portable
 */
public class Job {
    private String id;
    private String name;
    private int priority;
    private Resources resources = new Resources();
    private Workload workload = new Workload();
    
    private long startTime;
    private long durationTime;
    private long pid;
    
    
    public Job(){
        id = "";
        name = "";
        priority = 0;
        resources.getCpu_cores();
        resources.getMemory();
        workload.getDuration_ms();
        
        startTime = 0;
        durationTime = 0;
        pid = 0;
    }
    
    public int getCpuValue(){
        return this.resources.getCpu_cores();
    }
    
    public int getMemoryValue(){
        try {
            String[] memoryJob = this.resources.getMemory().split(" ");
            int memoryValue = Integer.parseInt(memoryJob[0]);
            return switch (this.getMemoryType()) {
                case "GB" ->
                    memoryValue * 1000;
                case "MB" ->
                    memoryValue;
                default ->
                    0;
            };
        } catch (NumberFormatException ex) {
            return 0;
        }
    }
    
    public String getMemoryType(){
        String[] memoryJob = this.resources.getMemory().split(" ");
        return memoryJob[1].toUpperCase();
    }
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * @return the resources
     */
    public Resources getResources() {
        return resources;
    }

    /**
     * @param resources the resources to set
     */
    public void setResources(Resources resources) {
        this.resources = resources;
    }

    /**
     * @return the workload
     */
    public Workload getWorkload() {
        return workload;
    }

    /**
     * @param workload the workload to set
     */
    public void setWorkload(Workload workload) {
        this.workload = workload;
    }
    
    @Override
    public String toString(){
        return "{ID: " + id + 
                "|Name: " + name + 
                "|Priority: " + priority + 
                "|Resources: CPU cores = " + this.getCpuValue() + ", memory = " + this.getMemoryValue() + 
                "|Workload: duration(ms) = " + workload.getDuration_ms() + 
                "|Duration: " + this.getDurationTime()/1e9 + 
                "|PID: " + pid + "}";
    }

    /**
     * @return the startTime
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the durationTime
     */
    public long getDurationTime() {
        return durationTime;
    }

    /**
     * @param durationTime the durationTime to set
     */
    public void setDurationTime(long durationTime) {
        this.durationTime = durationTime;
    }

    /**
     * @return the pid
     */
    public long getPid() {
        return pid;
    }

    /**
     * @param pid the pid to set
     */
    public void setPid(long pid) {
        this.pid = pid;
    }
}
