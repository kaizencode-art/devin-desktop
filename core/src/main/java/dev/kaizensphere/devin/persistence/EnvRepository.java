package dev.kaizensphere.devin.persistence;

import dev.kaizensphere.devin.model.EnvModel;

import java.util.Collection;

public interface EnvRepository {

    public void saveAll(Collection<EnvModel> envs);

    public Collection<EnvModel> loadAll();

    public void close();
}
