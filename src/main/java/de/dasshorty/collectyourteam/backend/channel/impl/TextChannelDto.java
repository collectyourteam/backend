package de.dasshorty.collectyourteam.backend.channel.impl;

import de.dasshorty.collectyourteam.backend.channel.ChannelDto;
import de.dasshorty.collectyourteam.backend.channel.messages.MessageDto;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.List;

public class TextChannelDto extends ChannelDto {

    @DocumentReference
    List<MessageDto> message;

}
