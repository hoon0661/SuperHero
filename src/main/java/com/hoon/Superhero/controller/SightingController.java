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
import com.hoon.Superhero.dto.Hero;
import com.hoon.Superhero.dto.Location;
import com.hoon.Superhero.dto.Sighting;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 *
 * @author Hoon
 */
@Controller
public class SightingController {
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    OrganizationDao organizationDao;
    
    @Autowired
    SightingDao sightingDao;
    
    
    @GetMapping("/sightings")
    public String displaySightings(Model model){
        List<Hero> heroes = heroDao.getAllHeroes();
        List<Location> locations = locationDao.getAllLocations();
        List<Sighting> sightings = sightingDao.getAllSightings();
        
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        model.addAttribute("sightings", sightings);
                
        return "sightings";
        
    }
    
    @PostMapping("/addSighting")
    public String addSighting(HttpServletRequest request, Model model){
        String heroId = request.getParameter("heroId");
        String locationId = request.getParameter("locationId");
        String date = request.getParameter("date");
        
        Sighting sighting = new Sighting();
        sighting.setHero(heroDao.getHeroById(Integer.parseInt(heroId)));
        sighting.setLocation(locationDao.getLocationById(Integer.parseInt(locationId)));
        
         if(heroId == null || heroId.length() == 0){
            model.addAttribute("heroError", "Please choose hero");
        }
        
        if(locationId == null || locationId.length() == 0){
            model.addAttribute("locationError", "Please choose location");
        }
        
        //Need to figure out how to display current data in database
        if(date.length() != 10){
            model.addAttribute("sighting", sighting);
            model.addAttribute("sightings", sightingDao.getAllSightings());
            model.addAttribute("heroes", heroDao.getAllHeroes());
            model.addAttribute("dateError", "Please choose date");
            model.addAttribute("locations", locationDao.getAllLocations());
            return "sightings";
        }
        
        LocalDate localDate = LocalDate.parse(date);
        
        if(localDate.isAfter(LocalDate.now())){
            model.addAttribute("sighting", sighting);
            model.addAttribute("sightings", sightingDao.getAllSightings());
            model.addAttribute("heroes", heroDao.getAllHeroes());
            model.addAttribute("dateError", "Date cannot be future date");
            model.addAttribute("locations", locationDao.getAllLocations());
            return "sightings";
        }
        
        sighting.setDate(localDate);
        sightingDao.addSighting(sighting);
        return "redirect:/sightings";
    }
    
    @GetMapping("/sightingDetail")
    public String sightingDetail(Integer id, Model model){
        Sighting sighting = sightingDao.getSightingById(id);
        model.addAttribute("sighting", sighting);
        return "sightingDetail";
    }
    
    @GetMapping("/sightingsByDate")
    public String sightingsByDate(){
        return "sightingsByDate";
    }
    
    @PostMapping("/searchSightingsByDate")
    public String searchSightingsByDate(HttpServletRequest request, Model model){
        if(request.getParameter("date").equals("")){
            model.addAttribute("error", "Please select date");
            return "sightingsByDate";
        }
        LocalDate date = LocalDate.parse(request.getParameter("date"));
        List<Sighting> sightings = sightingDao.getSightingsForDate(date);
        if(sightings.isEmpty()){
            model.addAttribute("error", "Cannot find any sighting on " + date.toString());
            return "sightingsByDate";
        }
        model.addAttribute("sightings", sightings); 
        return "sightingsByDate";
    }
    
    @GetMapping("/deleteSighting")
    public String deleteSighting(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        sightingDao.deleteSightingById(id);
        
        return "redirect:/sightings";
    }
    
    @GetMapping("/editSighting")
    public String editSighting(Integer id, Model model){
        Sighting sighting = sightingDao.getSightingById(id);
        List<Hero> heroes = heroDao.getAllHeroes();
        List<Location> locations = locationDao.getAllLocations();
        
        model.addAttribute("sighting", sighting);
        model.addAttribute("heroes", heroes);
        model.addAttribute("locations", locations);
        
        return "editSighting";
    }
    
    
    //Need to try with constraint validation
    @PostMapping("/editSighting")
    public String performEditSighting(HttpServletRequest request, Model model){

        String heroId = request.getParameter("heroId");
        String locationId = request.getParameter("locationId");
        String date = request.getParameter("date");
        
        Sighting sighting = sightingDao.getSightingById(Integer.parseInt(request.getParameter("id")));
        sighting.setHero(heroDao.getHeroById(Integer.parseInt(heroId)));
        sighting.setLocation(locationDao.getLocationById(Integer.parseInt(locationId)));
        
        if(heroId == null || heroId.length() == 0){
            model.addAttribute("heroError", "Please choose hero");
        }
        
        if(locationId == null || locationId.length() == 0){
            model.addAttribute("locationError", "Please choose location");
        }
        
        if(date.length() != 10){
            model.addAttribute("sighting", sighting);
            model.addAttribute("heroes", heroDao.getAllHeroes());
            model.addAttribute("dateError", "Please choose date");
            model.addAttribute("locations", locationDao.getAllLocations());
            return "editSighting";
        }
        
        LocalDate localDate = LocalDate.parse(date);
        
        if(localDate.isAfter(LocalDate.now())){
            model.addAttribute("sighting", sighting);
            model.addAttribute("sightings", sightingDao.getAllSightings());
            model.addAttribute("heroes", heroDao.getAllHeroes());
            model.addAttribute("dateError", "Date cannot be future date");
            model.addAttribute("locations", locationDao.getAllLocations());
            return "sightings";
        }
        
        sighting.setDate(localDate);

        sightingDao.updateSighting(sighting);

        return "redirect:/sightings";
    }
    
    @GetMapping("/home")
    public String getSightingsForHome(Model model){

        List<Sighting> sightings = sightingDao.getAllSightings();
        if(sightings.isEmpty()){
            return "home";
        }
        sightings.sort((o1,o2) -> o1.getDate().compareTo(o2.getDate()));
        List<Sighting> latestTenSightings = new ArrayList<>();
        if(sightings.size() >= 10){
            for(int i = sightings.size() - 1; i >= sightings.size() - 10; i--){
                latestTenSightings.add(sightings.get(i));
            }
            
        } 
        else {
            for(int i = sightings.size() -1; i >= 0; i--){
                latestTenSightings.add(sightings.get(i));
            }
        }

        List<List<Sighting>> sightingForEachRow = new ArrayList<>();
        int i = 0;
        for(int count = 0; count < latestTenSightings.size() / 3 + 1; count++){         
            List<Sighting> list = new ArrayList<>();         
            while(i < latestTenSightings.size()){
                list.add(latestTenSightings.get(i));
                i++;
                if(i % 3 == 0 && i != 0){
                    break;
                }
            }
            if(!list.isEmpty()){
                sightingForEachRow.add(list);   
            }
        }
        
        model.addAttribute("sightings", sightingForEachRow);
        return "home";
    }
}
