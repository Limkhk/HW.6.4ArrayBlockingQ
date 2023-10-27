package org.example;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Main {
    static BlockingQueue<String> aQueue = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> bQueue = new ArrayBlockingQueue<>(100);
    static BlockingQueue<String> cQueue = new ArrayBlockingQueue<>(100);
    public static void main(String[] args) {
        new Thread(() -> {
            for(int i = 0; i < 10_000; i++){
                String str = generateText("abc", 100_000);
                try {
                    aQueue.put(str);
                    bQueue.put(str);
                    cQueue.put(str);
                } catch (InterruptedException e){
                    return;
                }
            }
        }).start();

        findMostString(aQueue, 'a');
        findMostString(bQueue, 'b');
        findMostString(cQueue, 'c');
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
    public static void findMostString(BlockingQueue<String> sourceQueue, char charToCount){
        new Thread(() -> {
            String most = "";
            int count = 0;
            for(int i = 0; i < 10_000; i++){
                try {
                    String str = sourceQueue.take();
                    int c = 0;
                    for(int j = 0; j < str.length(); j++){
                        if(str.charAt(j) == charToCount){
                            c++;
                        }
                    }

                    if(count < c){
                        count = c;
                        most = str;
                    }
                } catch (InterruptedException e) {
                    return;
                }
            }

            System.out.println(count + " " + charToCount + ": " + most.substring(0, 30));
        }).start();
    }
}