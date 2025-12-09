import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataRetrieverTest {

    private static DataRetriever r;

    @BeforeAll
    static void setup() {
        r = new DataRetriever();
    }

    @Test void testCategories() throws Exception {
        assertEquals(7, r.getAllCategories().size());
    }

    @Test void testPagination() throws Exception {
        assertEquals(2, r.getProductList(1, 3).size());
        assertEquals(2, r.getProductList(2, 2).size());
    }

    @Test void testFilterByName() throws Exception {
        assertEquals(1, r.getProductsByCriteria("Dell", null, null, null).size());
    }

    @Test void testFilterByCategory() throws Exception {
        List<Product> info = r.getProductsByCriteria(null, "info", null, null);
        assertTrue(info.size() >= 2);
        assertTrue(info.stream().anyMatch(p -> p.getName().contains("Dell")));
    }

    @Test void testFilterByDateRange() throws Exception {
        var febMar = r.getProductsByCriteria(null, null,
                Instant.parse("2024-02-01T00:00:00Z"),
                Instant.parse("2024-03-01T23:59:59Z"));
        assertEquals(2, febMar.size());
    }

    @Test void testMultipleCategories() throws Exception {
        var iphone = r.getProductsByCriteria("iPhone", null, null, null).get(0);
        assertEquals(2, iphone.getCategories().size());
        assertTrue(iphone.getCategories().contains("Mobile"));
    }

    @Test void testCombinedWithPagination() throws Exception {
        assertEquals(1, r.getProductsByCriteria("Dell", null, null, null, 1, 5).size());
    }
}
