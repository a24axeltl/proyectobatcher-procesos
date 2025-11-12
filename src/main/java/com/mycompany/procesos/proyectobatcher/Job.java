/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.procesos.proyectobatcher;

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
    
    public Job(){
        id = "";
        name = "";
        priority = 0;
        resources.getCpu_cores();
        resources.getMemory();
        workload.getDuration_ms();
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
                "|Resources: CPU cores = " + resources.getCpu_cores() + ", memory = " + resources.getMemory() + 
                "|Workload: duration(ms) = " + workload.getDuration_ms() + "}";
    }
}
