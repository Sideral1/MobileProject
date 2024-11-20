package com.example.finalprojectaccountpage.firebasestuff;

public class UserModal {

    private static UserModal currentUser = new UserModal(null,null,null,null,null);

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String profilePicture;

    public UserModal(){}

    public UserModal(String firstName, String lastName, String email, String password, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    // Getting current user
    public static UserModal getCurrentUser()
    {
        return UserModal.currentUser;
    }

    public static void setCurrentUser(UserModal currentUser) {
        UserModal.currentUser = currentUser;
    }
}
