package dev.kaizensphere.devin.records.subscribers;

import dev.kaizensphere.devin.application.ports.out.EnvRepository;import dev.kaizensphere.devin.domain.store.EnvStore;import dev.kaizensphere.devin.domain.store.EnvStoreListener;

public class EnvPersistenceSubscriber implements EnvStoreListener {

    private final EnvStore store;
    private final EnvRepository repository;

    public EnvPersistenceSubscriber(EnvStore store, EnvRepository repository) {
        this.store = store;
        this.repository = repository;
    }

    @Override
    public void onEnvironmentChanged(Change change) {
        repository.saveAll(store.getEnvs());
    }
}
