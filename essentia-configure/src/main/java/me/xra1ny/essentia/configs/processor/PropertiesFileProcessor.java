package me.xra1ny.essentia.configs.processor;

import lombok.Getter;
import lombok.NonNull;
import me.xra1ny.essentia.configs.annotation.Property;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;


public class PropertiesFileProcessor implements FileProcessor<String> {
    @NonNull
    private final File file;

    @Getter
    @NonNull
    private final Properties properties = new Properties();

    public PropertiesFileProcessor(@NonNull File file) {
        this.file = file;
    }

    @Override
    public Map<String, String> load(@NonNull Class<?> type) throws Exception {
        properties.load(new FileReader(file));

        return Map.ofEntries(properties.entrySet().toArray(Map.Entry[]::new));
    }

    @Override
    public String read(@NonNull String key) {
        return properties.getProperty(key);
    }

    @Override
    public String read(@NonNull String key, @NonNull String def) {
        return properties.getProperty(key, def);
    }

    @Override
    public void write(@NonNull Map<String, String> serializedContentMap) {
        serializedContentMap.forEach(this::write);
    }

    @Override
    public void write(@NonNull Object object) {
        write(serialize(object));
    }

    @Override
    public void write(@NonNull String key, @NonNull String value) {
        properties.setProperty(key, value);
    }

    @Override
    public void save(@NonNull Map<String, String> serializedContentMap) throws Exception {
        serializedContentMap
                .forEach(properties::setProperty);

        properties.store(new FileWriter(file), null);
    }

    @Override
    public Map<String, String> serialize(@NonNull Object object) {
        return Map.ofEntries(getPropertyFieldList(object.getClass()).stream()
                .map(field -> {
                    try {
                        final Optional<Property> optionalProperty = Optional.ofNullable(field.getAnnotation(Property.class));

                        if(optionalProperty.isPresent()) {
                            return Map.entry(optionalProperty.get().value(), field.get(object));
                        }

                        return Map.entry(field.getName(), field.get(object));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(Map.Entry[]::new));
    }

    @Override
    public Object deserialize(@NonNull Map<String, String> serializedContentMap, @NonNull Class<Object> type) throws Exception {
        try {
            final Constructor<?> defaultConstructor = type.getConstructor();
            final Object object = defaultConstructor.newInstance();

            // default constructor was found, inject field properties...
            serializedContentMap
                    .forEach((key, value) -> {
                        final Optional<Field> optionalField = getFieldByProperty(type, key);

                        optionalField.ifPresent(field -> {
                            try {
                                field.set(object, value);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    });

            return object;
        }catch(NoSuchMethodException e) {
            // default constructor not found, attempt to get constructor matching properties...
            final Constructor<?> constructor = type.getConstructor(getPropertyFieldList(type).stream().map(Object::getClass).toArray(Class[]::new));

            // constructor found, create new instance with this constructor...

            return constructor.newInstance(serializedContentMap.values());
        }
    }
}
