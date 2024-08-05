package de.dasshorty.collectyourteam.backend.server;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServerRepository extends MongoRepository<ServerDto, ObjectId> {
}
