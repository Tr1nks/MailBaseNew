package com.tr1nksgroup.model.services;

import com.tr1nksgroup.model.entities.StudyLevelEntity;

public interface StudyLevelService {
    StudyLevelEntity getByLevelId(Integer integer);

    boolean containsByLevelId(Integer integer);

    StudyLevelEntity save(StudyLevelEntity studyLevelEntity);
}
