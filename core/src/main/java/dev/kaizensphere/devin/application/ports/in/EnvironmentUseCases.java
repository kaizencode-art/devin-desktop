package dev.kaizensphere.devin.application.ports.in;

import java.util.UUID;

public interface EnvironmentUseCases {
    void createNewEnvironment();
    void createNewEnvironmentVariable(UUID envId);
    void importEnvironment();
    void exportEnvironment(UUID envId);
    void deleteEnvironment(UUID envId);
    void updateEnvironment(UUID id, String title, String description);
    void updateEnvVariable(UUID envId, UUID varId, String name, String value);
    void deleteEnvVariable(UUID envId, UUID varId);
    void openRecentEnvironment();
}
