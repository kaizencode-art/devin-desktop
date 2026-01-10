package dev.kaizensphere.devin.application.ports.out;

import dev.kaizensphere.devin.domain.model.EnvModel;

import java.util.Collection;

public interface EnvRepository {

    public void saveAll(Collection<EnvModel> envs);

    public Collection<EnvModel> loadAll();

    public void close();
}
