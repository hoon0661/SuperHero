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
public class HeroDaoDBTest {
    
    @Autowired
    HeroDao heroDao;
    
    @Autowired
    LocationDao locationDao;
    
    @Autowired
    OrganizationDao organizationDao;
    
    @Autowired
    SightingDao sightingDao;
    
    public HeroDaoDBTest() {
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

    /**
     * Test of getHeroById method, of class HeroDaoDB.
     */
    @Test
    public void testAddAndGetHeroById() {
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
        
        Hero fromDao = heroDao.getHeroById(hero.getId());
        
        System.out.println(hero.toString());
        System.out.println(fromDao.toString());
        
        assertEquals(hero, fromDao);
    }

    /**
     * Test of getAllHeroes method, of class HeroDaoDB.
     */
    @Test
    public void testGetAllHeroes() {
        
        Organization organization1 = new Organization();
        organization1.setDescription("test description1");
        organization1.setName("test name1");
        organization1.setEmail("test email");
        organization1.setCity("test city1");
        
        organization1 = organizationDao.addOrganization(organization1);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization1);
        
        Hero hero1 = new Hero();
        hero1.setName("test name");
        hero1.setHeroType("test type");
        hero1.setSuperPower("test power");
        hero1.setDescription("test description");
        hero1.setOrganizations(organizations);
        
        hero1 = heroDao.addHero(hero1);
        
        Hero hero2 = new Hero();
        hero2.setName("test name");
        hero2.setHeroType("test type");
        hero2.setSuperPower("test power");
        hero2.setDescription("test description");
        hero2.setOrganizations(organizations);
        
        hero2 = heroDao.addHero(hero2);
        
        List<Hero> heroes = heroDao.getAllHeroes();
        
        assertEquals(2,heroes.size());
        assertTrue(heroes.contains(hero1));
        assertTrue(heroes.contains(hero2));
    }

    /**
     * Test of updateHero method, of class HeroDaoDB.
     */
    @Test
    public void testUpdateHero() {
        
        Organization organization1 = new Organization();
        organization1.setDescription("test description1");
        organization1.setName("test name1");
        organization1.setEmail("test email");
        organization1.setCity("test city1");
        
        organization1 = organizationDao.addOrganization(organization1);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization1);
        
        Hero hero1 = new Hero();
        hero1.setName("test name");
        hero1.setHeroType("test type");
        hero1.setSuperPower("test power");
        hero1.setDescription("test description");
        hero1.setOrganizations(organizations);
        
        hero1 = heroDao.addHero(hero1);
        
        Hero fromDao = heroDao.getHeroById(hero1.getId());
        assertEquals(hero1, fromDao);
        
        hero1.setName("test name2");
        heroDao.updateHero(hero1);
        
        assertNotEquals(hero1, fromDao);
        
        fromDao = heroDao.getHeroById(hero1.getId());
        
        assertEquals(hero1, fromDao);
    }

    @Test
    public void testGetHeroesForOrganization() {
        
        Organization organization1 = new Organization();
        organization1.setDescription("test description1");
        organization1.setName("test name1");
        organization1.setEmail("test email");
        organization1.setCity("test city1");
        
        organization1 = organizationDao.addOrganization(organization1);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization1);
        
        Hero hero1 = new Hero();
        hero1.setName("test name");
        hero1.setHeroType("test type");
        hero1.setSuperPower("test power");
        hero1.setDescription("test description");
        hero1.setOrganizations(organizations);
        
        hero1 = heroDao.addHero(hero1);
        
        List<Hero> fromDao = heroDao.getHeroesForOrganization(organization1.getId());
        
        assertEquals(fromDao.size(), 1);
        assertTrue(fromDao.contains(hero1));
        
    }
    
    @Test
    public void testDeleteHeroById(){
        Organization organization1 = new Organization();
        organization1.setDescription("test description1");
        organization1.setName("test name1");
        organization1.setEmail("test email");
        organization1.setCity("test city1");
        
        organization1 = organizationDao.addOrganization(organization1);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization1);
        
        Hero hero1 = new Hero();
        hero1.setName("test name");
        hero1.setHeroType("test type");
        hero1.setSuperPower("test power");
        hero1.setDescription("test description");
        hero1.setOrganizations(organizations);
        
        hero1 = heroDao.addHero(hero1);
        
        List<Hero> heroes = heroDao.getAllHeroes();
        assertEquals(heroes.size(), 1);
        
        heroDao.deleteHeroById(hero1.getId());
        heroes = heroDao.getAllHeroes();
        assertEquals(heroes.size(), 0);
    }
    
    @Test
    public void testGetHeroesForLocation(){
        Organization organization1 = new Organization();
        organization1.setDescription("test description1");
        organization1.setName("test name1");
        organization1.setEmail("test email");
        organization1.setCity("test city1");
        
        organization1 = organizationDao.addOrganization(organization1);
        List<Organization> organizations = new ArrayList<>();
        organizations.add(organization1);
        
        Hero hero1 = new Hero();
        hero1.setName("test name");
        hero1.setHeroType("test type");
        hero1.setSuperPower("test power");
        hero1.setDescription("test description");
        hero1.setOrganizations(organizations);
        
        hero1 = heroDao.addHero(hero1);
        
        Location location = new Location();
        location.setDescription("test description");
        location.setLatitude("test latitude");
        location.setLongitude("test longitude");
        location.setCity("test city");
        
        location = locationDao.addLocation(location);
        
        Sighting sighting = new Sighting();
        sighting.setHero(hero1);
        sighting.setLocation(location);
        sighting.setDate(LocalDate.now());
        
        sighting = sightingDao.addSighting(sighting);
        
        List<Hero> heroes = heroDao.getHeroesForLocation(location.getCity());
        assertEquals(heroes.size(), 1);
        assertTrue(heroes.contains(hero1));
        
    }

    
}
