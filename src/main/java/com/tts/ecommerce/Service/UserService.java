package com.tts.ecommerce.Service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tts.ecommerce.Model.Product;
import com.tts.ecommerce.Model.User;
import com.tts.ecommerce.Repository.UserRepository;

@Service
public class UserService implements UserDetailsService
{
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User findByUsername(String username)
    {
        return userRepository.findByUsername(username);
    }

    public void saveNew(User user)
    {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void saveExisting(User user)
    {
        userRepository.save(user);
    }

    public User getLoggedInUser()
    {
        return findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    public void updateCart(Map<Product, Integer> cart)
    {
        User user = getLoggedInUser();
        user.setCart(cart);
        saveExisting(user);
    }
    
    public boolean testPassword(String password)
    {   
        char[] chars = password.toCharArray();
        int x = password.length();
        boolean hasLower = false;
        boolean hasUpper =false;
        boolean hasChar = false;
        boolean hasDigit = false;
//      Set<Character> set = new HashSet<Character>(Arrays.asList('!', '@', '#', '%', '^', '&', 
//          '(', ')', '-', '+'));
        for(char i : chars)
        {
            if(i > 96 && i < 123)
                hasLower = true;
            if(i > 64 && i < 91)
                hasUpper = true;
            if(i > 47 && i < 58 )
                hasDigit = true;
            if((i > 32 && i < 48) || (i > 90 && i < 94))
                hasChar = true;
            
        }
        System.out.println(hasLower + "" + hasUpper + "" + hasDigit + "" + hasChar);
        if(hasLower && hasUpper && hasChar && hasDigit && (x > 8))
        {
            return true;
        }
        else {
            
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        User user = findByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("Username not found.");
        return user;
    }

    

}
