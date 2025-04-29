package com.lambdacode.creditscoreanalysiswebapplication.Repository;

import com.lambdacode.creditscoreanalysiswebapplication.Model.Staff;
import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepository extends JpaRepository<Staff, Integer> {
    Staff findByStaffEmail(String staffName);
    List<Staff> findByStatusFalse();
}
