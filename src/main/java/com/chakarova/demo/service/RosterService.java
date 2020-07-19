package com.chakarova.demo.service;

import com.chakarova.demo.model.binding.RosterAddBindingModel;

import java.util.List;
import java.util.Map;

public interface RosterService {
    void addRoster(RosterAddBindingModel rosterAddBindingModel);
    Map<String, List<String>> getLastRoster();
}
