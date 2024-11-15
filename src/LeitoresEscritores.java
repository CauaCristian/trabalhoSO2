import java.util.concurrent.*;

public class LeitoresEscritores {

    // ----------- 1. Semáforos -----------
    static class LeitoresEscritoresSemaforos {
        static final Semaphore mutex = new Semaphore(1);
        static final Semaphore escritor = new Semaphore(1);
        static final Semaphore leitor = new Semaphore(1);

        static int leitores = 0;

        static void leitor() throws InterruptedException {
            leitor.acquire();
            mutex.acquire();
            leitores++;
            if (leitores == 1) escritor.acquire();
            mutex.release();
            leitor.release();

            // Leitura do recurso compartilhado
            System.out.println("Leitor lendo");

            mutex.acquire();
            leitores--;
            if (leitores == 0) escritor.release();
            mutex.release();
        }

        static void escritor() throws InterruptedException {
            escritor.acquire();
            // Escrita no recurso compartilhado
            System.out.println("Escritor escrevendo");
            escritor.release();
        }
    }

    // ----------- 2. Mutex -----------
    static class LeitoresEscritoresMutex {
        static final Object mutex = new Object();
        static final Object escritor = new Object();
        static int leitores = 0;

        static void leitor() {
            synchronized (mutex) {
                leitores++;
                if (leitores == 1) synchronized (escritor) {}
            }

            // Leitura do recurso compartilhado
            System.out.println("Leitor lendo");

            synchronized (mutex) {
                leitores--;
                if (leitores == 0) synchronized (escritor) {}
            }
        }

        static void escritor() {
            synchronized (escritor) {
                // Escrita no recurso compartilhado
                System.out.println("Escritor escrevendo");
            }
        }
    }

    // ----------- 3. Monitores -----------
    static class LeitoresEscritoresMonitores {
        static final Object lock = new Object();
        static final Object escritor = new Object();
        static int leitores = 0;

        static void leitor() {
            synchronized (lock) {
                leitores++;
                if (leitores == 1) synchronized (escritor) {}
            }

            // Leitura do recurso compartilhado
            System.out.println("Leitor lendo");

            synchronized (lock) {
                leitores--;
                if (leitores == 0) synchronized (escritor) {}
            }
        }

        static void escritor() {
            synchronized (escritor) {
                // Escrita no recurso compartilhado
                System.out.println("Escritor escrevendo");
            }
        }
    }

    // ----------- 4. Troca de Mensagens -----------
    static class LeitoresEscritoresMensagens {
        static final BlockingQueue<String> leitores = new LinkedBlockingQueue<>();
        static final BlockingQueue<String> escritores = new LinkedBlockingQueue<>();

        static void leitor() {
            try {
                leitores.put("Leitor lendo");
                System.out.println("Leitor lendo");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        static void escritor() {
            try {
                escritores.put("Escritor escrevendo");
                System.out.println("Escritor escrevendo");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // ----------- 5. Barreiras -----------
    static class LeitoresEscritoresBarreiras {
        static final CyclicBarrier barrier = new CyclicBarrier(2);

        static void leitor() throws InterruptedException, BrokenBarrierException {
            barrier.await();  // Aguardando o escritor
            System.out.println("Leitor lendo");
            barrier.await();  // Esperando o escritor terminar
        }

        static void escritor() throws InterruptedException, BrokenBarrierException {
            barrier.await();  // Aguardando o leitor
            System.out.println("Escritor escrevendo");
            barrier.await();  // Aguardando o leitor terminar
        }
    }

    // Main para executar as simulações
    public static void main(String[] args) {
        System.out.println("Simulação dos Leitores e Escritores com diferentes abordagens:");

        // Criando threads para cada abordagem
        Thread thread1 = new Thread(() -> {
            try {
                System.out.println("\nIniciando Semáforos");
                LeitoresEscritoresSemaforos.leitor();
                LeitoresEscritoresSemaforos.escritor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread thread2 = new Thread(() -> {
            System.out.println("\nIniciando Mutex");
            LeitoresEscritoresMutex.leitor();
        });

        Thread thread3 = new Thread(() -> {
            System.out.println("\nIniciando Mutex (Escritor)");
            LeitoresEscritoresMutex.escritor();
        });

        Thread thread4 = new Thread(() -> {
            System.out.println("\nIniciando Monitores");
            LeitoresEscritoresMonitores.leitor();
        });

        Thread thread5 = new Thread(() -> {
            System.out.println("\nIniciando Monitores (Escritor)");
            LeitoresEscritoresMonitores.escritor();
        });

        Thread thread6 = new Thread(() -> {
            System.out.println("\nIniciando Troca de Mensagens");
            LeitoresEscritoresMensagens.leitor();
        });

        Thread thread7 = new Thread(() -> {
            System.out.println("\nIniciando Troca de Mensagens (Escritor)");
            LeitoresEscritoresMensagens.escritor();
        });

        Thread thread8 = new Thread(() -> {
            try {
                System.out.println("\nIniciando Barreiras");
                LeitoresEscritoresBarreiras.leitor();
                LeitoresEscritoresBarreiras.escritor();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        // Iniciando as threads
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread5.start();
        thread6.start();
        thread7.start();
        thread8.start();

        // Aguardando o término de todas as threads
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
            thread6.join();
            thread7.join();
            thread8.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
