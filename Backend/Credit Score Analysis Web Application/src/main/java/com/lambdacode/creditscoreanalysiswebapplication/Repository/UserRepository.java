package com.lambdacode.creditscoreanalysiswebapplication.Repository;

import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserEmail(String userEmail);
    List<User> findByStatusFalse();
}
