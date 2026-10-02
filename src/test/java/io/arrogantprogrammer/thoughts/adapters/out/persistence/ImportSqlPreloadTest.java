package io.arrogantprogrammer.thoughts.adapters.out.persistence;

import io.arrogantprogrammer.thoughts.domain.ThoughtStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// A dedicated profile restarts Quarkus, so import.sql runs against a fresh schema
// regardless of what other tests did to the shared database.
@QuarkusTest
@TestProfile(ImportSqlPreloadTest.FreshStart.class)
class ImportSqlPreloadTest {

    public static class FreshStart implements QuarkusTestProfile {
    }

    @Test
    void importSqlIsPickedUpOnStartAndPreloadsTheDatabase() {
        assertEquals(99, ThoughtEntity.count());
    }

    @Test
    void preloadedThoughtsAreAllApproved() {
        assertEquals(0, ThoughtEntity.count("status <> ?1", ThoughtStatus.APPROVED));
    }

    @Test
    void preloadedThoughtsHaveContentAndAuthor() {
        assertEquals(0, ThoughtEntity.count("content is null or author is null or length(content) < 10"));
        assertTrue(ThoughtEntity.count("author = 'Charles Bukowski'") >= 1);
    }
}
