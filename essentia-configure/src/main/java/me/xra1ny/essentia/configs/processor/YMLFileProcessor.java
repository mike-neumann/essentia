package me.xra1ny.essentia.configs.processor;

import lombok.Getter;
import lombok.NonNull;
import me.xra1ny.essentia.configs.annotation.Property;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class YMLFileProcessor implements FileProcessor<Object> {
    @NonNull
    private final File file;

    @Getter
    @NonNull
    private final Yaml yaml;

    @NonNull
    private final Map<String, Object> data = new HashMap<>();

    public YMLFileProcessor(@NonNull File file) {
        this.file = file;

        final LoaderOptions loaderOptions = new LoaderOptions();

        loaderOptions.setTagInspector(tag -> true);
        yaml = new Yaml(loaderOptions);
    }

    private void addTypeDescriptor(@NonNull Class<?> type) {
        for(Field field : getPropertyFieldList(type)) {
            yaml.addTypeDescription(new TypeDescription(field.getType(), "!" + field.getType().getSimpleName()));

            addTypeDescriptor(field.getType());
        }
    }

    @Override
    public Map<String, Object> load(@NonNull Class<?> type) throws Exception {
        data.clear();

        // add type descriptors for complex types...
        addTypeDescriptor(type);

        final Map<String, Object> data = yaml.load(new FileReader(file));

        if(data != null) {
            this.data.putAll(data);
        }

        return this.data;
    }

    @Override
    public Object read(@NonNull String key) {
        return data.get(key);
    }

    @Override
    public Object read(@NonNull String key, @NonNull Object def) {
        return data.getOrDefault(key, def);
    }

    @Override
    public void write(@NonNull Map<String, Object> serializedContentMap) {
        data.putAll(serializedContentMap);
    }

    @Override
    public void write(@NonNull Object object) {

    }

    @Override
    public void write(@NonNull String key, @NonNull Object value) throws Exception {
        data.put(key, serialize(value));
    }

    @Override
    public void save(@NonNull Map<String, Object> serializedContentMap) throws Exception {
        data.putAll(serializedContentMap);
        yaml.dump(data, new FileWriter(file));
    }

    @Override
    public Map<String, Object> serialize(@NonNull Object object) throws Exception {
        return Map.ofEntries(getPropertyFieldList(object.getClass()).stream()
                .map(field -> {
                    try {
                        final Optional<Property> optionalProperty = Optional.ofNullable(field.getAnnotation(Property.class));

                        if(optionalProperty.isPresent()) {
                            final Property property = optionalProperty.get();

                            return Map.entry(property.value(), field.get(object));
                        }

                        return Map.entry(field.getName(), field.get(object));
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(Map.Entry[]::new));
    }

    @Override
    public Object deserialize(@NonNull Map<String, Object> serializedContentMap, @NonNull Class<Object> type) throws Exception {
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
