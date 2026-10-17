package dev.kaizensphere.devin.domain.store;

import dev.kaizensphere.devin.domain.model.EnvModel;

import java.util.*;

public class InMemoryEnvStore implements EnvStore {
    private final List<EnvModel> envs;
    private final Map<UUID, Integer> idIndex = new HashMap<>();
    List<EnvStoreListener> listeners = new ArrayList<>();

    public InMemoryEnvStore() {
        this.envs = new ArrayList<>();
    }

    public InMemoryEnvStore(List<EnvModel> envs) {
        this.envs = envs;
        rebuildIdIndex();
    }

    private void rebuildIdIndex() {
        idIndex.clear();
        for (int i = 0; i < envs.size(); i++) {
            idIndex.put(envs.get(i).id(), i);
        }
    }

    public void notifyEnvChange(EnvStoreListener.Change change) {
        listeners.forEach(listener -> listener.onEnvironmentChanged(change));
    }

    public void addEnv(EnvModel envModel) {
        envs.add(envModel);
        idIndex.put(envModel.id(), envs.size() - 1);
        notifyEnvChange(new EnvStoreListener.Change.EnvAdded(envModel));
    }

    public void updateEnv(EnvModel oldEnv, EnvModel newEnv) {
        Integer index = idIndex.get(oldEnv.id());
        if (index == null) return;
        envs.set(index, newEnv);
        rebuildIdIndex();
        notifyEnvChange(new EnvStoreListener.Change.EnvUpdated(oldEnv, newEnv));
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
            notifyEnvChange(new EnvStoreListener.Change.EnvDeleted(envModel));
        }
        return removed;
    }

    public List<EnvModel> getEnvs() {
        return List.copyOf(envs);
    }

    public Optional<EnvModel> getEnv(EnvModel envModel) {
        Integer index = idIndex.get(envModel.id());
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
