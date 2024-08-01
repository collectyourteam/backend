package de.dasshorty.collectyourteam.backend.channel;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "channels")
@Getter
@Setter
public abstract class ChannelDto {

    @Id
    private ObjectId id;
    private ChannelType channelType;
    private String channelName;
    private String channelDescription;
    boolean locked;

}
