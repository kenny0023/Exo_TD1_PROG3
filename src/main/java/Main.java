import java.time.Instant;

public class Main {
   public static void main(String[] args) {
       DataRetriever retriever = new DataRetriever();

       try {
           System.out.println("=== Toutes les catégories ===");
           retriever.getAllCategories().forEach(System.out::println);

           System.out.println("\n=== Pagination ===");
           testPagination(retriever);

           System.out.println("\n=== Filtres simples ===");
           testSimpleFilters(retriever);

           System.out.println("\n=== Filtres + Pagination ===");
           testFiltersWithPagination(retriever);

       } catch (Exception e) {
           e.printStackTrace();
       }
   }

   private static void testPagination(DataRetriever r) throws Exception {
       int[][] tests = {{1,10}, {1,5}, {1,3}, {2,2}};
       for (int[] t : tests) {
           System.out.println("\n--- Page " + t[0] + ", Size " + t[1] + " ---");
           r.getProductList(t[0], t[1]).forEach(System.out::println);
       }
   }

   private static void testSimpleFilters(DataRetriever r) throws Exception {
       Object[][] tests = {
               {"Dell", null, null, null},
               {null, "info", null, null},
               {"iPhone", "mobile", null, null},
               {null, null, Instant.parse("2024-02-01T00:00:00Z"), Instant.parse("2024-03-01T23:59:59Z")},
               {"Samsung", "bureau", null, null},
               {"Sony", "informatique", null, null},
               {null, "audio", Instant.parse("2024-01-01T00:00:00Z"), Instant.parse("2024-12-01T23:59:59Z")},
               {null, null, null, null}
       };

       for (Object[] t : tests) {
           System.out.printf("\n--- Filtre: product='%s', category='%s', min=%s, max=%s ---\n",
                   t[0], t[1], t[2], t[3]);
           r.getProductsByCriteria((String)t[0], (String)t[1], (Instant)t[2], (Instant)t[3])
                   .forEach(System.out::println);
       }
   }

   private static void testFiltersWithPagination(DataRetriever r) throws Exception {
       r.getProductsByCriteria(null, null, null, null, 1, 10)
               .forEach(p -> System.out.println("Tous (page 1/10): " + p));

       System.out.println("\n--- Dell (page 1, size 5) ---");
       r.getProductsByCriteria("Dell", null, null, null, 1, 5)
               .forEach(System.out::println);

       System.out.println("\n--- Catégorie 'informatique' (page 1, size 10) ---");
       r.getProductsByCriteria(null, "informatique", null, null, 1, 10)
               .forEach(System.out::println);
   }
}
