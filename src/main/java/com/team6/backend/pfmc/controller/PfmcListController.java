package com.team6.backend.pfmc.controller;

import com.team6.backend.pfmc.entity.PfmcList;
import com.team6.backend.pfmc.repository.PfmcListRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class PfmcListController {

    @Autowired
    private PfmcListRepository PfmcListRepository;

    @GetMapping("/pfmclist")
    public List<PfmcList> getAllPfmclists() {
        return PfmcListRepository.findAll();
    }
}
