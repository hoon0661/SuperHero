/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.Superhero.dao;

import com.hoon.Superhero.dao.OrganizationDaoDB.OrganizationMapper;
import com.hoon.Superhero.dto.Hero;
import com.hoon.Superhero.dto.Location;
import com.hoon.Superhero.dto.Organization;
import com.hoon.Superhero.dto.Sighting;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Hoon
 */
@Repository
public class HeroDaoDB implements HeroDao{
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Hero getHeroById(int id) {
        try{
            final String GET_HERO_BY_ID = "SELECT * FROM hero WHERE id = ?";
            Hero hero = jdbc.queryForObject(GET_HERO_BY_ID, new HeroMapper(), id);
            hero.setOrganizations(getOrganizationsForHero(id));
            return hero;
        } catch (DataAccessException ex){
            return null;
        }
    }
    
    private List<Organization> getOrganizationsForHero(int id){
        final String SELECT_ORGANIZATIONS_FOR_HERO = "SELECT o.* FROM organization o JOIN hero_organization ho ON o.id = ho.organizationId WHERE heroId = ?";
        return jdbc.query(SELECT_ORGANIZATIONS_FOR_HERO, new OrganizationMapper(), id);
    }

    @Override
    public List<Hero> getAllHeroes() {
        final String GET_ALL_HEROES = "SELECT * FROM hero";
        List<Hero> heroes = jdbc.query(GET_ALL_HEROES, new HeroMapper());
        associateOrganizations(heroes);
        return heroes;
    }
    
    private void associateOrganizations(List<Hero> heroes){
        for(Hero hero : heroes){
            hero.setOrganizations(getOrganizationsForHero(hero.getId()));
        }
    }

    @Override
    @Transactional
    public Hero addHero(Hero hero) {
        final String INSERT_HERO = "INSERT INTO hero(name, description, superPower, heroType, imgUrl) "
                + "VALUES(?,?,?,?,?)";
        jdbc.update(INSERT_HERO, hero.getName(), hero.getDescription(), hero.getSuperPower(), hero.getHeroType(), hero.getImgUrl());
        
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        hero.setId(newId);
        insertHeroOrganization(hero);
//        insertHeroSighting(hero);
        return hero;
    }
    
    private void insertHeroOrganization(Hero hero){
        final String INSERT_HERO_ORGANIZATION ="INSERT INTO hero_organization(heroId, organizationId) VALUES(?,?)";
        for(Organization organization : hero.getOrganizations()){
            jdbc.update(INSERT_HERO_ORGANIZATION, hero.getId(), organization.getId());
        }
    }
    
//    private void insertHeroSighting(Hero hero){
//        final String INSERT_HERO_SIGHTING ="INSERT INTO sighting(heroId, sightingId) VALUES(?,?)";
//        for(Sighting sighting : hero.getSightings()){
//            jdbc.update(INSERT_HERO_SIGHTING, hero.getId(), sighting.getId());
//        }
//    }

    @Override
    public void updateHero(Hero hero) {
        final String UPDATE_HERO = "UPDATE hero "
                + "SET name = ?, "
                + "description = ?, "
                + "superPower = ?, "
                + "heroType = ?, "
                + "imgUrl = ? "
                + "WHERE id = ?";
        jdbc.update(UPDATE_HERO, hero.getName(), hero.getDescription(), hero.getSuperPower(), hero.getHeroType(), hero.getImgUrl(), hero.getId());
        
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM hero_organization WHERE heroId = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, hero.getId());
        insertHeroOrganization(hero);
    }

    @Override
    @Transactional
    public void deleteHeroById(int id) {
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM hero_organization WHERE heroId = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, id);
        
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE heroId = ?";
        jdbc.update(DELETE_SIGHTING, id);
        
        final String DELETE_HERO = "DELETE FROM hero WHERE Id = ?";
        jdbc.update(DELETE_HERO, id);
    }

    @Override
    public List<Hero> getHeroesForOrganization(int organizationId) {
        final String GET_HEROES_FOR_ORGANIZATION = "SELECT h.* FROM hero h "
                + "JOIN hero_organization ho ON h.id = ho.heroId "
                + "WHERE ho.organizationId = ?";
        List<Hero> heroes = jdbc.query(GET_HEROES_FOR_ORGANIZATION, new HeroMapper(), organizationId);
        associateOrganizations(heroes);
        return heroes;
    }

//    @Override
//    public List<Hero> getHeroesForLocation(Location location) {
//        final String GET_HEROES_FOR_LOCATION = "SELECT h.* FROM hero h "
//                + "JOIN sighting s ON h.id = s.heroId "
//                + "WHERE s.locationId = ?";
//        
//        List<Hero> heroes = jdbc.query(GET_HEROES_FOR_LOCATION, new HeroMapper(), location.getId());
//        associateOrganizations(heroes);
//        return heroes;
//    }
    
    @Override
    public List<Hero> getHeroesForLocation(String location) {
        final String GET_HEROES_FOR_LOCATION = "SELECT h.* FROM hero h "
                + "JOIN sighting s ON h.id = s.heroId "
                + "JOIN location l ON s.locationId = l.id "
                + "WHERE l.city = ?";
        
        List<Hero> heroes = jdbc.query(GET_HEROES_FOR_LOCATION, new HeroMapper(), location);
        associateOrganizations(heroes);
        return heroes;
    }
    
    public static final class HeroMapper implements RowMapper<Hero>{

        @Override
        public Hero mapRow(ResultSet rs, int i) throws SQLException {
            Hero hero = new Hero();
            hero.setId(rs.getInt("id"));
            hero.setName(rs.getString("name"));
            hero.setDescription(rs.getString("description"));
            hero.setSuperPower(rs.getString("superPower"));
            hero.setHeroType(rs.getString("heroType"));
            hero.setImgUrl(rs.getString("imgUrl"));

            return hero;
        }
        
    }
}
