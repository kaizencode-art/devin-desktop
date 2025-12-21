package dev.kaizensphere.devin.records.storage;

import dev.kaizensphere.devin.model.EnvModel;
import dev.kaizensphere.devin.persistence.EnvRepository;
import org.eclipse.store.storage.embedded.types.EmbeddedStorage;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

public class EclipseStoreEnvAdapter implements EnvRepository {

    private final EmbeddedStorageManager storageManager;
    private final DataRoot root = new DataRoot();

    public EclipseStoreEnvAdapter(Path storagePath) {
        this.storageManager = EmbeddedStorage.start(root, storagePath);
    }

    @Override
    public void saveAll(Collection<EnvModel> envs) {
        root.setEnviroments(List.copyOf(envs));
        storageManager.store(root);
    }

    @Override
    public Collection<EnvModel> loadAll() {
        return root.getEnviroments();
    }

    @Override
    public void close() {
        storageManager.shutdown();
    }
}
