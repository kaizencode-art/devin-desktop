package dev.kaizensphere.devin.records.subscribers;

import dev.kaizensphere.devin.persistence.EnvRepository;import dev.kaizensphere.devin.store.EnvStore;import dev.kaizensphere.devin.store.EnvStoreListener;

public class EnvPersistenceSubscriber implements EnvStoreListener {

    private final EnvStore store;
    private final EnvRepository repository;

    public EnvPersistenceSubscriber(EnvStore store, EnvRepository repository) {
        this.store = store;
        this.repository = repository;
    }

    @Override
    public void onEnvironmentChanged() {
        repository.saveAll(store.getEnvs());
    }
}
