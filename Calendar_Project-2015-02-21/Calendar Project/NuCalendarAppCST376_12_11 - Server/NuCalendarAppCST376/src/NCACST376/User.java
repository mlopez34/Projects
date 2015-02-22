/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NCACST376;

/**
 *
 * @author MLSSD
 */
public class User {
    
    private String Name;
    private String Pass;
    private boolean Admin;
    
    public User(String name, String pass, boolean admin)
    {
        this.Name = name;
        this.Pass = pass;
        this.Admin = admin;
    }

    public void setAdmin(boolean Admin) {
        this.Admin = Admin;
    }

    public String getPass() {
        return Pass;
    }

    public boolean isAdmin() {
        return Admin;
    }
    
    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public void setPass(String Pass) {
        this.Pass = Pass;
    }
    @Override
    public String toString()
    {
        return this.Name;
    }
    
    
}
