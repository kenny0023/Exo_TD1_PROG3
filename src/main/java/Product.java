import java.time.Instant;
import java.util.List;

public class Product {
   private int id;
   private String name;
   private double price;
   private Instant creationDatetime;
   private List<String> categories;

   public Product(int id, String name, double price, Instant creationDatetime, List<String> categories) {
       this.id = id;
       this.name = name;
       this.price = price;
       this.creationDatetime = creationDatetime;
       this.categories = categories;
   }

   public String getCategoryName() {
       return categories.isEmpty() ? "Aucune" : String.join(", ", categories);
   }

   public int getId() { return id; }
   public String getName() { return name; }
   public double getPrice() { return price; }
   public Instant getCreationDatetime() { return creationDatetime; }
   public List<String> getCategories() { return categories; }

   @Override
   public String toString() {
       return "Product{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", price=" + price +
               ", creationDatetime=" + creationDatetime +
               ", categories=" + categories +
               '}';
   }
}
