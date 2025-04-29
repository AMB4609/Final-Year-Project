package com.lambdacode.creditscoreanalysiswebapplication.Repository;

import com.lambdacode.creditscoreanalysiswebapplication.Model.CreditReport;
import com.lambdacode.creditscoreanalysiswebapplication.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CreditReportRepository extends JpaRepository<CreditReport, Long> {
    List<CreditReport> findByUser(User user);

}
