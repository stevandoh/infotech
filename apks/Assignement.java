// given an array of integer write a function that returns the top 2 maximum numbers in the array

public static int[] twoLargest(int values[]){
    int largestA = Integer.MIN_VALUE, largestB = Integer.MIN_VALUE;

    for(int value : values) {
        if(value > largestA) {
            largestB = largestA;
            largestA = value;
        } else if (value > largestB) {
            largestB = value;
        }
    }
    return new int[] { largestA, largestB }; 
}

//write a function that accepts an array of integers with duplicate numbers.
// Your function should return an integer array without duplicates

public class RemoveDuplicateInArrayExample{  
public static int removeDuplicateElements(int arr[], int n){  
        if (n==0 || n==1){  
            return n;  
        }  
        int[] temp = new int[n];  
        int j = 0;  
        for (int i=0; i<n-1; i++){  
            if (arr[i] != arr[i+1]){  
                temp[j++] = arr[i];  
            }  
         }  
        temp[j++] = arr[n-1];     
        // Changing original array  
        for (int i=0; i<j; i++){  
            arr[i] = temp[i];  
        }  
        return j;  
    }  

// Write a fucntion that accepts 3 integers as date (day,month,year) . Your function should print the
next day date. 

public static String getNextDate(String day,String month, String year) {
        String nextDate = "";
        try {
            Calendar today = Calendar.getInstance();
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            String curDate = day +"/"+month +"/" + year;
            Date date = format.parse(curDate);
            today.setTime(date);
            today.add(Calendar.DAY_OF_YEAR, 1);
            nextDate = format.format(today.getTime());
        } catch (Exception e) {
            return nextDate;
        }
        return nextDate;
    }

// write a function that accepts an array og integers .Returns the number in the array that is closest to zero

public static int getSumOfTwoClosestToZeroElements(int[] a) {
            /*
             Please implement this method to
             return the sum of the two elements closest to zero.
             If there are two elements equally close to zero like -2 and 2,
             consider the positive element to be "closer" to zero than the negative one.
            */
            int[] b = a;
            Arrays.sort(b);
            int negativeValuePlacement = 0;
            int positiveValuePlacement = 0;
            int closestNumberToZero;

            for( int i = 0; i < b.length; i++) {
                if (b[i] < 0) {negativeValuePlacement++;
                    positiveValuePlacement++;
                }
                if (b[i] == 0) positiveValuePlacement++;
            }
            --negativeValuePlacement;

            if (b[0] >= 0) closestNumberToZero = b[positiveValuePlacement];
            else {
                if ((b[negativeValuePlacement]*-1) < b[positiveValuePlacement]) {
                closestNumberToZero = (b[negativeValuePlacement]*-1);
            }   else    closestNumberToZero = b[positiveValuePlacement]; }
        return closestNumberToZero;
        }