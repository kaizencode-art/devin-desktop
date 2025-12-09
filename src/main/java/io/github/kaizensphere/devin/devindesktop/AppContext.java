package io.github.kaizensphere.devin.devindesktop;

import io.github.kaizensphere.devin.devindesktop.models.EnvModel;
import io.github.kaizensphere.devin.devindesktop.models.EnvVariableModel;
import io.github.kaizensphere.devin.devindesktop.observablemodel.SelectedEnvFx;
import io.github.kaizensphere.devin.devindesktop.store.EnvStore;
import io.github.kaizensphere.devin.devindesktop.store.EnvStoreFx;

import java.util.List;

public class AppContext {
    public static final String APP_NAME = "Devin Desktop";
    public static final String APP_VERSION = "0.0.1";
    public static final String APP_AUTHOR = "<NAME>";
    public static final String APP_URL = "https://github.com/kaizensphere/devin-desktop";
    public static final String APP_LICENSE = "MIT";

    // DI setup for the environnement
    public static final EnvStore envStore = new EnvStore();
    public static final EnvStoreFx envStoreFx = new EnvStoreFx(envStore);
    public static final SelectedEnvFx selectedEnvFx = SelectedEnvFx.init(envStore);

    public static void run() {
        EnvVariableModel[] envVariables = new EnvVariableModel[3];
        envVariables[0] = new EnvVariableModel("env1", "env1");
        envVariables[1] = new EnvVariableModel("env2", "env2");
        envVariables[2] = new EnvVariableModel("env3", "env3");
        envStore.addEnv(new EnvModel("Titre 1", "Description 1", EnvModel.Status.VALID, List.of(envVariables)));
        envStore.addEnv(new EnvModel("Titre 2", "Description 2", EnvModel.Status.WARNING));
        envStore.addEnv(new EnvModel("Titre 3", "Description 3", EnvModel.Status.INVALID));
    }
}
