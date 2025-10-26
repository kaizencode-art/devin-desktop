package io.github.kaizensphere.devin.devindesktop.store;

import io.github.kaizensphere.devin.devindesktop.models.EnvModel;
import io.github.kaizensphere.devin.devindesktop.models.EnvVariableModel;

import java.util.*;

public class EnvStore implements EnvSubject {
    private final List<EnvModel> envs = new ArrayList<>();
    private final Map<UUID, Integer> idIndex = new HashMap<>();
    List<EnvStoreListener> listeners = new ArrayList<>();

    public void addEnvVariable(UUID envId, EnvVariableModel envVariableModel) {
        getEnvById(envId).ifPresent(envModel -> {
            envModel.variables().add(envVariableModel);
            notifyListeners();
        });
    }

    public void updateEnvVariable(UUID envId, EnvVariableModel envVariableModel) {
        getEnvById(envId).ifPresent(envModel -> {
            envModel.variables().set(envModel.variables().indexOf(envVariableModel), envVariableModel);
            notifyListeners();
        });
    }

    public void removeEnvVariable(UUID envId, EnvVariableModel envVariableModel) {
        getEnvById(envId).ifPresent(envModel -> {
            envModel.variables().remove(envVariableModel);
            notifyListeners();
        });
    }

    public void addEnv(EnvModel envModel) {
        if (idIndex.containsKey(envModel.id())) {
            throw new IllegalArgumentException("Duplicate Env ID: " + envModel.id());
        }
        envs.add(envModel);
        idIndex.put(envModel.id(), envs.size() - 1);
        notifyListeners();

    }

    public void updateEnv(EnvModel envModel) {
        if (!idIndex.containsKey(envModel.id())) {
            throw new IllegalArgumentException("Env ID not found: " + envModel.id());
        }
        envs.set(idIndex.get(envModel.id()), envModel);
        notifyListeners();
    }

    public List<EnvModel> getEnvs() {
        return List.copyOf(envs);
    }

    public Optional<EnvModel> getEnvById(UUID id) {
        Integer index = idIndex.get(id);
        return index != null ? Optional.of(envs.get(index)) : Optional.empty();
    }

    public boolean removeEnv(EnvModel envModel) {
        if (!envs.contains(envModel)) return false;
        envs.remove(envModel);
        idIndex.remove(envModel.id());
        notifyListeners();
        return true;
    }

    private void notifyListeners() {
        listeners.forEach(EnvStoreListener::onEnvironmentChanged);
    }

    @Override
    public void registerListener(EnvStoreListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(EnvStoreListener listener) {
        listeners.remove(listener);
    }
}
