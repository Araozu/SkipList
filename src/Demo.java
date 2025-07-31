/**
 * Demonstration of the improved SkipList implementation
 * Shows the enhanced features and performance
 */
public class Demo {
    
    public static void main(String[] args) {
        System.out.println("=== SkipList Implementation Demo ===\n");
        
        // Create a SkipList with height 4
        SkipList<Integer> sl = new SkipList<>(4);
        
        System.out.println("1. Creating empty SkipList:");
        System.out.println("   Size: " + sl.size());
        System.out.println("   Empty: " + sl.estaVacia());
        System.out.println("   Simple view: " + sl.toSimpleString());
        
        System.out.println("\n2. Adding elements: 50, 25, 75, 10, 60, 80");
        sl.insertar(50);
        sl.insertar(25);
        sl.insertar(75);
        sl.insertar(10);
        sl.insertar(60);
        sl.insertar(80);
        
        System.out.println("   Size: " + sl.size());
        System.out.println("   Simple view: " + sl.toSimpleString());
        
        System.out.println("\n3. Testing search operations:");
        System.out.println("   Contains 25: " + sl.contiene(25));
        System.out.println("   Contains 60: " + sl.contiene(60));
        System.out.println("   Contains 99: " + sl.contiene(99));
        
        System.out.println("\n4. Deleting elements: 25, 80");
        sl.eliminar(25);
        sl.eliminar(80);
        
        System.out.println("   Size: " + sl.size());
        System.out.println("   Simple view: " + sl.toSimpleString());
        
        System.out.println("\n5. Performance test with 1000 elements:");
        SkipList<Integer> largeSL = new SkipList<>(10);
        
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            largeSL.insertar(i);
        }
        long insertTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            largeSL.contiene(i);
        }
        long searchTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        for (int i = 0; i < 500; i++) {
            largeSL.eliminar(i * 2); // Delete every other element
        }
        long deleteTime = System.nanoTime() - startTime;
        
        System.out.printf("   Insert 1000 elements: %.2f ms\n", insertTime / 1_000_000.0);
        System.out.printf("   Search 1000 elements: %.2f ms\n", searchTime / 1_000_000.0);
        System.out.printf("   Delete 500 elements: %.2f ms\n", deleteTime / 1_000_000.0);
        System.out.println("   Final size: " + largeSL.size());
        
        System.out.println("\n6. Error handling demo:");
        try {
            SkipList<String> invalidSL = new SkipList<>(1);
        } catch (IllegalArgumentException e) {
            System.out.println("   ✅ Caught invalid height: " + e.getMessage());
        }
        
        try {
            sl.insertar(null);
        } catch (IllegalArgumentException e) {
            System.out.println("   ✅ Caught null insertion: " + e.getMessage());
        }
        
        System.out.println("\n7. Detailed visualization of small list:");
        SkipList<String> visualSL = new SkipList<>(3);
        visualSL.insertar("Apple");
        visualSL.insertar("Banana");
        visualSL.insertar("Cherry");
        
        System.out.println("   Simple view: " + visualSL.toSimpleString());
        System.out.println("   Detailed view:");
        System.out.println(visualSL.toString());
        
        System.out.println("\n=== Demo Complete ===");
        System.out.println("Key improvements demonstrated:");
        System.out.println("• O(log n) deletion performance (no more timeouts!)");
        System.out.println("• Comprehensive error handling");
        System.out.println("• Size tracking");
        System.out.println("• Improved random level generation");
        System.out.println("• Better documentation and code quality");
    }
}