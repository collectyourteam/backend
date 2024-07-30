package de.dasshorty.collectyourteam.backend.user;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserDto, ObjectId> {

    Optional<UserDto> findByUsername(String username);

}
