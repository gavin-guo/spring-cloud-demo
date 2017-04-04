package com.gavin.repository;

import com.gavin.entity.UserAuthorityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthorityEntity, String> {

}
