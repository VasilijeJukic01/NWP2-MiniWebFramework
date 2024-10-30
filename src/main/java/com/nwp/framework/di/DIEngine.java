package com.nwp.framework.di;

import com.nwp.annotations.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static com.nwp.config.Config.*;

public class DIEngine {

    private static DIEngine instance;

    private final DIContainer container;
    private final Map<String, Object> singletons;

    private DIEngine() {
        this.container = new DIContainer();
        this.singletons = new HashMap<>();
        // Registering
        this.container.registerQualifiers();
    }

    public static DIEngine getInstance() {
        return Optional.ofNullable(instance).orElseGet(() -> {
            instance = new DIEngine();
            return instance;
        });
    }

    private Object returnClassInstance(Class<?> clazz) {
        return Optional.ofNullable(singletons.get(clazz.getName()))
                .orElseGet(() -> storeSingleton(clazz));
    }

    private Object storeSingleton(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object obj = constructor.newInstance();
            singletons.put(clazz.getName(), obj);
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Initialization
    public void initialize(String controllerName) throws ClassNotFoundException {
        System.out.println(COL_PURPLE + "Initializing controller" + COL_DEFAULT + ": " + controllerName);

        Class<?> clazz = Class.forName(controllerName);
        Object controller = returnClassInstance(clazz);
        initializeFields(controller, clazz.getDeclaredFields());
    }

    private void initializeFields(Object parent, Field[] children) {
        Arrays.stream(children)
                .filter(field -> field.isAnnotationPresent(Autowired.class))
                .forEach(field -> initializeField(parent, field));
    }

    private void initializeField(Object parent, Field field) {
        try {
            System.out.println(COL_BRIGHT_CYAN + "Initializing field" + COL_DEFAULT + ": " + field.getName());

            Class<?> clazz = resolveFieldClass(field);
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object obj = resolveInstance(constructor, clazz);

            // Inject
            field.setAccessible(true);
            field.set(parent, obj);

            verbose(field, parent, obj);

            // Recursion
            initializeFields(obj, clazz.getDeclaredFields());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Class<?> resolveFieldClass(Field field) throws ClassNotFoundException {
        if (field.getType().isInterface() && !field.isAnnotationPresent(Qualifier.class)) {
            throw new RuntimeException("Field of type Interface is annotated with @Autowired but not with @Qualifier: " + field.getName());
        }
        return field.getType().isInterface()
                ? Optional.ofNullable(field.getAnnotation(Qualifier.class))
                    .map(qualifier -> container.getImplementation(qualifier.value()))
                    .orElseThrow(() -> new RuntimeException("No implementation found for field: " + field.getName()))
                : Class.forName(field.getType().getName());
    }

    private Object resolveInstance(Constructor<?> constructor, Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if (clazz.isAnnotationPresent(Bean.class)) {
            return clazz.getAnnotation(Bean.class).scope().equals("singleton") ? returnClassInstance(clazz) : constructor.newInstance();
        }
        else if (clazz.isAnnotationPresent(Service.class)) {
            return returnClassInstance(clazz);
        }
        else if (clazz.isAnnotationPresent(Component.class)) {
            return constructor.newInstance();
        }
        throw new RuntimeException("Field annotated with @Autowired is not a @Bean, @Service or @Component: " + clazz.getName());
    }

    private void verbose(Field field, Object parent, Object obj) {
        Optional.ofNullable(field.getAnnotation(Autowired.class))
                .filter(Autowired::verbose)
                .ifPresent(autowired -> System.out.println(COL_YELLOW + "Initialized " + obj.getClass().getSimpleName() + " " +
                        field.getName() + " in " + parent.getClass().getSimpleName() + " on " + new Date() + " with " + obj.hashCode() + COL_DEFAULT));
    }

}
