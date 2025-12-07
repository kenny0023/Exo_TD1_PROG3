import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DataRetriever {

   public List<Category> getAllCategories() throws SQLException {
       List<Category> categories = new ArrayList<>();
       String sql = "SELECT id, name FROM Product_category ORDER BY id";

       try (Connection conn = DBConnection.getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {

           while (rs.next()) {
               categories.add(new Category(rs.getInt("id"), rs.getString("name")));
           }
       }
       return categories;
   }

   public List<Product> getProductList(int page, int size) throws SQLException {
       return getProductsByCriteria(null, null, null, null, page, size);
   }

   public List<Product> getProductsByCriteria(String productName, String categoryName,
                                              Instant creationMin, Instant creationMax) throws SQLException {
       return getProductsByCriteria(productName, categoryName, creationMin, creationMax, 1, Integer.MAX_VALUE);
   }

   public List<Product> getProductsByCriteria(String productName, String categoryName,
                                              Instant creationMin, Instant creationMax,
                                              int page, int size) throws SQLException {
       List<Product> products = new ArrayList<>();

       StringBuilder sql = new StringBuilder();
       sql.append("SELECT p.id, p.name, p.price, p.creation_datetime, pc.name as category_name ");
       sql.append("FROM Product p ");
       sql.append("LEFT JOIN Product_category pc ON p.id = pc.product_id ");
       sql.append("WHERE 1=1 ");

       List<Object> params = new ArrayList<>();

       if (productName != null && !productName.isEmpty()) {
           sql.append("AND p.name ILIKE ? ");
           params.add("%" + productName + "%");
       }
       if (categoryName != null && !categoryName.isEmpty()) {
           sql.append("AND pc.name ILIKE ? ");
           params.add("%" + categoryName + "%");
       }
       if (creationMin != null) {
           sql.append("AND p.creation_datetime >= ? ");
           params.add(Timestamp.from(creationMin));
       }
       if (creationMax != null) {
           sql.append("AND p.creation_datetime <= ? ");
           params.add(Timestamp.from(creationMax));
       }

       sql.append("ORDER BY p.id ");
       sql.append("LIMIT ? OFFSET ?");
       params.add(size);
       params.add((page - 1) * size);

       try (Connection conn = DBConnection.getDBConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

           for (int i = 0; i < params.size(); i++) {
               pstmt.setObject(i + 1, params.get(i));
           }

           try (ResultSet rs = pstmt.executeQuery()) {
               while (rs.next()) {
                   int id = rs.getInt("id");
                   Product existing = products.stream()
                           .filter(p -> p.getId() == id)
                           .findFirst()
                           .orElse(null);

                   List<String> categories = new ArrayList<>();
                   if (existing != null) {
                       categories = new ArrayList<>(existing.getCategories());
                       products.remove(existing);
                   }

                   String catName = rs.getString("category_name");
                   if (catName != null) {
                       categories.add(catName);
                   }

                   Product product = new Product(
                           id,
                           rs.getString("name"),
                           rs.getDouble("price"),
                           rs.getTimestamp("creation_datetime").toInstant(),
                           categories
                   );
                   products.add(product);
               }
           }
       }
       return products;
   }
}
