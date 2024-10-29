package com.example.yesim_spring.controller;

import com.example.yesim_spring.database.Dto.ContainerDto;
import com.example.yesim_spring.service.ContainerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContainerController {

    private final ContainerService containerService;

    @GetMapping("/manager/container/all")
    public List<ContainerDto> getContainerList(){
        return containerService.findAll();
    }
}
