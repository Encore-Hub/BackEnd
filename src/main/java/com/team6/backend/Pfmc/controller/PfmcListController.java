package com.team6.backend.Pfmc.controller;

import com.team6.backend.Pfmc.entity.PfmcList;
import com.team6.backend.Pfmc.repository.PfmcListRepository;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class PfmcListController {

    @Autowired
    private PfmcListRepository PfmcListRepository;

    @GetMapping("/pfmc")
    public List<PfmcList> getAllMusicals() {
        return PfmcListRepository.findAll();
    }
}
