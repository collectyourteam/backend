package de.dasshorty.collectyourteam.backend.image;

import lombok.Getter;
import lombok.Setter;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "images")
@Getter
@Setter
public class ImageDto {

    @Id
    private ObjectId id;
    private Binary image;

}
