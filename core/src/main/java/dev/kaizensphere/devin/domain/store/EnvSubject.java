package dev.kaizensphere.devin.domain.store;

public interface EnvSubject {

    void registerListener(EnvStoreListener listener);

    void removeListener(EnvStoreListener listener);
}
