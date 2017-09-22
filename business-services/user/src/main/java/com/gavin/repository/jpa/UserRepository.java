package com.gavin.repository.jpa;

import com.gavin.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByLoginName(String _loginName);

}
