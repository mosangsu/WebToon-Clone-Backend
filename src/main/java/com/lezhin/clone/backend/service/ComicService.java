package com.lezhin.clone.backend.service;

import org.modelmapper.ModelMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;
import java.util.List;

import com.lezhin.clone.backend.dto.ComicDto;
import com.lezhin.clone.backend.entity.Comic;
import com.lezhin.clone.backend.enums.EpisodeSchedule;
import com.lezhin.clone.backend.repository.ComicRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class ComicService {

    private final ComicRepository comicRepository;

    private final ModelMapper modelMapper;

    public List<ComicDto.Scheduled.Res> getScheduled(@NonNull EpisodeSchedule episodeSchedule) {
        List<Comic> e = comicRepository.findByEpisodeSchedule(episodeSchedule);
        return e.stream().map(p -> modelMapper.map(p, ComicDto.Scheduled.Res.class)).collect(Collectors.toList());
    }
}