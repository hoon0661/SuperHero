/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.Superhero.dao;

import com.hoon.Superhero.dto.Organization;
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
public class OrganizationDaoDB implements OrganizationDao{
    
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public Organization getOrganizationById(int id) {
        try{
            final String GET_ORGANIZATION_BY_ID = "SELECT * FROM organization WHERE id = ?";
            return jdbc.queryForObject(GET_ORGANIZATION_BY_ID, new OrganizationMapper(), id);
        }catch (DataAccessException ex){
            return null;
        }
    }

    @Override
    public List<Organization> getAllOrganizations() {
        final String GET_ALL_ORGANIZATIONS = "SELECT * FROM organization";
        return jdbc.query(GET_ALL_ORGANIZATIONS, new OrganizationMapper());
    }

    @Override
    @Transactional
    public Organization addOrganization(Organization organization) {
        final String INSERT_ORGANIZATION = "INSERT INTO organization(name, description, city, email) VALUES(?,?,?,?)";
        jdbc.update(INSERT_ORGANIZATION, organization.getName(), organization.getDescription(), organization.getCity(), organization.getEmail());
        
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organization.setId(newId);
        return organization;
    }

    @Override
    public void updateOrganization(Organization organization) {
        final String UPDATE_ORGANIZATION = "UPDATE organization SET name = ?, description = ?, city = ?, email = ? WHERE id = ?";
        jdbc.update(UPDATE_ORGANIZATION, organization.getName(), organization.getDescription(), organization.getCity(), organization.getEmail(), organization.getId());
    }

    @Override
    @Transactional
    public void deleteOrganizationById(int id) {
        final String DELETE_HERO_ORGANIZATION = "DELETE FROM hero_organization WHERE organizationId = ?";
        jdbc.update(DELETE_HERO_ORGANIZATION, id);
        
        final String DELETE_ORGANIZATION = "DELETE FROM organization WHERE id = ?";
        jdbc.update(DELETE_ORGANIZATION, id);
    }

    @Override
    public List<Organization> getOrganizationsForHero(int heroId) {
        final String GET_ORGANIZATIONS_FOR_HERO = "SELECT o.* FROM organization o JOIN hero_organization ho ON o.id = ho.organizationId WHERE ho.heroId = ?";
        return jdbc.query(GET_ORGANIZATIONS_FOR_HERO, new OrganizationMapper(), heroId);
    }
    
    
    
    public static final class OrganizationMapper implements RowMapper<Organization> {

        @Override
        public Organization mapRow(ResultSet rs, int i) throws SQLException {
            Organization organization = new Organization();
            organization.setId(rs.getInt("id"));
            organization.setName(rs.getString("name"));
            organization.setDescription(rs.getString("description"));
            organization.setCity(rs.getString("city"));
            organization.setEmail(rs.getString("email"));
            
            return organization;
        }
    }
}