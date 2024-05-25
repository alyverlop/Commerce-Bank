package com.Commerceapp.app.rest;
 
import java.util.Collection;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

 
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
 
public class CustomUserDetails implements UserDetails {
 
    private User user;
     
    public CustomUserDetails(User user) {
        this.user = user;
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
 
    @Override
    public String getPassword() {
        return user.getPassword();
    }
 
    @Override
    public String getUsername() {
        return user.getEmail();
    }
 
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
 
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
 
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
 
    @Override
    public boolean isEnabled() {
        return true;
    }
     
    public String getFullName() {
        return user.getFirstName() + " " + user.getLastName();
    }
    
    public String getaddress() {
        return user.getAddress();
          
    }
    
    public String getbirth() {
        return user.getDob();       
          
    }
    
    public String getaccnum() {
        return user.getAccountNum();
          
    }
    
    public String getuser_email() {
        return user.getEmail();
    }
    
    public String getbalanceview() {
        double balance = user.getBalance();
        
        BigDecimal formattedBalance = BigDecimal.valueOf(balance);
        
        formattedBalance = formattedBalance.setScale(2, RoundingMode.HALF_UP);
        
        return formattedBalance.toString();
    }


    
 
}