package com.lambdacode.creditscoreanalysiswebapplication.Repository;

import com.lambdacode.creditscoreanalysiswebapplication.Model.Activity;
import com.lambdacode.creditscoreanalysiswebapplication.Model.Staff;
import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    List<Activity> findTop5ByOrderByActivityTimeDesc();

    @Query("SELECT a FROM Activity a WHERE a.user.userEmail = :email")
    List<Activity> findByUserEmail(@Param("email") String email);

    @Query("SELECT a FROM Activity a WHERE a.staff.staffEmail = :email")
    List<Activity> findByStaffEmail(@Param("email") String email);

    void deleteActivitiesByUser(User userId);

    void deleteActivitiesByStaff(Staff staff);
}
