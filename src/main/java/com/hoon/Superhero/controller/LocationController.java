/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.Superhero.controller;

import com.hoon.Superhero.dao.HeroDao;
import com.hoon.Superhero.dao.LocationDao;
import com.hoon.Superhero.dao.OrganizationDao;
import com.hoon.Superhero.dao.SightingDao;
import com.hoon.Superhero.dto.Location;
import com.hoon.Superhero.dto.Organization;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Hoon
 */
@Controller
public class LocationController {
    
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    OrganizationDao organizationDao;
    
    @Autowired
    SightingDao sightingDao;
    
    Set<ConstraintViolation<Location>> violationsForAdd = new HashSet<>();
    Set<ConstraintViolation<Location>> violationsForEdit = new HashSet<>();
    private int idForEditError = -1;
    
    @GetMapping("/locations")
    public String displayLocations(Model model){
        violationsForEdit.clear();
        model.addAttribute("errors", violationsForAdd);
        List<Location> locations = locationDao.getAllLocations();
        model.addAttribute("locations", locations);
        return "locations";
    }
    
    @PostMapping("/addLocation")
    public String addLocation(HttpServletRequest request){
        String city = request.getParameter("city");
        String latitude = request.getParameter("latitude");
        String longitude = request.getParameter("longitude");
        String description = request.getParameter("description");
        
        Location location = new Location();
        location.setCity(city);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setDescription(description);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violationsForAdd = validate.validate(location);
        
        if(violationsForAdd.isEmpty()){
            locationDao.addLocation(location);
        }
        
        return "redirect:/locations";
    }
    
    @GetMapping("/locationDetail")
    public String locationDetail(Integer id, Model model){
        Location location = locationDao.getLocationById(id);
        model.addAttribute("location", location);
        return "locationDetail";
    }
    
    @GetMapping("/deleteLocation")
    public String deleteLocation(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        locationDao.deleteLocationById(id);
        
        return "redirect:/locations";
    }
    
    @GetMapping("/editLocation")
    public String editLocation(HttpServletRequest request, Model model) {
        int id;
        if(idForEditError != -1){
            id = idForEditError;
            idForEditError = -1;
        } else{
            id = Integer.parseInt(request.getParameter("id"));
        }
        Location location = locationDao.getLocationById(id);
        model.addAttribute("errors", violationsForEdit);
        model.addAttribute("location", location);
        return "editLocation";
    }
    
    @PostMapping("/editLocation")
    public String performEditLocation(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        Location location = locationDao.getLocationById(id);
        
        location.setCity(request.getParameter("city"));
        location.setLatitude(request.getParameter("latitude"));
        location.setLongitude(request.getParameter("longitude"));
        location.setDescription(request.getParameter("description"));
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violationsForEdit = validate.validate(location);

        if(violationsForEdit.isEmpty()){
            locationDao.updateLocation(location);
            return "locations";
        } else{
            idForEditError = id;
            return "redirect:/editLocation";
        }        
                
        
    }
    
    @GetMapping("/locationsByHero")
    public String locationsByHero(){
        return "locationsByHero";
    }
    
    @PostMapping("/searchLocationsByHero")
    public String searchLocationsByHero(HttpServletRequest request, Model model){
        String heroName = request.getParameter("name");
        
        if(heroName.isEmpty()){
            model.addAttribute("error", "Please type in hero");
        } else {
            List<Location> locations = locationDao.getLocationForHero(heroName);
            if(locations.isEmpty()){
                model.addAttribute("error", "Cannot find any location by " + heroName);
                return "locationsByHero";
            }
            model.addAttribute("locations", locations);
            
        }
        return "locationsByHero";
    }
}
