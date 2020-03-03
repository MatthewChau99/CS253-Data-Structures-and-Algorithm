package hw3;

import java.util.*;


public class PivotRule {
    public Integer getPivot(ArrayList<Integer> array) {
        return array.get(0);
    }
}

class RandomizedRule extends PivotRule {
    @Override
    public Integer getPivot(ArrayList<Integer> array) {
        Random r = new Random();
        return r.nextInt(array.size());
    }
}

class MedianOfMediansRule extends PivotRule {
    @Override
    public Integer getPivot(ArrayList<Integer> array) {
        /* Base case: when array has size <= 5 */
        if (array.size() <= 5) return array.indexOf(median5(array));
        else {
            ArrayList<Integer> medians = new ArrayList<>();
            for (int i = 0; i < array.size() - 1; i += 5) {
                if (i + 4 > array.size() - 1) {
                    medians.add(median5(new ArrayList<>(array.subList(i, array.size() - 1))));
                } else {
                    medians.add(median5(new ArrayList<>(array.subList(i, i + 5))));
                }
            }
            return array.indexOf(QuickSelector.quickSelect(medians, (medians.size() - 1) / 2, new MedianOfMediansRule()));
        }
    }

    /* returns the median of ArrayList when it has size <= 5 */
    private int median5(ArrayList<Integer> array) {
        ArrayList<Integer>  copyArray = (ArrayList<Integer>) array.clone();
        Collections.sort(copyArray);
        return copyArray.get((array.size() - 1) / 2);
    }
}

