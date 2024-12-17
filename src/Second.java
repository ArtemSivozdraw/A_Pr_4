import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicReference;

public class Second {
    public static void main(String[] args) {
        long global_start = System.nanoTime();                                                                          // Глобальний лічильник
        AtomicReference<AtomicIntegerArray> arr_store = new AtomicReference<>(new AtomicIntegerArray(new int[20]));     // змінна для зберігання, створеного в потоці, масиву.

        // Створення випадкового масиву довжиною 20
        CompletableFuture<int[]> array = CompletableFuture.supplyAsync(() -> {
            long start = System.nanoTime();
            int[] arr = new int[20];
            for(int i=0;i<arr.length;i++){
                arr[i] = (int) (Math.random()*100);
            }
            arr_store.set(new AtomicIntegerArray(arr));
            long end = System.nanoTime();

            System.out.println("Array generated, "  + calculate_time(start,end));
            return arr;
        });
        // Функція для обрахунку добутку різниці сусідніх елементів масива
        CompletableFuture<Integer> function = array.thenApplyAsync((arr) -> {
            long start = System.nanoTime();
            int result = 1;
            for(int i=1;i<arr.length; i++){
                result*=arr[i]-arr[i-1];
            }
            long end = System.nanoTime();

            System.out.println("Result calculated, " + calculate_time(start,end));
            return result;
        });
        // Вивід результатів в термінал
        function.thenAcceptAsync(result -> {
            long start = System.nanoTime();
            System.out.printf("Result : "+result);
            System.out.printf(" | Array : " + arr_store);
            long end = System.nanoTime();
            System.out.println("\nOutput done, "+calculate_time(start,end));
        }).join();


        try {
            function.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        long global_end = System.nanoTime();
        System.out.println("Total "+calculate_time(global_start,global_end));
    }
    // Функція підрахунку часу
    public static String calculate_time(long start, long end){
        return ("time taken: " + (double)(end - start)/1000000 + " mls");
    }
}
