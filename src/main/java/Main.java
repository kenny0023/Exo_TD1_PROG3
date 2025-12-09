import java.time.Instant;

public class Main {
    public static void main(String[] args) {
        DataRetriever r = new DataRetriever();

        try {
            System.out.println("=== Toutes les catégories ===");
            r.getAllCategories().forEach(System.out::println);

            System.out.println("\n=== Pagination ===");
            r.getProductList(1, 10).forEach(p -> System.out.println("p10: " + p));
            r.getProductList(1, 5).forEach(p -> System.out.println("p5: " + p));
            r.getProductList(1, 3).forEach(p -> System.out.println("p3: " + p));
            r.getProductList(2, 2).forEach(p -> System.out.println("p2-2: " + p));

            System.out.println("\n=== Filtres simples ===");
            r.getProductsByCriteria("Dell", null, null, null).forEach(System.out::println);
            r.getProductsByCriteria(null, "info", null, null).forEach(System.out::println);
            r.getProductsByCriteria("iPhone", "mobile", null, null).forEach(System.out::println);
            r.getProductsByCriteria(null, null,
                    Instant.parse("2024-02-01T00:00:00Z"),
                    Instant.parse("2024-03-01T23:59:59Z")).forEach(System.out::println);
            r.getProductsByCriteria("Samsung", "bureau", null, null).forEach(System.out::println);
            r.getProductsByCriteria("Sony", "informatique", null, null).forEach(System.out::println);
            r.getProductsByCriteria(null, "audio",
                    Instant.parse("2024-01-01T00:00:00Z"),
                    Instant.parse("2024-12-01T23:59:59Z")).forEach(System.out::println);
            r.getProductsByCriteria(null, null, null, null).forEach(System.out::println);

            System.out.println("\n=== Filtres + Pagination ===");
            r.getProductsByCriteria(null, null, null, null, 1, 10)
                    .forEach(p -> System.out.println("Tous p1/10: " + p));
            r.getProductsByCriteria("Dell", null, null, null, 1, 5)
                    .forEach(p -> System.out.println("Dell p1/5: " + p));
            r.getProductsByCriteria(null, "informatique", null, null, 1, 10)
                    .forEach(p -> System.out.println("Info p1/10: " + p));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}