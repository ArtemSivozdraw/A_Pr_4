import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class First {
    public static void main(String[] args) {

        //Цей рядок викликає асинхроний метод supplyAsync, який є початком обробки масиву. Метод повертає випадково згенерований масив чисел
        CompletableFuture<int[]> array = CompletableFuture.supplyAsync(() -> {
            long start = System.nanoTime();
            int[] arr = new int[10];
            for (int i = 0; i < arr.length; i++) {
                arr[i] = (int) (Math.random() * 100);
            }
            long end = System.nanoTime();
            System.out.println("Array generated: " + Arrays.toString(arr) + ", Time taken: " + (double)(end - start)/1000000 + " ms");
            return arr;
        });

        // Цей рядок використовує метод thenApplyAsync і передається йому в якоскі параметра, результат виконання метода array.
        // Метод додає до кожного елемента 10
        CompletableFuture<int[]> modifiedArray = array.thenApplyAsync(arr -> {
            long start = System.nanoTime();
            for (int i = 0; i < arr.length; i++) {
                arr[i] += 10;
            }
            long end = System.nanoTime();
            System.out.println("Array after adding 10: " + Arrays.toString(arr) + ", Time taken: " + (double)(end - start)/1000000 + " ms");
            return arr;
        });

        // Accept відрізняється від Apply тим, що він нічого не повертає. Тобто він потрібен тільки для того щоб виконати певні операції, на основі даних, що він отримав.
        // Метод ділить кожен елемент масива на 2
        CompletableFuture<Void> dividedArray = modifiedArray.thenAcceptAsync(arr -> {
            long start = System.nanoTime();
            double[] divArray = new double[arr.length];
            for (int i = 0; i < arr.length; i++) {
                divArray[i] = arr[i] / 2.0;
            }
            long end = System.nanoTime();
            System.out.println("Array after division by 2: " + Arrays.toString(divArray) + ", Time taken: " + (double)(end - start)/1000000 + " ms");
        });


        // Цей метод дуже спірний, тому що практично марний. Він не приймає і не повертає ніяких значень і потрібен лише для того, щоб інформувати про закінчення операцій.
        dividedArray.thenRunAsync(() -> {
            System.out.println("All tasks completed!");
        });

        try {
            dividedArray.get();                                   // Рядок який вимагає отримання результату від dividedArray
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
