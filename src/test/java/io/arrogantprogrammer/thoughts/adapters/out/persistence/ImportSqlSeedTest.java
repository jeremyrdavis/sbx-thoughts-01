package io.arrogantprogrammer.thoughts.adapters.out.persistence;

import io.arrogantprogrammer.thoughts.domain.ThoughtStatus;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Issue #1: import.sql in src/main/resources is picked up on application start.
 * Runs under its own profile so the application restarts with a freshly seeded schema,
 * independent of other tests that delete rows.
 */
@QuarkusTest
@TestProfile(ImportSqlSeedTest.FreshSeedProfile.class)
class ImportSqlSeedTest {

    public static class FreshSeedProfile implements QuarkusTestProfile {
        @Override
        public Map<String, String> getConfigOverrides() {
            return Map.of("quarkus.hibernate-orm.database.generation", "drop-and-create");
        }
    }

    @Test
    @TestTransaction
    void importSqlPreloadsAllNinetyNineQuotesOnStartup() {
        assertEquals(99, ThoughtEntity.count());
    }

    @Test
    @TestTransaction
    void importSqlPreloadsEveryQuoteAsApproved() {
        assertEquals(99, ThoughtEntity.count("status", ThoughtStatus.APPROVED));
    }

    @Test
    @TestTransaction
    void importSqlPreloadsAuthorAndContentForQuotes() {
        ThoughtEntity twain = ThoughtEntity.find("author", "Mark Twain").firstResult();
        assertTrue(twain.content.startsWith("You can't depend on your judgment"));
        assertEquals("American humorist", twain.authorBio);
    }
}
