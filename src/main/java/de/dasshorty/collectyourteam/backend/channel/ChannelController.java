package de.dasshorty.collectyourteam.backend.channel;

import de.dasshorty.collectyourteam.backend.user.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/channel")
public class ChannelController {

    private final ChannelRepository channelRepository;

    @Autowired
    public ChannelController(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @GetMapping()
    public ResponseEntity<?> getChannels(@AuthenticationPrincipal UserDto dto) {

        return ResponseEntity.ok(channelRepository.findAll());

    }

}
