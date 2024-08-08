package com.team6.backend.pfmc.controller;

import com.team6.backend.pfmc.entity.Pfmc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class PfmcController {

    @Autowired
    private com.team6.backend.pfmc.repository.PfmcRepository PfmcRepository;

    @GetMapping("/pfmc")
    public List<Pfmc> getAllMusicals() {
        return PfmcRepository.findAll();
    }
}

