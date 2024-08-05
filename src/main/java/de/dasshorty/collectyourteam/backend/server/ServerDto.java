package de.dasshorty.collectyourteam.backend.server;

import de.dasshorty.collectyourteam.backend.image.ImageDto;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Setter
@Getter
@Document(collection = "server")
public class ServerDto {

    @Id
    private ObjectId id;

    private String serverName;
    private String serverDescription;
    @DocumentReference
    private ImageDto logo;
    @DocumentReference
    private ImageDto banner;

}
