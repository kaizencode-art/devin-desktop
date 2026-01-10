package dev.kaizensphere.devin.records.storage;

import dev.kaizensphere.devin.domain.model.EnvModel;

import java.util.ArrayList;
import java.util.List;

class DataRoot {
    private List<EnvModel> environments;


    public List<EnvModel> getEnvironments() {
        return this.environments;
    }

    public void setEnvironments(List<EnvModel> environments) {
        this.environments = environments;
    }

}