package com.dharshinimart.config;

import com.dharshinimart.model.Category;
import com.dharshinimart.model.Product;
import com.dharshinimart.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProductSeeder {

    @Bean
    CommandLineRunner seedProducts(ProductRepository productRepository) {
        return args -> {
            if (productRepository.count() > 0) {
                System.out.println("[DharshiniMart] Products already seeded: " + productRepository.count());
                return;
            }
            List<Product> products = List.of(
                // Fruits 10
                new Product("Apple", Category.FRUITS, 180, "kg", 45, 10, "🍎", "Fresh juicy apples rich in fiber and vitamins.", 10),
                new Product("Banana", Category.FRUITS, 60, "dozen", 60, 15, "🍌", "Sweet ripe bananas, perfect for daily nutrition.", 0),
                new Product("Orange", Category.FRUITS, 90, "kg", 35, 8, "🍊", "Juicy oranges loaded with Vitamin C.", 5),
                new Product("Mango", Category.FRUITS, 150, "kg", 30, 8, "🥭", "King of fruits - sweet Alphonso mangoes.", 12),
                new Product("Grapes", Category.FRUITS, 120, "kg", 25, 5, "🍇", "Seedless fresh grapes, sweet and crunchy.", 0),
                new Product("Watermelon", Category.FRUITS, 40, "kg", 50, 10, "🍉", "Refreshing watermelon, perfect for summer.", 0),
                new Product("Pomegranate", Category.FRUITS, 200, "kg", 20, 5, "🍎", "Fresh pomegranates, antioxidant rich.", 8),
                new Product("Papaya", Category.FRUITS, 55, "kg", 22, 5, "🍈", "Ripe papaya good for digestion.", 0),
                new Product("Guava", Category.FRUITS, 70, "kg", 18, 5, "🍐", "Fresh guavas, vitamin rich.", 0),
                new Product("Pineapple", Category.FRUITS, 65, "piece", 15, 5, "🍍", "Sweet and tangy pineapples.", 0),

                // Vegetables 12
                new Product("Tomato", Category.VEGETABLES, 40, "kg", 25, 8, "🍅", "Fresh red tomatoes for cooking.", 0),
                new Product("Potato", Category.VEGETABLES, 35, "kg", 100, 20, "🥔", "Farm fresh potatoes, versatile vegetable.", 10),
                new Product("Onion", Category.VEGETABLES, 38, "kg", 80, 15, "🧅", "Fresh onions, essential for every kitchen.", 0),
                new Product("Carrot", Category.VEGETABLES, 50, "kg", 30, 8, "🥕", "Orange crunchy carrots rich in Vitamin A.", 0),
                new Product("Beans", Category.VEGETABLES, 60, "kg", 20, 5, "🫘", "Fresh green beans, high in fiber.", 0),
                new Product("Cabbage", Category.VEGETABLES, 35, "kg", 18, 5, "🥬", "Fresh green cabbage.", 0),
                new Product("Cauliflower", Category.VEGETABLES, 45, "kg", 12, 5, "🥦", "White fresh cauliflower florets.", 0),
                new Product("Brinjal", Category.VEGETABLES, 45, "kg", 0, 5, "🍆", "Fresh purple brinjals.", 0),
                new Product("Beetroot", Category.VEGETABLES, 40, "kg", 16, 5, "🥬", "Fresh beetroot for salads.", 0),
                new Product("Spinach", Category.VEGETABLES, 30, "bundle", 14, 5, "🥬", "Green leafy spinach iron rich.", 0),
                new Product("Green Peas", Category.VEGETABLES, 90, "kg", 19, 5, "🟢", "Fresh green peas.", 15),
                new Product("Ladys Finger", Category.VEGETABLES, 50, "kg", 16, 5, "🥒", "Tender ladys finger (Okra).", 0),

                // Rice & Grains 9
                new Product("Basmati Rice", Category.RICE_GRAINS, 120, "kg", 60, 15, "🍚", "Premium long-grain Basmati rice.", 10),
                new Product("Ponni Rice", Category.RICE_GRAINS, 65, "kg", 70, 15, "🍚", "Tamil Nadu special Ponni boiled rice.", 0),
                new Product("Sona Masuri Rice", Category.RICE_GRAINS, 68, "kg", 55, 10, "🍚", "Lightweight aromatic Sona Masuri rice.", 5),
                new Product("Idli Rice", Category.RICE_GRAINS, 60, "kg", 45, 10, "🍚", "Special rice for soft idlis and dosas.", 0),
                new Product("Brown Rice", Category.RICE_GRAINS, 95, "kg", 25, 5, "🍚", "Healthy fiber-rich brown rice.", 0),
                new Product("Wheat", Category.RICE_GRAINS, 48, "kg", 50, 10, "🌾", "Whole wheat grains.", 0),
                new Product("Rava", Category.RICE_GRAINS, 55, "kg", 30, 8, "🌾", "Semolina for upma and kesari.", 0),
                new Product("Maida", Category.RICE_GRAINS, 50, "kg", 28, 8, "🌾", "Refined wheat flour.", 0),
                new Product("Atta", Category.RICE_GRAINS, 58, "kg", 8, 10, "🌾", "Whole wheat atta for rotis. Low stock example.", 10),

                // Dals & Pulses 8
                new Product("Toor Dal", Category.DALS_PULSES, 145, "kg", 35, 8, "🫘", "Premium toor dal for sambar.", 10),
                new Product("Moong Dal", Category.DALS_PULSES, 130, "kg", 32, 8, "🟡", "Split yellow moong dal, easy to digest.", 0),
                new Product("Urad Dal", Category.DALS_PULSES, 135, "kg", 28, 8, "⚪", "White urad dal for idli batter.", 0),
                new Product("Chana Dal", Category.DALS_PULSES, 110, "kg", 30, 8, "🟤", "Bengal gram dal.", 5),
                new Product("Green Gram", Category.DALS_PULSES, 125, "kg", 22, 5, "🟢", "Whole green gram sprouts.", 0),
                new Product("Black Gram", Category.DALS_PULSES, 128, "kg", 18, 5, "⚫", "Black gram for healthy protein.", 0),
                new Product("Chickpeas", Category.DALS_PULSES, 115, "kg", 26, 5, "🟠", "Kabuli chana for curries.", 0),
                new Product("Rajma", Category.DALS_PULSES, 140, "kg", 20, 5, "🔴", "Red kidney beans.", 0),

                // Cooking Essentials 12
                new Product("Sunflower Oil", Category.COOKING_ESSENTIALS, 165, "litre", 40, 10, "🫒", "Pure sunflower cooking oil.", 15),
                new Product("Groundnut Oil", Category.COOKING_ESSENTIALS, 190, "litre", 25, 8, "🥜", "Cold pressed groundnut oil.", 0),
                new Product("Coconut Oil", Category.COOKING_ESSENTIALS, 220, "litre", 20, 5, "🥥", "Pure coconut oil.", 0),
                new Product("Mustard Oil", Category.COOKING_ESSENTIALS, 175, "litre", 18, 5, "🟡", "Mustard oil for authentic taste.", 0),
                new Product("Salt", Category.COOKING_ESSENTIALS, 22, "kg", 100, 20, "🧂", "Iodized crystal salt.", 0),
                new Product("Sugar", Category.COOKING_ESSENTIALS, 50, "kg", 0, 10, "🍬", "Refined white sugar. Out of stock demo.", 0),
                new Product("Jaggery", Category.COOKING_ESSENTIALS, 70, "kg", 15, 5, "🟤", "Traditional jaggery cubes.", 0),
                new Product("Turmeric Powder", Category.COOKING_ESSENTIALS, 300, "kg", 12, 5, "🟨", "Pure turmeric powder.", 5),
                new Product("Chilli Powder", Category.COOKING_ESSENTIALS, 320, "kg", 14, 5, "🌶️", "Spicy red chilli powder.", 0),
                new Product("Coriander Powder", Category.COOKING_ESSENTIALS, 280, "kg", 10, 5, "🌿", "Aromatic coriander powder.", 0),
                new Product("Pepper", Category.COOKING_ESSENTIALS, 850, "kg", 8, 5, "⚫", "Black pepper whole.", 0),
                new Product("Cumin", Category.COOKING_ESSENTIALS, 650, "kg", 9, 5, "🟤", "Cumin seeds.", 0),

                // Spices & Masala 8
                new Product("Garam Masala", Category.SPICES_MASALA, 480, "kg", 10, 5, "🧂", "Aromatic garam masala blend.", 10),
                new Product("Sambar Powder", Category.SPICES_MASALA, 350, "kg", 12, 5, "🍛", "Authentic sambar powder.", 0),
                new Product("Rasam Powder", Category.SPICES_MASALA, 340, "kg", 11, 5, "🍲", "Tangy rasam powder.", 0),
                new Product("Chicken Masala", Category.SPICES_MASALA, 420, "kg", 9, 5, "🍗", "Spicy chicken masala.", 12),
                new Product("Curry Powder", Category.SPICES_MASALA, 300, "kg", 13, 5, "🍛", "All-purpose curry powder.", 0),
                new Product("Biriyani Masala", Category.SPICES_MASALA, 520, "kg", 7, 5, "🍚", "Hyderabadi biriyani masala.", 15),
                new Product("Pepper Powder", Category.SPICES_MASALA, 800, "kg", 6, 5, "⚫", "Freshly ground pepper powder.", 0),
                new Product("Ginger Garlic Paste", Category.SPICES_MASALA, 90, "kg", 16, 5, "🧄", "Fresh ginger garlic paste.", 0),

                // Dairy & Eggs 6
                new Product("Milk", Category.DAIRY_EGGS, 32, "litre", 50, 10, "🥛", "Fresh pasteurized milk.", 0),
                new Product("Curd", Category.DAIRY_EGGS, 45, "kg", 20, 5, "🥣", "Thick fresh curd.", 0),
                new Product("Butter", Category.DAIRY_EGGS, 55, "100g", 25, 8, "🧈", "Creamy salted butter.", 5),
                new Product("Cheese", Category.DAIRY_EGGS, 95, "200g", 18, 5, "🧀", "Processed cheese cubes.", 0),
                new Product("Paneer", Category.DAIRY_EGGS, 85, "200g", 15, 5, "🧀", "Soft fresh paneer.", 10),
                new Product("Eggs", Category.DAIRY_EGGS, 75, "dozen", 40, 10, "🥚", "Farm fresh eggs (12 pcs).", 0),

                // Beverages 6
                new Product("Tea", Category.BEVERAGES, 180, "250g", 30, 8, "🍵", "Premium Assam tea powder.", 0),
                new Product("Coffee", Category.BEVERAGES, 220, "200g", 22, 5, "☕", "Instant filter coffee.", 0),
                new Product("Health Drink", Category.BEVERAGES, 320, "500g", 12, 5, "🥤", "Nutrition health drink for kids.", 15),
                new Product("Fruit Juice", Category.BEVERAGES, 85, "litre", 18, 5, "🧃", "Mixed fruit juice tetra pack.", 0),
                new Product("Soft Drinks", Category.BEVERAGES, 40, "750ml", 35, 10, "🥤", "Assorted soft drinks.", 0),
                new Product("Mineral Water", Category.BEVERAGES, 20, "litre", 60, 15, "💧", "Packaged mineral water.", 0),

                // Snacks 7
                new Product("Biscuits", Category.SNACKS, 35, "packet", 45, 15, "🍪", "Crispy glucose biscuits. Buy 2 Get 1 Offer!", 20),
                new Product("Chips", Category.SNACKS, 20, "packet", 50, 15, "🥔", "Potato chips assorted flavors.", 0),
                new Product("Cookies", Category.SNACKS, 60, "packet", 20, 5, "🍪", "Butter cookies delicious.", 10),
                new Product("Mixture", Category.SNACKS, 75, "packet", 18, 5, "🥜", "Spicy south indian mixture.", 0),
                new Product("Namkeen", Category.SNACKS, 65, "packet", 22, 5, "🥨", "Crunchy namkeen.", 15),
                new Product("Chocolates", Category.SNACKS, 50, "packet", 30, 10, "🍫", "Assorted milk chocolates.", 20),
                new Product("Cakes", Category.SNACKS, 350, "kg", 8, 5, "🎂", "Fresh cream cakes.", 0),

                // Personal Care 6
                new Product("Soap", Category.PERSONAL_CARE, 35, "piece", 40, 10, "🧼", "Herbal bathing soap.", 0),
                new Product("Shampoo", Category.PERSONAL_CARE, 120, "200ml", 22, 5, "🧴", "Nourishing shampoo.", 10),
                new Product("Toothpaste", Category.PERSONAL_CARE, 55, "100g", 28, 8, "🦷", "Fluoride toothpaste.", 0),
                new Product("Toothbrush", Category.PERSONAL_CARE, 30, "piece", 35, 10, "🪥", "Soft bristle toothbrush.", 0),
                new Product("Face Wash", Category.PERSONAL_CARE, 95, "100ml", 16, 5, "🧴", "Gentle face wash.", 5),
                new Product("Hand Wash", Category.PERSONAL_CARE, 85, "200ml", 14, 5, "🧴", "Liquid hand wash.", 0),

                // Household 7
                new Product("Detergent", Category.HOUSEHOLD, 110, "kg", 20, 5, "🧹", "Washing detergent powder.", 10),
                new Product("Dishwash", Category.HOUSEHOLD, 75, "500ml", 18, 5, "🍽️", "Lemon dishwash gel.", 0),
                new Product("Floor Cleaner", Category.HOUSEHOLD, 95, "500ml", 12, 5, "🧹", "Disinfectant floor cleaner.", 0),
                new Product("Toilet Cleaner", Category.HOUSEHOLD, 85, "500ml", 10, 5, "🚽", "Powerful toilet cleaner.", 0),
                new Product("Washing Powder", Category.HOUSEHOLD, 120, "kg", 15, 5, "👕", "Premium washing powder.", 12),
                new Product("Garbage Bags", Category.HOUSEHOLD, 45, "packet", 25, 8, "🗑️", "Disposable garbage bags.", 0),
                new Product("Tissue Paper", Category.HOUSEHOLD, 40, "packet", 30, 10, "🧻", "Soft tissue paper box.", 0)
            );
            productRepository.saveAll(products);
            System.out.println("[DharshiniMart] Seeded " + products.size() + " products");
        };
    }
}
