/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hoon.Superhero.dao;

import com.hoon.Superhero.dto.Hero;
import com.hoon.Superhero.dto.Location;
import com.hoon.Superhero.dto.Organization;
import java.util.List;

/**
 *
 * @author Hoon
 */
public interface HeroDao {
    Hero getHeroById(int id);
    List<Hero> getAllHeroes();
    Hero addHero(Hero hero);
    void updateHero(Hero hero);
    void deleteHeroById(int id);
   
    List<Hero> getHeroesForOrganization(int organizationId);
//    List<Hero> getHeroesForLocation(Location location);
    List<Hero> getHeroesForLocation(String location);
}
