package me.xra1ny.essentia.database.test;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import me.xra1ny.essentia.database.test.database.TestDatabase;
import me.xra1ny.essentia.database.test.entity.TestEntity;
import me.xra1ny.essentia.database.test.repository.TestRepository;

import java.util.List;
import java.util.Optional;

@Log
public class EssentiaDatabaseTest {
    @SneakyThrows
    public static void main(String[] args) {
        final TestDatabase testDatabase = new TestDatabase();
        final TestRepository testRepository = new TestRepository();

        testDatabase.registerRepository(testRepository);
        testDatabase.enable();

        final List<TestEntity> testEntities = testRepository.findAll();

        log.info("testEntities: %s"
                .formatted(testEntities));

        final Optional<TestEntity> optionalTestEntity = testRepository.findByValue("username", "name1");

        log.info(optionalTestEntity.toString());
    }
}
