package de.dasshorty.collectyourteam.backend.role;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface RoleRepository extends MongoRepository<RoleDto, ObjectId> {

    Optional<RoleDto> findByName(String name);

}
