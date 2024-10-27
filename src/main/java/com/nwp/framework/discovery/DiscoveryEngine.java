package com.nwp.framework.discovery;

import com.nwp.annotations.Controller;
import com.nwp.annotations.GET;
import com.nwp.annotations.Path;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DiscoveryEngine {

    private static final DiscoveryEngine instance = new DiscoveryEngine();
    private final List<Route> routes;

    private DiscoveryEngine() {
        this.routes = init();
    }

    public static DiscoveryEngine getInstance() {
        return instance;
    }

    // Lets go
    private List<Route> init() {
        // Filter Controllers and their methods
        return ScanUtils.findAllClasses("com.nwp.app").stream()
                .filter(clazz -> clazz.isAnnotationPresent(Controller.class))
                .flatMap(clazz -> Stream.of(clazz.getMethods())
                        .filter(method -> method.isAnnotationPresent(Path.class))
                        .map(method -> createRoute(clazz, method))
                )
                .distinct()
                .collect(Collectors.toList());
    }

    private Route createRoute(Class<?> clazz, Method method) {
        String httpMethod = method.isAnnotationPresent(GET.class) ? "GET" : "POST";
        String path = method.getAnnotation(Path.class).path();

        return new Route(path, httpMethod, clazz, method.toString());
    }

    public List<Route> getRoutes() {
        System.out.println("Discovery Engine Routes:");
        routes.forEach(System.out::println);
        return routes;
    }
}
