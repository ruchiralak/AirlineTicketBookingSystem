/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ASUS
 */
public class User {
     private String userId;
     private String username;
     private String email;
     private String password;
     private String role;
     private String status;
     
     public User() {}
     
     public User(String username, String email, String password) {
      
      this.username = username;
      this.email = email;
      this.password = password;
      this.role = "Customer"; // default role
      this.status = "Active"; // default status
}

     //for customer    
     public User(String userId,String username, String email, String password,String role) {
      this.userId =userId;
      this.username = username;
      this.email = email;
      this.password = password;
      this.role = role; // default role
      this.status = "Active"; // default status
}

     //for admin
    public User(String userId, String username, String email, String password, String role, String status) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getStatus() {
        return status;
    }

   //Setteres

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
     
     
}
