/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */
public class Plane {
     private String planeId;
     private String ModelName;
     private String CapacityClass;
     
     public Plane(String ModelName, String CapacityClass){
                   this.ModelName =ModelName;
                   this.CapacityClass=CapacityClass;
     }

    public Plane(String planeId, String ModelName, String CapacityClass) {
        this.planeId = planeId;
        this.ModelName = ModelName;
        this.CapacityClass = CapacityClass;
    }

    public String getPlaneId() {
        return planeId;
    }

    public void setPlaneId(String planeId) {
        this.planeId = planeId;
    }

    public String getModelName() {
        return ModelName;
    }

    public void setModelName(String ModelName) {
        this.ModelName = ModelName;
    }

    public String getCapacityClass() {
        return CapacityClass;
    }

    public void setCapacityClass(String CapacityClass) {
        this.CapacityClass = CapacityClass;
    }
     
     
    
}
