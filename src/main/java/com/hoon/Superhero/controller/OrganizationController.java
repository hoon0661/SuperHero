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
public class OrganizationController {
    
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    OrganizationDao organizationDao;
    
    @Autowired
    SightingDao sightingDao;
    
    Set<ConstraintViolation<Organization>> violationsForAdd = new HashSet<>();
    Set<ConstraintViolation<Organization>> violationsForEdit = new HashSet<>();
    
    private int idForEditError = -1;
    
    @GetMapping("/organizations")
    public String displayOrganizations(Model model){
        model.addAttribute("errors", violationsForAdd);
        List<Organization> organizations = organizationDao.getAllOrganizations();
        model.addAttribute("organizations", organizations);
        return "organizations";
    }
    
    @PostMapping("/addOrganization")
    public String addOrganization(HttpServletRequest request){
        String name = request.getParameter("name");
        String city = request.getParameter("city");
        String description = request.getParameter("description");
        String email = request.getParameter("email");
        
        Organization organization = new Organization();
        organization.setName(name);
        organization.setCity(city);
        organization.setDescription(description);
        organization.setEmail(email);
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violationsForAdd.clear();
        violationsForAdd = validate.validate(organization);
        
        if(violationsForAdd.isEmpty()){
            organizationDao.addOrganization(organization);
        }
        
        return "redirect:/organizations";
    }
    
    @GetMapping("/organizationDetail")
    public String organizationDetail(Integer id, Model model){
        Organization organization = organizationDao.getOrganizationById(id);
        model.addAttribute("organization", organization);
        return "organizationDetail";
    }
    
    @GetMapping("/deleteOrganization")
    public String deleteOrganization(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        organizationDao.deleteOrganizationById(id);
        
        return "redirect:/organizations";
    }
    
    @GetMapping("/editOrganization")
    public String editOrganization(HttpServletRequest request, Model model){
        int id;
        if(idForEditError != -1){
            id = idForEditError;
            idForEditError = -1;
        } else {
            id = Integer.parseInt(request.getParameter("id"));        
        }
        Organization organization = organizationDao.getOrganizationById(id);
        model.addAttribute("organization", organization);
        model.addAttribute("errors", violationsForEdit);
        return "editOrganization";
    }
    
    @PostMapping("/editOrganization")
    public String performEditOrganization(HttpServletRequest request){
        int id = Integer.parseInt(request.getParameter("id"));
        Organization organization = organizationDao.getOrganizationById(id);
        
        organization.setName(request.getParameter("name"));
        organization.setCity(request.getParameter("city"));
        organization.setDescription(request.getParameter("description"));
        organization.setEmail(request.getParameter("email"));
        
        Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
        violationsForEdit = validate.validate(organization);
        
        if(violationsForEdit.isEmpty()){
            organizationDao.updateOrganization(organization);
            return "organizations";
        } else{
            idForEditError = id;
            return "redirect:/editOrganization";
        }
        
        
    }
    
    @GetMapping("/organizationsByHero")
    public String organizationsByHero(Model model){
        List<Hero> heroes = heroDao.getAllHeroes();
        model.addAttribute("heroes", heroes);
        return "organizationsByHero";
    }
    
    @PostMapping("/searchOrganizationsByHero")
    public String searchOrganizationsByHero(HttpServletRequest request, Model model){
        if(heroDao.getAllHeroes().isEmpty()){
            model.addAttribute("error", "There is no hero in the list");
            return "organizationsByHero";
        }
        int heroId = Integer.parseInt(request.getParameter("heroId"));
        List<Organization> organizations = organizationDao.getOrganizationsForHero(heroId);
        model.addAttribute("organizations", organizations);
        if(organizations.isEmpty()){
            model.addAttribute("error", "There is no organization found");
            return "organizationsByHero";
        }
        
//        List<Hero> heroes = heroDao.getAllHeroes();
//        model.addAttribute("heroes", heroes);
        return "organizationsByHero";
    }
    
}
