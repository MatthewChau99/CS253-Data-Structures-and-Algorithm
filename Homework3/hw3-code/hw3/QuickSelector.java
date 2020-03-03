package hw3;

import java.util.*;

public class QuickSelector {
    /* returns the element at the "index" index of array */
    public static Integer quickSelect(ArrayList<Integer> array, int index, PivotRule rule){
        if (array.size() == 0) {
            System.out.println("array cannot be empty");
            return 0;
        }
        int pivot = partition(array, rule);
        if (pivot == index) return array.get(pivot);
        if (pivot < index) return quickSelect(new ArrayList<>(array.subList(pivot + 1, array.size())), index - array.subList(0, pivot + 1).size(), rule);
        return quickSelect(new ArrayList<>(array.subList(0, pivot)), index, rule);
    }

    /*
     *   returns the pivot index
     */
    private static int partition(ArrayList<Integer> array, PivotRule rule) {
        int pivot = rule.getPivot(array);
        swap(array, pivot, array.size() - 1);
        int i = 0, j = 0;
        while (j < array.size()) {
            if (array.get(j) <= array.get(array.size() - 1)) {
                swap(array, i, j);
                i++;
            }
            j++;
        }
        return i - 1;
    }

    private static void swap(ArrayList<Integer> arr, int index1, int index2) {
        int temp = arr.get(index2);
        arr.set(index2, arr.get(index1));
        arr.set(index1, temp);
    }

    public static void main(String[] args) {
        ArrayList<Integer> array1 = generateArrayList(900, 600);
        System.out.println(testAccuracy(array1));
        System.out.println(testSpeed());
    }

    private static ArrayList<Integer> generateArrayList(int size, int range) {
        ArrayList<Integer> result = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            result.add(r.nextInt(range));
        }
        return result;
    }

    private static boolean testAccuracy(ArrayList<Integer> array) {
        ArrayList<Integer> random = new ArrayList<>();
        ArrayList<Integer> medians = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {
            random.add(quickSelect(array, i, new RandomizedRule()));
            medians.add(quickSelect(array, i, new MedianOfMediansRule()));
        }

        Collections.sort(array);
        for (int i = 0; i < array.size(); i++) {
            if (!random.get(i).equals(array.get(i)) || !medians.get(i).equals(array.get(i))) {
                System.out.println("Random: " + random.get(i));
                System.out.println("Medians: " + medians.get(i));
                System.out.println("Array: " + array.get(i));
                return false;
            }
        }
        return true;
    }

    private static String testSpeed() {
        StringBuilder s = new StringBuilder();
        s.append("Size \t\t").append("Random \t\t").append("Medians \n");

        for (int i = 10000; i <= 10000000; i *= 10) {
            ArrayList<Integer> testArray = generateArrayList(i, i * 2);
            s.append(i);
            s.append("\t\t");

            /* Test Random Rule */
            long sRandom = System.currentTimeMillis();
            quickSelect(testArray, i / 2, new RandomizedRule());
            long tRandom = System.currentTimeMillis() - sRandom;
            s.append((double) tRandom / 1000);
            s.append("\t\t");

            /* Test Medians of Medians rule */
            long sMedian = System.currentTimeMillis();
            quickSelect(testArray, i / 2, new MedianOfMediansRule());
            long tMedian = System.currentTimeMillis() - sMedian;
            s.append((double) tMedian / 1000);
            s.append('\n');
        }
         return s.toString();
    }
}


