/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package NCACST376;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.*;
/**
 *
 * @author MLSSD
 */
public class UserList<AnyType> implements Iterable{
    
    private UserNode<AnyType> head;
    
    public UserList()
    {
        head = null;
    }
    //is empty?
    public boolean isEmpty()
    {
        return head == null;
    }
    //add a user
    public void add(User u)
    {
        if (head == null)
        {
            addFirst(u);
        }
        else
        {
            UserNode<AnyType> tmp = head;
            while (tmp.NextUser != null)
            {
                tmp = tmp.NextUser;
            }
            tmp.NextUser = new UserNode(u, null);
        }
    }
    
    //add a user to the start of the list
    public void addFirst(User u)
    {
        head = new UserNode(u, head);
    }
    //get first user
    public User getFirstUser()
    {
        if(head == null) throw new NoSuchElementException();
        
        return head.user;
    }
    //remove the first user
    public User removeFirst()
    {
        User tmp = getFirstUser();
        head = head.NextUser;
        return tmp;
    }
    //remove a certain event (in string) from the list
    public User remove(String s)
    {
        User e = this.getUser(s);
        return remove(e);
    }
    //remove user
    public User remove(User u)
    {
        UserNode<AnyType> tmp = head;
        UserNode<AnyType> ahead = head;
        if (ahead.user.equals(u))
        {
            return removeFirst();
        }
        ahead = ahead.NextUser;
        while (!ahead.user.equals(u) && ahead != null)
        {
            tmp = tmp.NextUser;
            ahead = ahead.NextUser;
        }
        //got to the end and didnt find it
        if (ahead == null )
        {
            throw new NoSuchElementException();
        }
        else{
            //Event<AnyType> tmpRemove = tmp.NextEvent;
            if (ahead.NextUser == null)
            {
                tmp.NextUser = null;
                User n = ahead.user;
                ahead = null;
                return n;
            }
            else{
                tmp.NextUser = ahead.NextUser;
                User n = ahead.user;
                ahead = null;
                return n;
            }
        }
    }
    //does the list contain the user
    public boolean contains(User x)
    {
        for (Object tmp : this)
        {
            if (tmp.equals(x))
            {
                return true;
            }
        }
        return false;
    }
    //does the list contain the user (in string)
    public boolean contains(String s)
    {
        ArrayList<User> allUsers = new ArrayList<User>();
        allUsers = this.getAllUsers();
        for (User e : allUsers)
        {
            if (e.getName().equals(s))
            {
                return true;
            }
        }
        return false;
    }
    public User getUser(String key)
    {
        {
            UserNode<AnyType> tmp = head;
            User e = (User)tmp.getUser();
            String s = e.getName();
            while(tmp != null && !key.equals(s))
            {
                tmp = tmp.NextUser; //this is the node
                if (tmp != null){
                    e = (User)tmp.getUser(); //this is the User inside the node
                    s = e.getName(); //name of the User
                }

            }
            if (tmp != null)
            {
                return e;
            }
            else
            {
                throw new NoSuchElementException();
            }
        }
    }
    
    public ArrayList<User> getAllUsers()
    {
        UserNode<AnyType> tmp = head;
        ArrayList<User> allUsers = new ArrayList<User>();
        while(tmp != null)
        {
            allUsers.add(tmp.user);
            tmp = tmp.NextUser;
        }
        return allUsers;
    }
    
    
    private static class UserNode<AnyType>
    {
        private User user;
        private UserNode<AnyType> NextUser;
        
        public UserNode(User u, UserNode<AnyType> nextUser)
        {
            this.user = u;
            this.NextUser = nextUser;
            
        }
        public User getUser()
        {
            return this.user;
        }
    }
    @Override
    public Iterator<User> iterator()
    {
        return new UserListIterator();
    }
    private class UserListIterator implements Iterator<User>
    {
        private UserNode<AnyType> Next;
        
        public UserListIterator()
        {
            Next = head;
        }
        @Override
        public boolean hasNext()
        {
            return Next != null;
        }
        @Override
        public User next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            User r = Next.user;
            Next = Next.NextUser;
            return r;
        }
        @Override
        public void remove()
        {
            throw new UnsupportedOperationException();
        }
    }
      
}
