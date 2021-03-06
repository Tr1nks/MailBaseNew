package com.tr1nksgroup.model.services;

import com.tr1nksgroup.model.entities.SpecialityEntity;

public interface SpecialityService {
    SpecialityEntity getBySpecialityId(Integer integer);

    boolean containsByspecialityId(int specialityId);

    SpecialityEntity save(SpecialityEntity specialityEntity);
}
