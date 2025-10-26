package io.github.kaizensphere.devin.devindesktop.store;

public interface EnvSubject {

    public void registerListener(EnvStoreListener listener);

    public void removeListener(EnvStoreListener listener);
}
