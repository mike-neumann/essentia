package me.xra1ny.essentia.configs;

import lombok.NonNull;
import me.xra1ny.essentia.configs.annotation.ConfigInfo;
import me.xra1ny.essentia.configs.processor.FileProcessor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

/**
 * @apiNote Must be annotated with {@link ConfigInfo}.
 * @param <T>
 */
public abstract class Config<T> {
    /**
     * Defines the processor for this config.
     */
    @NonNull
    private FileProcessor<T> fileProcessor;

    public Config() {
        final ConfigInfo info = Optional.ofNullable(getClass().getAnnotation(ConfigInfo.class))
                .orElseThrow(() -> new RuntimeException("Config needs to be annotated with @ConfigInfo!"));

        load(info.value(), (Class<? extends FileProcessor<T>>) info.processor());
    }

    public void save() {
        try {
            fileProcessor.save(fileProcessor.serialize(this));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void load(@NonNull String fileName, @NonNull Class<? extends FileProcessor<T>> processor) {
        try {
            final File file = createFile(fileName);

            // attempt to create default processor instance.
            final Constructor<? extends FileProcessor<T>> defaultConstructor = processor.getDeclaredConstructor(File.class);

            fileProcessor = defaultConstructor.newInstance(file);

            try {
                // after everything has worked without problem, inject field of our config with the values now retrievable...
                injectFields(fileProcessor.load(getClass()));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            throw new RuntimeException("error while creating config file processor " + processor.getSimpleName() + " for config " + fileName);
        }
    }

    @NonNull
    private File createFile(@NonNull String fileName) {
        final File file = new File(fileName);
        if (!file.exists()) {
            if (file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }

            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException("error while creating config file " + fileName);
            }
        }

        return file;
    }

    private void injectFields(@NonNull Map<String, T> serializedContentMap) {
        serializedContentMap
                .forEach((key, value) -> {
                    final Optional<Field> optionalField = fileProcessor.getFieldByProperty(getClass(), key);

                    optionalField.ifPresent(field -> injectField(field, value));
                });
    }

    private void injectField(@NonNull Field field, @NonNull Object value) {
        try {
            // when the value we are trying to inject is an instance of a map, we want to deserialize it to a complex object.
            if (value instanceof Map<?, ?> map) {
                try {
                    value = fileProcessor.deserialize((Map<String, T>) map, (Class<Object>) value.getClass());
                } catch (Exception e) {
                    throw new RuntimeException("error while deserializing " + value.getClass().getSimpleName());
                }
            }

            field.set(this, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("error while injecting field " + field.getName() + " with " + value);
        }
    }
}
