package me.xra1ny.essentia.schedule;

import lombok.NonNull;
import lombok.extern.java.Log;
import me.xra1ny.essentia.schedule.annotation.Scheduled;
import me.xra1ny.essentia.schedule.exception.ScheduledMethodInvalidSignatureException;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Log
public class EssentiaSchedule {
    /**
     * Initialises EssentiaSchedule on the given project's main class
     *
     * @param mainClass Your main class
     */
    public static void run(@NonNull Class<?> mainClass) {
        final List<Method> scheduledMethodsList = new Reflections(mainClass, new MethodAnnotationsScanner()).getMethodsAnnotatedWith(Scheduled.class).stream()
                .filter(method -> {

                    if (method.getParameterCount() != 0) {
                        throw new ScheduledMethodInvalidSignatureException(method);
                    }

                    if (!Modifier.isStatic(method.getModifiers())) {
                        throw new ScheduledMethodInvalidSignatureException(method);
                    }

                    return true;
                })
                .toList();

        scheduledMethodsList
                .forEach(method -> {
                    final Scheduled scheduled = method.getAnnotation(Scheduled.class);

                    new Timer().schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    try {
                                        method.invoke(this);
                                    } catch (IllegalAccessException | InvocationTargetException e) {
                                        log.severe("error while invoking scheduled method %s"
                                                .formatted(method.getName()));
                                    }
                                }
                            }, scheduled.value(), scheduled.value()
                    );
                });
    }
}
