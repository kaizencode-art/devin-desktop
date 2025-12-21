package dev.kaizensphere.devin.records.storage;

import dev.kaizensphere.devin.model.EnvModel;

import java.util.List;

class DataRoot {
    private List<EnvModel> enviroments;

    public List<EnvModel> getEnviroments() {
        return enviroments;
    }

    public void setEnviroments(List<EnvModel> enviroments) {
        this.enviroments = enviroments;
    }

}