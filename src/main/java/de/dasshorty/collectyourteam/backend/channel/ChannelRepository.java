package de.dasshorty.collectyourteam.backend.channel;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChannelRepository extends MongoRepository<ChannelDto, ObjectId> {
}
