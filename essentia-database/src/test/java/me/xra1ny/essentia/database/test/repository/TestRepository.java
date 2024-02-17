package me.xra1ny.essentia.database.test.repository;

import me.xra1ny.essentia.database.Repository;
import me.xra1ny.essentia.database.test.entity.TestEntity;

public class TestRepository extends Repository<TestEntity, Long> {
    @Override
    public Class<TestEntity> managedEntityType() {
        return TestEntity.class;
    }
}
