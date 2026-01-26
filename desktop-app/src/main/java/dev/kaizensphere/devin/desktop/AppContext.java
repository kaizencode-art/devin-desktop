package dev.kaizensphere.devin.desktop;

import dev.kaizensphere.devin.application.ports.in.EnvironmentUseCases;
import dev.kaizensphere.devin.application.services.EnvironmentService;
import dev.kaizensphere.devin.desktop.observablemodel.SelectedEnvFx;
import dev.kaizensphere.devin.application.ports.out.EnvRepository;
import dev.kaizensphere.devin.records.storage.EclipseStoreEnvAdapter;
import dev.kaizensphere.devin.records.subscribers.EnvPersistenceSubscriber;
import dev.kaizensphere.devin.domain.store.EnvStore;
import dev.kaizensphere.devin.desktop.store.EnvStoreFx;
import java.nio.file.Path;
import java.util.ArrayList;

public class AppContext {
    public static final String APP_NAME = "Devin Desktop";
    public static final String APP_VERSION = "0.0.1";
    public static final String APP_AUTHOR = "<NAME>";
    public static final String APP_URL = "https://github.com/kaizensphere/devin-desktop";
    public static final String APP_LICENSE = "MIT";


    // DI setup for the environnement
    public static final EnvRepository repository = createRepository();



    public static final EnvStore envStore = createEnvStore();
    public static final EnvironmentUseCases environmentService = createEnvironmentService();
    public static final EnvStoreFx envStoreFx = new EnvStoreFx(envStore);
    public static final SelectedEnvFx selectedEnvFx = SelectedEnvFx.init(envStore);

    private static EnvironmentUseCases createEnvironmentService() {
        return new EnvironmentService(envStore);
    }

    private static EnvRepository createRepository() {
        String storagePathStr = System.getenv("DEVIN_STORAGE_PATH");
        if(storagePathStr == null) {
            storagePathStr = System.getProperty("user.home") + "/.devin/storage";
        }
        return new EclipseStoreEnvAdapter(Path.of(storagePathStr));
    }

    private static EnvStore createEnvStore() {
        var loaded = repository.loadAll();
        if(loaded != null) return new EnvStore(new ArrayList<>(loaded));
        return new EnvStore();
    }

    public static void run() {
        envStore.registerListener(new EnvPersistenceSubscriber(envStore, repository));
    }
}
