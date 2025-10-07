package core.jdbc;

import jwp.controller.Controller;

import java.util.HashMap;
import java.util.Map;

public final class RequestMapper {
    private final Map<String, Controller> mappings = new HashMap<>();

    public void register(String path, Controller controller) {
        mappings.put(path, controller);
    }

    public Controller getController(String path) {
        return mappings.get(path);
    }
}
