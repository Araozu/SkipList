/**
 * Comprehensive unit tests for SkipList implementation
 * Tests basic functionality, edge cases, and performance improvements
 */
public class SkipListTest {
    
    public static void main(String[] args) {
        System.out.println("Running SkipList comprehensive tests...\n");
        
        boolean allPassed = true;
        allPassed &= testBasicOperations();
        allPassed &= testEdgeCases();
        allPassed &= testPerformance();
        allPassed &= testSizeTracking();
        allPassed &= testDuplicateHandling();
        
        System.out.println("\n" + (allPassed ? "✅ ALL TESTS PASSED!" : "❌ SOME TESTS FAILED!"));
    }
    
    private static boolean testBasicOperations() {
        System.out.println("Testing basic operations...");
        SkipList<Integer> sl = new SkipList<>(5);
        
        // Test empty list
        if (!sl.estaVacia()) {
            System.out.println("❌ Empty list check failed");
            return false;
        }
        
        // Test insertion
        sl.insertar(10);
        sl.insertar(5);
        sl.insertar(15);
        
        // Test contains
        if (!sl.contiene(10) || !sl.contiene(5) || !sl.contiene(15)) {
            System.out.println("❌ Contains check failed");
            return false;
        }
        
        if (sl.contiene(99)) {
            System.out.println("❌ Contains false positive");
            return false;
        }
        
        // Test deletion
        sl.eliminar(10);
        if (sl.contiene(10)) {
            System.out.println("❌ Deletion failed");
            return false;
        }
        
        System.out.println("✅ Basic operations passed");
        return true;
    }
    
    private static boolean testEdgeCases() {
        System.out.println("Testing edge cases...");
        
        // Test invalid constructor
        try {
            SkipList<Integer> sl = new SkipList<>(1);
            System.out.println("❌ Constructor validation failed");
            return false;
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        SkipList<Integer> sl = new SkipList<>(3);
        
        // Test null operations
        try {
            sl.insertar(null);
            System.out.println("❌ Null insertion validation failed");
            return false;
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            sl.contiene(null);
            System.out.println("❌ Null contains validation failed");
            return false;
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        try {
            sl.eliminar(null);
            System.out.println("❌ Null deletion validation failed");
            return false;
        } catch (IllegalArgumentException e) {
            // Expected
        }
        
        // Test deletion from empty list
        sl.eliminar(999); // Should not crash
        
        // Test single element operations
        sl.insertar(42);
        if (!sl.contiene(42)) {
            System.out.println("❌ Single element insertion failed");
            return false;
        }
        
        sl.eliminar(42);
        if (sl.contiene(42) || !sl.estaVacia()) {
            System.out.println("❌ Single element deletion failed");
            return false;
        }
        
        System.out.println("✅ Edge cases passed");
        return true;
    }
    
    private static boolean testPerformance() {
        System.out.println("Testing performance...");
        SkipList<Integer> sl = new SkipList<>(8);
        
        // Insert many elements and time it
        long startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            sl.insertar(i);
        }
        long insertTime = System.nanoTime() - startTime;
        
        // Search for all elements
        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            if (!sl.contiene(i)) {
                System.out.println("❌ Performance test: element " + i + " not found");
                return false;
            }
        }
        long searchTime = System.nanoTime() - startTime;
        
        // Delete all elements
        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            sl.eliminar(i);
        }
        long deleteTime = System.nanoTime() - startTime;
        
        if (!sl.estaVacia()) {
            System.out.println("❌ Performance test: list not empty after deletions");
            return false;
        }
        
        System.out.printf("✅ Performance test passed - Insert: %.2fms, Search: %.2fms, Delete: %.2fms\n",
                         insertTime/1_000_000.0, searchTime/1_000_000.0, deleteTime/1_000_000.0);
        return true;
    }
    
    private static boolean testSizeTracking() {
        System.out.println("Testing size tracking...");
        SkipList<String> sl = new SkipList<>(4);
        
        if (sl.size() != 0) {
            System.out.println("❌ Initial size should be 0");
            return false;
        }
        
        sl.insertar("apple");
        sl.insertar("banana");
        sl.insertar("cherry");
        
        if (sl.size() != 3) {
            System.out.println("❌ Size after insertions should be 3, got: " + sl.size());
            return false;
        }
        
        sl.eliminar("banana");
        
        if (sl.size() != 2) {
            System.out.println("❌ Size after deletion should be 2, got: " + sl.size());
            return false;
        }
        
        sl.eliminar("nonexistent");
        
        if (sl.size() != 2) {
            System.out.println("❌ Size should remain 2 after deleting nonexistent element");
            return false;
        }
        
        System.out.println("✅ Size tracking passed");
        return true;
    }
    
    private static boolean testDuplicateHandling() {
        System.out.println("Testing duplicate handling...");
        SkipList<Integer> sl = new SkipList<>(4);
        
        sl.insertar(100);
        int initialSize = sl.size();
        
        sl.insertar(100); // Try to insert duplicate
        
        // Note: Current implementation allows duplicates
        // This test just verifies the behavior is consistent
        if (sl.size() <= initialSize) {
            System.out.println("❌ Size should increase with duplicate insertion");
            return false;
        }
        
        System.out.println("✅ Duplicate handling passed (duplicates allowed)");
        return true;
    }
}