package dev.kaizensphere.devin.domain.store;

import dev.kaizensphere.devin.domain.model.EnvModel;
import dev.kaizensphere.devin.domain.model.EnvVariableModel;
import dev.kaizensphere.devin.application.ports.out.EnvRepository;

import java.util.*;

public class EnvStore implements EnvSubject {
    private final List<EnvModel> envs;
    private final Map<UUID, Integer> idIndex = new HashMap<>();
    List<EnvStoreListener> listeners = new ArrayList<>();

    public EnvStore() {
        this.envs = new ArrayList<>();
        envs.add(new EnvModel("test env", "New Environment", EnvModel.Status.VALID, new ArrayList<>()));
        rebuildIdIndex();
    }

    public EnvStore(List<EnvModel> envs) {
        this.envs = envs;
        rebuildIdIndex();
    }

    private void rebuildIdIndex() {
        idIndex.clear();
        for (int i = 0; i < envs.size(); i++) {
            idIndex.put(envs.get(i).id(), i);
        }
    }

    private void notifyListeners() {
        listeners.forEach(EnvStoreListener::onEnvironmentChanged);
    }

    public void addEnv(EnvModel envModel) {
        envs.add(envModel);
        idIndex.put(envModel.id(), envs.size() - 1);
        notifyListeners();
    }

    public void updateEnv(UUID oldId, EnvModel newEnv) {
        Integer index = idIndex.get(oldId);
        if (index == null) return;
        envs.set(index, newEnv);
        rebuildIdIndex();
        notifyListeners();
    }

    public boolean removeEnv(EnvModel envModel) {
        Integer index = idIndex.get(envModel.id());
        boolean removed = envs.remove(envModel);
        if (!removed && index != null) {
            if (index >= 0 && index < envs.size() && envs.get(index).id().equals(envModel.id())) {
                envs.remove((int) index);
                removed = true;
            }
        }
        if (removed) {
            rebuildIdIndex();
            notifyListeners();
        }
        return removed;
    }

    public List<EnvModel> getEnvs() {
        return List.copyOf(envs);
    }

    public Optional<EnvModel> getEnvById(UUID id) {
        Integer index = idIndex.get(id);
        return index != null ? Optional.of(envs.get(index)) : Optional.empty();
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
