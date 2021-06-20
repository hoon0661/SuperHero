/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.Superhero.dao;

import com.hoon.Superhero.dto.Hero;
import com.hoon.Superhero.dto.Location;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class LocationDaoDB implements LocationDao{
    
    @Autowired
    JdbcTemplate jdbc;
    
    @Override
    public Location getLocationById(int id) {
        try{
            final String GET_LOCATION_BY_ID = "SELECT * from location WHERE id = ?";
            return jdbc.queryForObject(GET_LOCATION_BY_ID, new LocationMapper(), id);
        }catch(DataAccessException ex){
            return null;
        }
    }

    @Override
    public List<Location> getAllLocations() {
        final String GET_ALL_LOCATIONS = "SELECT * from location";
        return jdbc.query(GET_ALL_LOCATIONS, new LocationMapper());
    }

    @Override
    @Transactional
    public Location addLocation(Location location) {
        final String INSERT_LOCATION = "INSERT INTO location(description, city, latitude, longitude) "
                + "VALUES(?,?,?,?)";
        jdbc.update(INSERT_LOCATION, location.getDescription(), location.getCity(), location.getLatitude(), location.getLongitude());
        
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setId(newId);
        return location;
    }

    @Override
    public void updateLocation(Location location) {
        final String UPDATE_LOCATION = "UPDATE location "
                + "SET description = ?, city = ?, latitude = ?, longitude = ? WHERE id = ?";
        jdbc.update(UPDATE_LOCATION, location.getDescription(), location.getCity(), location.getLatitude(), location.getLongitude(), location.getId());
    }

    @Override
    @Transactional
    public void deleteLocationById(int id) {
        final String DELETE_SIGHTINGS = "DELETE FROM sighting WHERE locationId = ?";
        jdbc.update(DELETE_SIGHTINGS, id);
        
        final String DELETE_LOCATION_BY_ID = "DELETE FROM location WHERE id = ?";
        jdbc.update(DELETE_LOCATION_BY_ID, id);
    }

    @Override
    public List<Location> getLocationForHero(String heroName) {
        final String GET_LOCATION_FOR_HERO = "SELECT l.* FROM location l "
                + "JOIN sighting s ON l.id = s.locationId "
                + "JOIN hero h ON h.id = s.heroId "
                + "WHERE h.name = ?";
        return jdbc.query(GET_LOCATION_FOR_HERO, new LocationMapper(), heroName);
    }
    
    public static final class LocationMapper implements RowMapper<Location> {

        @Override
        public Location mapRow(ResultSet rs, int i) throws SQLException {
            Location location = new Location();
            location.setId(rs.getInt("id"));
            location.setDescription(rs.getString("description"));
            location.setLongitude(rs.getString("longitude"));
            location.setLatitude(rs.getString("latitude"));
            location.setCity(rs.getString("city"));
            
            return location;
        }
        
    }
    
}
