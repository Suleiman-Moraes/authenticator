package br.com.moraes.authenticator.api.service;

import br.com.moraes.authenticator.api.interfaces.ICrudPatternService;
import br.com.moraes.authenticator.api.model.Profile;

public interface ProfileService extends ICrudPatternService<Profile>{

    Profile findTopByName(String name);
}