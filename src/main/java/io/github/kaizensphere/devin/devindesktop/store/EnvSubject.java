package io.github.kaizensphere.devin.devindesktop.store;

public interface EnvSubject {

    void registerListener(EnvStoreListener listener);

    void removeListener(EnvStoreListener listener);
}
