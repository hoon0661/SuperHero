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
public class LocationDaoDBTest {
    
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    OrganizationDao organizationDao;

    @Autowired
    SightingDao sightingDao;
    
    public LocationDaoDBTest() {
    }
    
    
    @BeforeEach
    public void setUp() {
        
        List<Location> locations = locationDao.getAllLocations();
        for(Location location : locations){
            locationDao.deleteLocationById(location.getId());
        }
        
        List<Hero> heroes = heroDao.getAllHeroes();
        for(Hero hero : heroes) {
            heroDao.deleteHeroById(hero.getId());
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


    /**
     * Test of getLocationById method, of class LocationDaoDB.
     */
    @Test
    public void testAddAndGetLocationById() {
        
        Location location = new Location();
        location.setDescription("test description");
        location.setLatitude("test latitude");
        location.setLongitude("test longitude");
        location.setCity("test city");
        
        location = locationDao.addLocation(location);
        
        Location fromDao = locationDao.getLocationById(location.getId());
        
        assertEquals(location, fromDao);
    }

    /**
     * Test of getAllLocations method, of class LocationDaoDB.
     */
    @Test
    public void testGetAllLocations() {
        
        Location location1 = new Location();
        location1.setLatitude("test latitude");
        location1.setLongitude("test longitude");
        location1.setCity("test city");
        location1.setDescription("test description");
        
        location1 = locationDao.addLocation(location1);
        
        Location location2 = new Location();
        location2.setLatitude("test latitude");
        location2.setLongitude("test longitude");
        location2.setCity("test city");
        location2.setDescription("test description");
        
        location2 = locationDao.addLocation(location2);
        
        List<Location> locations = locationDao.getAllLocations();
        
        assertEquals(2,locations.size());
        assertTrue(locations.contains(location1));
        assertTrue(locations.contains(location2));
    }

    /**
     * Test of updateLocation method, of class LocationDaoDB.
     */
    @Test
    public void testUpdateLocation() {
        Location location1 = new Location();
        location1.setDescription("test description1");
        location1.setLatitude("test latitude1");
        location1.setLongitude("test longitude1");
        location1.setCity("test city1");
        
        location1 = locationDao.addLocation(location1);
        
        Location fromDao = locationDao.getLocationById(location1.getId());
        assertEquals(location1, fromDao);
        
        location1.setCity("test city2");
        locationDao.updateLocation(location1);
        
        assertNotEquals(location1, fromDao);
        
        fromDao = locationDao.getLocationById(location1.getId());
        
        assertEquals(location1, fromDao);
        
    }

    /**
     * Test of deleteLocationById method, of class LocationDaoDB.
     */
    @Test
    public void testDeleteLocationById() {
        Location location = new Location();
        location.setDescription("test description");
        location.setLatitude("test latitude");
        location.setLongitude("test longitude");
        location.setCity("test city");
        
        location = locationDao.addLocation(location); 
        Location fromDao = locationDao.getLocationById(location.getId());
        assertEquals(location, fromDao);

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
        hero = heroDao.addHero(hero);
        
        Sighting sighting = new Sighting();
        sighting.setDate(LocalDate.now());
        sighting.setLocation(location);
        sighting.setHero(hero);
        
        sighting = sightingDao.addSighting(sighting);
        
        locationDao.deleteLocationById(location.getId());
        
        fromDao = locationDao.getLocationById(location.getId());
        assertNull(fromDao);
        
    }

    /**
     * Test of getLocationForHero method, of class LocationDaoDB.
     */
    @Test
    public void testGetLocationForHero() {
        
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
        
        List<Location> locations = locationDao.getLocationForHero(hero.getName());
        
        assertEquals(locations.size(), 1);
        assertTrue(locations.contains(location));
    }
    
}
