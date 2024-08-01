package de.dasshorty.collectyourteam.backend.channel.messages;

import de.dasshorty.collectyourteam.backend.channel.ChannelDto;
import de.dasshorty.collectyourteam.backend.user.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.Date;

@Document(collection = "messages")
@Getter
@Setter
public class MessageDto {

    private ObjectId id;

    @DocumentReference
    private ChannelDto channel;

    @DocumentReference
    private UserDto author;

    private String content;

    private Date sendAt;

}
