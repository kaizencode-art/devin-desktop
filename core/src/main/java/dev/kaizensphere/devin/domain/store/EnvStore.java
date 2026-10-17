package dev.kaizensphere.devin.domain.store;

import dev.kaizensphere.devin.domain.model.EnvModel;

import java.util.List;
import java.util.Optional;

public interface EnvStore extends EnvSubject {
    void notifyEnvChange(EnvStoreListener.Change change);
    void addEnv(EnvModel env);
    void updateEnv(EnvModel oldEnv, EnvModel newEnv);
    boolean removeEnv(EnvModel env);
    List<EnvModel> getEnvs();
    Optional<EnvModel> getEnv(EnvModel env);
}
