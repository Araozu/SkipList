/**
 * Performance test to validate deletion improvements
 * This test helps verify that deletion operates in O(log n) time
 */
public class PerformanceTest {
    
    public static void main(String[] args) {
        System.out.println("Testing SkipList performance...");
        
        // Test with different sizes to observe performance scaling
        int[] sizes = {100, 500, 1000, 5000};
        
        for (int size : sizes) {
            testDeletionPerformance(size);
        }
    }
    
    private static void testDeletionPerformance(int size) {
        SkipList<Integer> sl = new SkipList<>(8); // Height of 8 should be sufficient
        
        // Insert elements
        System.out.printf("Testing with %d elements:\n", size);
        
        long startTime = System.nanoTime();
        for (int i = 0; i < size; i++) {
            sl.insertar(i);
        }
        long insertTime = System.nanoTime() - startTime;
        
        // Test deletion of elements in the middle (worst case scenario)
        startTime = System.nanoTime();
        for (int i = size/4; i < 3*size/4; i++) {
            sl.eliminar(i);
        }
        long deleteTime = System.nanoTime() - startTime;
        
        System.out.printf("  Insert time: %.2f ms\n", insertTime / 1_000_000.0);
        System.out.printf("  Delete time: %.2f ms\n", deleteTime / 1_000_000.0);
        System.out.printf("  Delete time per element: %.4f ms\n", 
                         (deleteTime / 1_000_000.0) / (size/2));
        System.out.println();
    }
}