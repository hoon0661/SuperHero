/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.Superhero.dao;

import com.hoon.Superhero.dto.Hero;
import com.hoon.Superhero.dto.Location;
import com.hoon.Superhero.dto.Organization;
import com.hoon.Superhero.dto.Sighting;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;

/**
 *
 * @author Hoon
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrganizationDaoDBTest {
    
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    SightingDao sightingDao;
    
    public OrganizationDaoDBTest() {
    }
    
    @BeforeEach
    public void setUp() {
        List<Hero> heroes = heroDao.getAllHeroes();
        for(Hero hero : heroes) {
            heroDao.deleteHeroById(hero.getId());
        }
        
        List<Location> locations = locationDao.getAllLocations();
        for(Location location : locations){
            locationDao.deleteLocationById(location.getId());
        }
        
        List<Organization> organizations = organizationDao.getAllOrganizations();
        for(Organization organization : organizations){
            organizationDao.deleteOrganizationById(organization.getId());
        }
        
        List<Sighting> sightings = sightingDao.getAllSightings();
        for(Sighting sighting : sightings){
            sightingDao.deleteSightingById(sighting.getId());
        }
    }
    
    @Test
    public void testAddAndGetOrganization(){
        Organization organization = new Organization();
        organization.setDescription("test description");
        organization.setName("test name");
        organization.setEmail("test email");
        organization.setCity("test city");
        
        organization = organizationDao.addOrganization(organization);
        
        Organization fromDao = organizationDao.getOrganizationById(organization.getId());
        
        assertEquals(organization, fromDao);
    }
    
    @Test
    public void testGetAllOrganizations(){
        Organization organization1 = new Organization();
        organization1.setDescription("test description1");
        organization1.setName("test name1");
        organization1.setEmail("test email1");
        organization1.setCity("test city1");
        
        organization1 = organizationDao.addOrganization(organization1);
        
        Organization organization2 = new Organization();
        organization2.setDescription("test description2");
        organization2.setName("test name2");
        organization2.setEmail("test email2");
        organization2.setCity("test city2");
        
        organization2 = organizationDao.addOrganization(organization2);
        
        List<Organization> organizations = organizationDao.getAllOrganizations();
        
        assertEquals(2,organizations.size());
        assertTrue(organizations.contains(organization1));
        assertTrue(organizations.contains(organization2));
        
    }
    
    @Test
    public void testUpdateOrganization(){
        Organization organization1 = new Organization();
        organization1.setDescription("test description1");
        organization1.setName("test name1");
        organization1.setEmail("test email1");
        organization1.setCity("test city1");
        
        organization1 = organizationDao.addOrganization(organization1);
        
        Organization fromDao = organizationDao.getOrganizationById(organization1.getId());
        assertEquals(organization1, fromDao);
        
        organization1.setCity("test city2");
        organizationDao.updateOrganization(organization1);
        
        assertNotEquals(organization1, fromDao);
        
        fromDao = organizationDao.getOrganizationById(organization1.getId());
        
        assertEquals(organization1, fromDao);
        
    }
    
    @Test
    public void testDeleteOrganization(){
        Organization organization1 = new Organization();
        organization1.setDescription("test description1");
        organization1.setName("test name1");
        organization1.setEmail("test email");
        organization1.setCity("test city1");
        
        organization1 = organizationDao.addOrganization(organization1);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization1);
        
        
        Hero hero = new Hero();
        hero.setName("test name");
        hero.setHeroType("test type");
        hero.setSuperPower("test power");
        hero.setDescription("test description");
        hero.setOrganizations(organizations);
        heroDao.addHero(hero);
        
        hero = heroDao.addHero(hero);
        
        Organization fromDao = organizationDao.getOrganizationById(organization1.getId());
        assertEquals(organization1, fromDao);
        
        organizationDao.deleteOrganizationById(organization1.getId());
        fromDao = organizationDao.getOrganizationById(organization1.getId());
        assertNull(fromDao);
        
    }
    
}
