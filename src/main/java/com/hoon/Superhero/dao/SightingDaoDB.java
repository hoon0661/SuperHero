/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.Superhero.dao;

import com.hoon.Superhero.dao.HeroDaoDB.HeroMapper;
import com.hoon.Superhero.dao.LocationDaoDB.LocationMapper;
import com.hoon.Superhero.dao.OrganizationDaoDB.OrganizationMapper;
import com.hoon.Superhero.dto.Hero;
import com.hoon.Superhero.dto.Location;
import com.hoon.Superhero.dto.Organization;
import com.hoon.Superhero.dto.Sighting;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
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
public class SightingDaoDB implements SightingDao{
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Sighting getSightingById(int id) {
        try{
            final String GET_SIGHTING_BY_ID = "SELECT * FROM sighting WHERE id = ?";
            Sighting sighting =  jdbc.queryForObject(GET_SIGHTING_BY_ID, new SightingMapper(), id);
            sighting.setLocation(getLocationForSighting(id));
            sighting.setHero(getHeroForSighting(id));
            sighting.getHero().setOrganizations(getOrganizationsForHero(sighting.getHero().getId()));
            return sighting;
        } catch (DataAccessException ex){
            return null;
        }
    }
    
    private List<Organization> getOrganizationsForHero(int id){
        final String SELECT_ORGANIZATIONS_FOR_HERO = "SELECT o.* FROM organization o JOIN hero_organization ho ON o.id = ho.organizationId WHERE heroId = ?";
        return jdbc.query(SELECT_ORGANIZATIONS_FOR_HERO, new OrganizationMapper(), id);
    }
    
    private Location getLocationForSighting(int id){
        final String SELECT_LOCATION_FOR_SIGHTING = "SELECT l.* FROM location l JOIN sighting s ON l.id = s.locationId WHERE s.id = ?";
        return jdbc.queryForObject(SELECT_LOCATION_FOR_SIGHTING, new LocationMapper(), id);
    }
    
    private Hero getHeroForSighting(int id){
        final String SELECT_HERO_FOR_SIGHTING = "SELECT h.* FROM hero h JOIN sighting s ON h.id = s.heroId WHERE s.id = ?";
        return jdbc.queryForObject(SELECT_HERO_FOR_SIGHTING, new HeroMapper(), id);
    }

    @Override
    public List<Sighting> getAllSightings() {
        final String SELECT_ALL_SIGHTINGS = "SELECT * FROM sighting";
        List<Sighting> sightings = jdbc.query(SELECT_ALL_SIGHTINGS, new SightingMapper());
        associateLocationAndHero(sightings);
        associateOrganizationForHero(sightings);
        return sightings;
    }
    
    private void associateOrganizationForHero(List<Sighting> sightings){
        for(Sighting sighting : sightings){
            sighting.getHero().setOrganizations(getOrganizationsForHero(sighting.getHero().getId()));
        }
    }
    
    private void associateLocationAndHero(List<Sighting> sightings){
        for(Sighting sighting : sightings){
            sighting.setLocation(getLocationForSighting(sighting.getId()));
            sighting.setHero(getHeroForSighting(sighting.getId()));
        }
    }

    @Override
    @Transactional
    public Sighting addSighting(Sighting sighting) {
        final String INSERT_SIGHTING = "INSERT INTO sighting (locationId, heroId, date) VALUES(?,?,?)";
        jdbc.update(INSERT_SIGHTING, sighting.getLocation().getId(), sighting.getHero().getId(), java.sql.Date.valueOf(sighting.getDate()));
        
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setId(newId);
        return sighting;
    }

    @Override
    public void updateSighting(Sighting sighting) {
        final String UPDATE_SIGHTING = "UPDATE sighting "
                + "SET locationId = ?, "
                + "heroId = ?, "
                + "date = ? "
                + "WHERE id = ?";
        
        jdbc.update(UPDATE_SIGHTING, sighting.getLocation().getId(), sighting.getHero().getId(), java.sql.Date.valueOf(sighting.getDate()), sighting.getId());
        
    }

//    @Override
//    @Transactional
    @Override
    public void deleteSightingById(int id) {
//        final String DELETE_HERO_SIGHTING = "DELETE FROM hero_sighting WHERE sightingId = ?";
//        jdbc.update(DELETE_HERO_SIGHTING, id);
        
        final String DELETE_SIGHTING = "DELETE FROM sighting WHERE id = ?";
        jdbc.update(DELETE_SIGHTING, id);
    }

    @Override
    public List<Sighting> getSightingsForDate(LocalDate date) {
        final String GET_SIGHTINGS_FOR_DATE = "SELECT * FROM sighting WHERE date = ?";
        
        List<Sighting> sightings =  jdbc.query(GET_SIGHTINGS_FOR_DATE, new SightingMapper(), date);
        associateLocationAndHero(sightings);
        associateOrganizationForHero(sightings);
        return sightings;
    }


    public static final class SightingMapper implements RowMapper<Sighting> {

        @Override
        public Sighting mapRow(ResultSet rs, int i) throws SQLException {
            Sighting sighting = new Sighting();
            sighting.setId(rs.getInt("id"));
            sighting.setDate(rs.getDate("date").toLocalDate());
            
            return sighting;
            
        }
        
    }
    
    
}
