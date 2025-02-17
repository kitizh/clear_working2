package com.restaurant.entities;

import com.restaurant.StringArrayConverter;
import jakarta.persistence.*;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "Access_types")
public class AccessType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accessId;

    @Column(nullable = false)
    private String description;

    @Column(name = "tabs_list", columnDefinition = "TEXT[]")
    @Convert(converter = StringArrayConverter.class)
    private String[] tabsList;

    // Getters and Setters
    public Long getAccessId() {
        return accessId;
    }

    public void setAccessId(Long accessId) {
        this.accessId = accessId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getTabsList() {
        return tabsList;
    }

    public void setTabsList(String[] tabsList) {
        this.tabsList = tabsList;
    }

    public List<String> getTabsListAsList() {
        return Arrays.asList(tabsList);
    }
}
