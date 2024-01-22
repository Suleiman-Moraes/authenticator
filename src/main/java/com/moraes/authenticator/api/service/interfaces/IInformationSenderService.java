package com.moraes.authenticator.api.service.interfaces;

import com.moraes.authenticator.api.model.Param;
import com.moraes.authenticator.api.model.User;

public interface IInformationSenderService {
    
    void sendEmailResetPassword(User user, String timeExpiration, Param paramUrlFrontend);
}
