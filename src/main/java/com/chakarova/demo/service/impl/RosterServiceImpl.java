package com.chakarova.demo.service.impl;

import com.chakarova.demo.dao.RosterRepository;
import com.chakarova.demo.error.ProductNotFoundError;
import com.chakarova.demo.model.binding.RosterAddBindingModel;
import com.chakarova.demo.model.entity.Roster;
import com.chakarova.demo.model.view.UsersAllViewModel;
import com.chakarova.demo.service.RosterService;
import com.chakarova.demo.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RosterServiceImpl implements RosterService {

    private final RosterRepository rosterRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;

    public RosterServiceImpl(RosterRepository rosterRepository, ModelMapper modelMapper, UserService userService) {
        this.rosterRepository = rosterRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public void addRoster(RosterAddBindingModel rosterAddBindingModel) {
        Roster entity = this.modelMapper.map(rosterAddBindingModel,Roster.class);
        this.rosterRepository.save(entity);
    }

    @Override
    public Map<String, List<String>> getLastRoster() {
        if(this.rosterRepository.count()!=0){
            Roster roster = this.rosterRepository.findById(rosterRepository.count())
                    .orElse(null);
            RosterAddBindingModel rosterAddBindingModel= this.modelMapper.map(roster,RosterAddBindingModel.class);
            return this.uploadRoster(rosterAddBindingModel);
        }else {
            throw new ProductNotFoundError("Roster not found");
        }

    }

    private Map<String, List<String>> uploadRoster(RosterAddBindingModel rosterAddBindingModel) {
        Map rosterMap = new HashMap();
        List<String>employeeNames =this.userService.findAllUsers().stream().filter(u->u.getRoles().contains("ROLE_USER"))
                .map(UsersAllViewModel::getUsername)
                .collect(Collectors.toList());
        for (int i = 0; i <employeeNames.size() ; i++) {
            List<String>val=new ArrayList<>();
            val.add(rosterAddBindingModel.getMonday().split(",")[i]);
            val.add(rosterAddBindingModel.getTuesday().split(",")[i]);
            val.add(rosterAddBindingModel.getWednesday().split(",")[i]);
            val.add(rosterAddBindingModel.getThursday().split(",")[i]);
            val.add(rosterAddBindingModel.getFriday().split(",")[i]);
            val.add(rosterAddBindingModel.getSaturday().split(",")[i]);

            rosterMap.put(employeeNames.get(i),val);

        }

        return rosterMap;
    }
}
