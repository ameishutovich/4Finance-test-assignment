package com.meishuto.common.persistence.dao;

import com.meishuto.common.persistence.domain.Application;

import java.util.Date;
import java.util.List;

public interface ApplicationDAO {
    Application saveApplication(Application application);

    Long countApplicationsFromPeriod(Date from, Date to, String ipAddress, Integer... statuses);

    List<Application> getApplicationsOfUser(String ipAddress);
}
