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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.Before;
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
public class SightingDaoDBTest {
    
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    SightingDao sightingDao;
    
    public SightingDaoDBTest() {
    }
    
   
    @BeforeEach
    public void setUp() {
        List<Location> locations = locationDao.getAllLocations();
        for(Location location : locations){
            locationDao.deleteLocationById(location.getId());
        }
        
        List<Organization> organizations = organizationDao.getAllOrganizations();
        for(Organization organization : organizations){
            organizationDao.deleteOrganizationById(organization.getId());
        }
        
        List<Hero> heroes = heroDao.getAllHeroes();
        for(Hero hero : heroes) {
            heroDao.deleteHeroById(hero.getId());
        }
        
        List<Sighting> sightings = sightingDao.getAllSightings();
        for(Sighting sighting : sightings){
            sightingDao.deleteSightingById(sighting.getId());
        }
    }
    

    /**
     * Test of getSightingById method, of class SightingDaoDB.
     */
    @Test
    public void testAddAndGetSightingById() {
       
        Location location = new Location();
        location.setDescription("test description");
        location.setLatitude("test latitude");
        location.setLongitude("test longitude");
        location.setCity("test city");
        
        location = locationDao.addLocation(location);
        
        Organization organization = new Organization();
        organization.setDescription("test description");
        organization.setName("test name");
        organization.setEmail("test email");
        organization.setCity("test city");
        
        organization = organizationDao.addOrganization(organization);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        
        Hero hero = new Hero();
        hero.setName("test name");
        hero.setHeroType("test type");
        hero.setSuperPower("test power");
        hero.setDescription("test description");
        hero.setOrganizations(organizations);
        
        hero = heroDao.addHero(hero);
        
        Sighting sighting = new Sighting();
        sighting.setHero(hero);
        sighting.setLocation(location);
        sighting.setDate(LocalDate.now());
        
        sighting = sightingDao.addSighting(sighting);
        Sighting fromDao = sightingDao.getSightingById(sighting.getId());
        
        assertEquals(sighting, fromDao);
        
    }

    /**
     * Test of getAllSightings method, of class SightingDaoDB.
     */
    @Test
    public void testGetAllSightings() {
        Location location = new Location();
        location.setDescription("test description");
        location.setLatitude("test latitude");
        location.setLongitude("test longitude");
        location.setCity("test city");
        
        location = locationDao.addLocation(location);
        
        Organization organization = new Organization();
        organization.setDescription("test description");
        organization.setName("test name");
        organization.setEmail("test email");
        organization.setCity("test city");
        
        organization = organizationDao.addOrganization(organization);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        
        Hero hero = new Hero();
        hero.setName("test name");
        hero.setHeroType("test type");
        hero.setSuperPower("test power");
        hero.setDescription("test description");
        hero.setOrganizations(organizations);
        
        hero = heroDao.addHero(hero);
        
        Sighting sighting = new Sighting();
        sighting.setHero(hero);
        sighting.setLocation(location);
        sighting.setDate(LocalDate.now());
        
        sighting = sightingDao.addSighting(sighting);
        
        List<Sighting> sightings = sightingDao.getAllSightings();
        assertEquals(sightings.size(), 1);
        assertTrue(sightings.contains(sighting));
    }

    @Test
    public void testUpdateSighting() {
        Location location = new Location();
        location.setDescription("test description");
        location.setLatitude("test latitude");
        location.setLongitude("test longitude");
        location.setCity("test city");
        
        location = locationDao.addLocation(location);
        
        Organization organization = new Organization();
        organization.setDescription("test description");
        organization.setName("test name");
        organization.setEmail("test email");
        organization.setCity("test city");
        
        organization = organizationDao.addOrganization(organization);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        
        Hero hero = new Hero();
        hero.setName("test name");
        hero.setHeroType("test type");
        hero.setSuperPower("test power");
        hero.setDescription("test description");
        hero.setOrganizations(organizations);
        
        hero = heroDao.addHero(hero);
        
        
        
        Sighting sighting = new Sighting();
        sighting.setHero(hero);
        sighting.setLocation(location);
        sighting.setDate(LocalDate.now());
        
        sighting = sightingDao.addSighting(sighting);
        Sighting fromDao = sightingDao.getSightingById(sighting.getId());
        assertEquals(sighting, fromDao);
        
        Hero hero2 = new Hero();
        hero2.setName("test name2");
        hero2.setHeroType("test type2");
        hero2.setSuperPower("test power2");
        hero2.setDescription("test description2");
        hero2.setOrganizations(organizations);
        
        hero2 = heroDao.addHero(hero2);
        
        sighting.setHero(hero2);
        assertNotEquals(fromDao, sighting);
        
        sightingDao.updateSighting(sighting);
        fromDao = sightingDao.getSightingById(sighting.getId());
        
        assertEquals(sighting, fromDao);
        
    }

    /**
     * Test of deleteSightingById method, of class SightingDaoDB.
     */
    @Test
    public void testDeleteSightingById() {
        
        Location location = new Location();
        location.setDescription("test description");
        location.setLatitude("test latitude");
        location.setLongitude("test longitude");
        location.setCity("test city");
        
        location = locationDao.addLocation(location);
        
        Organization organization = new Organization();
        organization.setDescription("test description");
        organization.setName("test name");
        organization.setEmail("test email");
        organization.setCity("test city");
        
        organization = organizationDao.addOrganization(organization);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        
        Hero hero = new Hero();
        hero.setName("test name");
        hero.setHeroType("test type");
        hero.setSuperPower("test power");
        hero.setDescription("test description");
        hero.setOrganizations(organizations);
        
        hero = heroDao.addHero(hero);
        
        Sighting sighting = new Sighting();
        sighting.setHero(hero);
        sighting.setLocation(location);
        sighting.setDate(LocalDate.now());
        
        sighting = sightingDao.addSighting(sighting);
        List<Sighting> sightings = sightingDao.getAllSightings();
        assertEquals(sightings.size(), 1);
        
        sightingDao.deleteSightingById(sighting.getId());
        sightings = sightingDao.getAllSightings();
        
        assertEquals(sightings.size(), 0);
        
        
        
    }

    /**
     * Test of getSightingsForDate method, of class SightingDaoDB.
     */
    @Test
    public void testGetSightingsForDate() {
        Location location = new Location();
        location.setDescription("test description");
        location.setLatitude("test latitude");
        location.setLongitude("test longitude");
        location.setCity("test city");
        
        location = locationDao.addLocation(location);
        
        Organization organization = new Organization();
        organization.setDescription("test description");
        organization.setName("test name");
        organization.setEmail("test email");
        organization.setCity("test city");
        
        organization = organizationDao.addOrganization(organization);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization);
        
        Hero hero = new Hero();
        hero.setName("test name");
        hero.setHeroType("test type");
        hero.setSuperPower("test power");
        hero.setDescription("test description");
        hero.setOrganizations(organizations);
        
        hero = heroDao.addHero(hero);
        
        Sighting sighting = new Sighting();
        sighting.setHero(hero);
        sighting.setLocation(location);
        sighting.setDate(LocalDate.now());
        
        sighting = sightingDao.addSighting(sighting);
        List<Sighting> sightings = sightingDao.getSightingsForDate(LocalDate.now());
        
        assertEquals(sightings.get(0).getDate(), sighting.getDate());
    }
    
}
