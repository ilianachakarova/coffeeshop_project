package com.chakarova.demo.dao;

import com.chakarova.demo.model.entity.Roster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RosterRepository extends JpaRepository<Roster,Long> {
}
