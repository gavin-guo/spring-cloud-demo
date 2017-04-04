package com.gavin.repository;

import com.gavin.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    UserEntity findByLoginName(String _loginName);

}
