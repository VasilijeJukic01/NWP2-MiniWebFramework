package com.nwp.framework.discovery;

import java.util.Objects;

public class Route {

    private String route;
    private String method;
    private Class<?> controller;
    private String name;

    public Route(String route, String method, Class<?> controller, String name) {
        this.route = route;
        this.method = method;
        this.controller = controller;
        this.name = name;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?> getController() {
        return controller;
    }

    public void setController(Class<?> controller) {
        this.controller = controller;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route1 = (Route) o;
        return Objects.equals(route, route1.route) && Objects.equals(method, route1.method) && Objects.equals(controller, route1.controller) && Objects.equals(name, route1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(route, method, controller, name);
    }

    @Override
    public String toString() {
        return "Route{" +
                "route='" + route + '\'' +
                ", method='" + method + '\'' +
                ", controller=" + controller +
                ", name='" + name + '\'' +
                '}';
    }
}
