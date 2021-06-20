/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.Superhero.dto;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Hoon
 */
public class Hero {
    private int id;
    
    @NotBlank(message = "Name must not be empty.")
    @Size(max = 30, message = "Name must be less than 30 characters.")
    private String name;
    
    @NotBlank(message = "Description must not be empty.")
    @Size(max = 200, message = "Description must be less than 200 characters.")
    private String description;
    
    @NotBlank(message = "Super power must not be empty.")
    @Size(max = 50, message = "Super power must be less than 50 characters.")
    private String superPower;
    
   
    @NotBlank(message = "Please choose hero type.")
    private String heroType;
    
    @NotBlank(message = "Please provide url for image")
    private String imgUrl;
    private List<Organization> organizations;
//    private List<Sighting> sightings; 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSuperPower() {
        return superPower;
    }

    public void setSuperPower(String superPower) {
        this.superPower = superPower;
    }

    public String getHeroType() {
        return heroType;
    }

    public void setHeroType(String heroType) {
        this.heroType = heroType;
    }

    public List<Organization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<Organization> organizations) {
        this.organizations = organizations;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Hero{" + "id=" + id + ", name=" + name + ", description=" + description + ", superPower=" + superPower + ", heroType=" + heroType + ", imgUrl=" + imgUrl + ", organizations=" + organizations + '}';
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.id;
        hash = 53 * hash + Objects.hashCode(this.name);
        hash = 53 * hash + Objects.hashCode(this.description);
        hash = 53 * hash + Objects.hashCode(this.superPower);
        hash = 53 * hash + Objects.hashCode(this.heroType);
        hash = 53 * hash + Objects.hashCode(this.imgUrl);
        hash = 53 * hash + Objects.hashCode(this.organizations);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Hero other = (Hero) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.description, other.description)) {
            return false;
        }
        if (!Objects.equals(this.superPower, other.superPower)) {
            return false;
        }
        if (!Objects.equals(this.heroType, other.heroType)) {
            return false;
        }
        if (!Objects.equals(this.imgUrl, other.imgUrl)) {
            return false;
        }
        if (!Objects.equals(this.organizations, other.organizations)) {
            return false;
        }
        return true;
    }

   
    
}
