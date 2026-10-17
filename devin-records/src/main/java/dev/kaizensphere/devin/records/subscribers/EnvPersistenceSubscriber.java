package dev.kaizensphere.devin.records.subscribers;

import dev.kaizensphere.devin.application.ports.out.EnvRepository;import dev.kaizensphere.devin.domain.store.InMemoryEnvStore;import dev.kaizensphere.devin.domain.store.EnvStoreListener;

public class EnvPersistenceSubscriber implements EnvStoreListener {

    private final InMemoryEnvStore store;
    private final EnvRepository repository;

    public EnvPersistenceSubscriber(InMemoryEnvStore store, EnvRepository repository) {
        this.store = store;
        this.repository = repository;
    }

    @Override
    public void onEnvironmentChanged(Change change) {
        repository.saveAll(store.getEnvs());
    }
}
