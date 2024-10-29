package com.nwp.framework.di;

import com.nwp.annotations.Qualifier;
import com.nwp.framework.discovery.ScanUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DIContainer {

    private final Map<String, Class<?>> implementations;

    public DIContainer() {
        this.implementations = new HashMap<>();
    }

    // Register all classes in the package with the Qualifier
    public void registerQualifiers() {
        ScanUtils.findAllClasses("com.nwp.app").stream()
                .filter(clazz -> clazz.isAnnotationPresent(Qualifier.class))
                .forEach(clazz -> {
                    Qualifier qualifier = clazz.getAnnotation(Qualifier.class);
                    if (implementations.containsKey(qualifier.value())) {
                        throw new RuntimeException("Multiple @Bean classes with the same @Qualifier value: " + qualifier.value());
                    }
                    implementations.put(qualifier.value(), clazz);
                });
    }

    public Class<?> getImplementation(String qualifier) {
        return Optional.ofNullable(implementations.get(qualifier))
                .orElseThrow(() -> new RuntimeException("No implementation found for qualifier: " + qualifier));
    }
}
