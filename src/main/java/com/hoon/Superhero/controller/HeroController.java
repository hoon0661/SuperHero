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
import com.hoon.Superhero.dto.Organization;
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
public class HeroController {
    
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    OrganizationDao organizationDao;
    
    @Autowired
    SightingDao sightingDao;
    
    
    
    @GetMapping("/heroes")
    public String displayHeroes(Model model){
        Hero hero = new Hero();
        model.addAttribute("hero", hero);
        List<Organization> organizations = organizationDao.getAllOrganizations();
        List<Hero> heroes = heroDao.getAllHeroes();
        model.addAttribute("organizations", organizations);
        model.addAttribute("heroes", heroes);
        return "heroes";
    }
    
    @PostMapping("/addHero")
    public String addHero(@Valid Hero hero, BindingResult result, HttpServletRequest request, Model model){
        
        String[] organizationIds = request.getParameterValues("organizationId");
        
        List<Organization> organizations = new ArrayList<>();
        
        if(organizationIds != null){
            
            for(String organizationId : organizationIds){
                organizations.add(organizationDao.getOrganizationById(Integer.parseInt(organizationId)));
            }
        }
        
        else{
            FieldError error = new FieldError("hero", "organizations", "Must include one organization");
            result.addError(error);
        }
        hero.setOrganizations(organizations);
        
        if(result.hasErrors()){
            model.addAttribute("organizations", organizationDao.getAllOrganizations());
            model.addAttribute("hero", hero);
            
            List<Hero> heroes = heroDao.getAllHeroes();
            model.addAttribute("heroes", heroes);
            return "heroes";
        }
        
        heroDao.addHero(hero);
        
        return "redirect:/heroes";
    }
    
    @GetMapping("/heroDetail")
    public String heroDetail(Integer id, Model model){
        Hero hero = heroDao.getHeroById(id);
        model.addAttribute("hero", hero);
        return "heroDetail";
    }
    
    @GetMapping("/deleteHero")
    public String deleteHero(Integer id){
        heroDao.deleteHeroById(id);
        return "redirect:/heroes";
    }
    
    @GetMapping("/editHero")
    public String editHero(Integer id, Model model){
        Hero hero = heroDao.getHeroById(id);
        List<Organization> organizations = organizationDao.getAllOrganizations();
        
        model.addAttribute("hero", hero);
        model.addAttribute("organizations", organizations);
        return "editHero";
    }
    
    @PostMapping("/editHero")
    public String performEditHero(@Valid Hero hero, BindingResult result, HttpServletRequest request, Model model){
        String[] organizationIds = request.getParameterValues("organizationId");
        
        List<Organization> organizations = new ArrayList<>();
        
        if(organizationIds != null){
            
            for(String organizationId : organizationIds){
                organizations.add(organizationDao.getOrganizationById(Integer.parseInt(organizationId)));
            }
        } else {
            FieldError error = new FieldError("hero", "organizations", "Must include one organization");
            result.addError(error);
                 
        }
        hero.setOrganizations(organizations);
        
        
        if(result.hasErrors()){
            model.addAttribute("organizations", organizationDao.getAllOrganizations());
            model.addAttribute("hero", hero);
            return "editHero";
        }
        heroDao.updateHero(hero);
        
        return "redirect:/heroes";
    }
    
    @GetMapping("/heroesByLocation")
    public String heroByLocation(){
        return "heroesByLocation";
    }
    
    @PostMapping("/searchHeroesByLocation")
    public String searchHeroesByLocation(HttpServletRequest request, Model model){
        String location = request.getParameter("name");
        
        if(location.isEmpty()){
            model.addAttribute("error", "Please type in location");
        } else {
            
            List<Hero> heroes = heroDao.getHeroesForLocation(location);
            if(heroes.isEmpty()){
                model.addAttribute("error", "Cannot find any hero by " + location);
                return "heroesByLocation";
            }
            model.addAttribute("heroes", heroes);
        }
        return "heroesByLocation";
        
    }
    
    @GetMapping("/heroesByOrganization")
    public String heroesByOrganization(Model model){
        List<Organization> organizations = organizationDao.getAllOrganizations();
        model.addAttribute("organizations", organizations);
        return "heroesByOrganization";
    }
    
    @PostMapping("/searchHeroesByOrganization")
    public String searchHeroesByOrganization(HttpServletRequest request, Model model){
        if(organizationDao.getAllOrganizations().isEmpty()){
            model.addAttribute("error", "There is no organization in the list");
            return "heroesByOrganization";
        }
        int organizationId = Integer.parseInt(request.getParameter("organizationId"));

        List<Hero> heroes = heroDao.getHeroesForOrganization(organizationId);
        if(heroes.isEmpty()){
            model.addAttribute("error", "There is no hero in " + organizationDao.getOrganizationById(organizationId).getName());
            return "heroesByOrganization";
        }
        model.addAttribute("heroes", heroes);
            
        
        return "heroesByOrganization";
    }
}
