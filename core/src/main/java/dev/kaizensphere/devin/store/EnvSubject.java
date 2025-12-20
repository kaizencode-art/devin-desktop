package dev.kaizensphere.devin.store;

public interface EnvSubject {

    void registerListener(dev.kaizensphere.devin.store.EnvStoreListener listener);

    void removeListener(dev.kaizensphere.devin.store.EnvStoreListener listener);
}
