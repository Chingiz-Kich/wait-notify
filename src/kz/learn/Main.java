package kz.learn;

import java.util.Scanner;


public class Main {

    public static void main(String[] args) throws InterruptedException {

        WaitNotify waitNotify = new WaitNotify();

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    waitNotify.produce();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    waitNotify.consume();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * start() - creates new thread and the run() method is executed on the newly created thread
         */
        thread1.start();
        thread2.start();

        /**
         * join() - waiting in current thread until calling object (thread) ends his process
         */
        thread1.join();
        thread2.join();

    }
}

class WaitNotify {

    private Scanner scanner = new Scanner(System.in);
    private final Object lock = new Object();

    public void produce() throws InterruptedException {

        /**
         * Вызов wait() & notify() не привязаны к объектам на которых идет синхронизация
         * Вызов этих методов привязывается к тому контексту, где мы находимся
         * Пример: мы синхронизируемся на lock объекте, но пока мы явно не вызовем у lock объекта lock.wait()
         * синхронизация будет идти на this.wait()
         * Java выкинет тут ошибку IllegalMonitorStateException: current thread is not owner
         * Потому что надо синхронизироватьсяна том же объекте на котором вызывается метод wait() or notify()
         */
        synchronized (lock) {
            System.out.println("Program was started");

            /**
             * При вызове метода wait():
             * 1. Отдаем intrinsic lock (монитор)
             * 2. Ждем пока будет вызван notify()
             */
            this.wait();
            System.out.println("Program was ended");
        }
    }

    public void consume() throws InterruptedException {
        synchronized (this) {
            System.out.println("Waiting for press any key");
            scanner.nextLine();

            notify();

            /**
             * sleep() специально добавлен для того, чтобы показать, что методом notify() не освобождается монитор объекта
             * для того чтобы полностью вернуть монитор объекта:
             *  1. Должен быть вызван метод notify()
             *  2. Должен быть завершен synchronized блок
             */
            Thread.sleep(5000);
        }
        System.out.println("Block for notify ended");
    }
}
