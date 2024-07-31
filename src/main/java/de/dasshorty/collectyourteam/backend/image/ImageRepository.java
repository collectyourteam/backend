package de.dasshorty.collectyourteam.backend.image;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ImageRepository extends MongoRepository<ImageDto, ObjectId> {
}
