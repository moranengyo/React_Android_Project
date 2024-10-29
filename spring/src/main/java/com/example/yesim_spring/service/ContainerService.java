package com.example.yesim_spring.service;

import com.example.yesim_spring.database.Dto.ContainerDto;
import com.example.yesim_spring.database.repository.ContainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ContainerService {
    private final ContainerRepository containerRepository;

    public List<ContainerDto> findAll(){
        return containerRepository.findAll()
                .stream().map(ContainerDto::of).toList();
    }
}
