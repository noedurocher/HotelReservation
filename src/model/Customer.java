package model;

import java.util.regex.Pattern;

public class Customer {
    public String firstName;
    public String lastName;
    public String email;

    private final String emailRegEx = "^(.+)@(.+).com$";
    private final Pattern pattern = Pattern.compile(emailRegEx);

    public Customer(String firstName, String lastName, String email){
        if(!pattern.matcher(email).matches()){
            throw new IllegalArgumentException("Error, Invalid email");
        }
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;

    }

    public String getEmail() {
        return this.email;
    }

    @Override
    public String toString(){
        return "First Name: " + this.firstName + "\nLast Name: " + this.lastName +
                "\nEmail: " + this.email;
    }

}
