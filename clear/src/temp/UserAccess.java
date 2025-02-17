package com.restaurant.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "User_access")
public class UserAccess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userAccessId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_access_user"))
    private User user;

    @ManyToOne
    @JoinColumn(name = "access_id", nullable = false, foreignKey = @ForeignKey(name = "fk_user_access_access"))
    private AccessType accessType;

    // Getters and Setters
    public Long getUserAccessId() {
        return userAccessId;
    }

    public void setUserAccessId(Long userAccessId) {
        this.userAccessId = userAccessId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

}