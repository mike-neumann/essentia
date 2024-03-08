package me.xra1ny.essentia.configs;

import lombok.NonNull;
import lombok.extern.java.Log;
import me.xra1ny.essentia.configs.annotation.ConfigInfo;
import me.xra1ny.essentia.configs.processor.FileProcessor;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;

/**
 * @apiNote Must be annotated with {@link ConfigInfo}.
 */
@Log
public abstract class Config {
    /**
     * Defines the processor for this config.
     */
    @NonNull
    private FileProcessor fileProcessor;

    public Config() {
        final ConfigInfo info = Optional.ofNullable(getClass().getAnnotation(ConfigInfo.class))
                .orElseThrow(() -> new RuntimeException("config needs to be annotated with @ConfigInfo!"));

        load(info.name(), info.processor());
    }

    public void save() {
        try {
            fileProcessor.save(fileProcessor.serialize(this));
        } catch (Exception e) {
            log.severe("error while saving config");
        }
    }

    private void load(@NonNull String fileName, @NonNull Class<? extends FileProcessor> processor) {
        try {
            final File file = createFile(fileName);

            // attempt to create default processor instance.
            final Constructor<? extends FileProcessor> defaultConstructor = processor.getDeclaredConstructor(File.class);

            fileProcessor = defaultConstructor.newInstance(file);

            try {
                // after everything has worked without problem, inject field of our config with the values now retrievable...
                injectFields(fileProcessor.load(getClass()));
            } catch (Exception e) {
                log.severe("error while injecting fields for config %s with processor %s"
                        .formatted(fileName, processor.getSimpleName()));
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException e) {
            log.severe("error while creating config file processor %s for config %s"
                    .formatted(processor.getSimpleName(), fileName));
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
                final boolean fileCreated = file.createNewFile();

                if(fileCreated) {
                    log.info("%s config file created"
                            .formatted(fileName));
                }
            } catch (IOException e) {
                log.severe("error while creating config file %s"
                        .formatted(fileName));
            }
        }

        return file;
    }

    private void injectFields(@NonNull Map<String, ?> serializedContentMap) {
        serializedContentMap
                .forEach((key, value) -> {
                    final Optional<Field> optionalField = fileProcessor.getFieldByProperty(getClass(), key);

                    optionalField.ifPresent(field -> injectField(field, value));
                });
    }

    private void injectField(@NonNull Field field, @Nullable Object value) {
        try {
            field.set(this, value);
        } catch (IllegalAccessException e) {
            log.severe("error while injecting field %s with %s"
                    .formatted(field.getName(), String.valueOf(value)));
        }
    }
}
