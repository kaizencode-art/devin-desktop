package io.github.kaizensphere.devin.devindesktop.store;

import io.github.kaizensphere.devin.devindesktop.models.EnvModel;
import io.github.kaizensphere.devin.devindesktop.models.EnvVariableModel;

import java.util.*;

public class EnvStore implements EnvSubject {
    private final List<EnvModel> envs = new ArrayList<>();
    private final Map<UUID, Integer> idIndex = new HashMap<>();
    List<EnvStoreListener> listeners = new ArrayList<>();

    private int findVarIndexById(List<EnvVariableModel> vars, UUID id) {
        for (int i = 0; i < vars.size(); i++) {
            if (vars.get(i).id().equals(id)) return i;
        }
        return -1;
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

    public void updateEnv(EnvModel envModel) {
        Integer index = idIndex.get(envModel.id());
        if(index == null) return;
        envs.set(index, envModel);
        notifyListeners();
    }

    public boolean removeEnv(EnvModel envModel) {
        Integer index = idIndex.get(envModel.id());
        boolean removed = envs.remove(envModel);
        if(!removed && index != null) {
            if(index >= 0 && index < envs.size() && envs.get(index).id().equals(envModel.id())) {
                envs.remove((int)index);
                removed = true;
            }
        }
        if(removed) {
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


    public void addEnvVariable(UUID envId, EnvVariableModel newEnvVariableModel) {
        Integer index = idIndex.get(envId);
        if(index == null) return;
        EnvModel envModel = envs.get(index);
        List<EnvVariableModel> updatedVars = new ArrayList<>(envModel.variables());
        updatedVars.add(newEnvVariableModel);
        EnvModel updatedEnvModel = new EnvModel(envModel.title(), envModel.description(), envModel.status(), updatedVars);
        envs.set(index, updatedEnvModel);
        rebuildIdIndex();
        notifyListeners();

    }

    public void updateEnvVariable(UUID envId, UUID oldEnvVariableId, EnvVariableModel updatedEnvVariable) {
        Integer index = idIndex.get(envId);
        if(index == null) return;
        EnvModel envModel = envs.get(index);
        List<EnvVariableModel> updatedVars = new ArrayList<>(envModel.variables());
        int selectedVarIndex = findVarIndexById(updatedVars, oldEnvVariableId);
        if(selectedVarIndex < 0) {
            this.addEnvVariable(envId, updatedEnvVariable);
            return;
        }
        updatedVars.set(selectedVarIndex, updatedEnvVariable);
        EnvModel updatedEnvModel = new EnvModel(envModel.title(), envModel.description(), envModel.status(), updatedVars);
        envs.set(index, updatedEnvModel);
        rebuildIdIndex();
        notifyListeners();
    }

    public void removeEnvVariable(UUID envId, UUID envVariableId) {
        Integer index = idIndex.get(envId);
        if(index == null) return;
        EnvModel envModel = envs.get(index);
        var vars = envModel.variables();
        int selectedVarIndex = findVarIndexById(vars, envVariableId);
        if(selectedVarIndex < 0) return;
        List<EnvVariableModel> updatedVars = new ArrayList<>(envModel.variables());
        updatedVars.remove(selectedVarIndex);
        EnvModel updatedEnvModel = new EnvModel(envModel.title(), envModel.description(), envModel.status(), updatedVars);
        envs.set(index, updatedEnvModel);
        notifyListeners();
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
