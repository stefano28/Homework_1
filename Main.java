import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Map map = Input(args);
        map.print();
        System.out.println("Il numero di pezzi presenti sulla mappa per ciascuna tipologia: ");
        PiecesForType(map);
        System.out.print("La casella con il maggior valore di difesa di giorno: ");
        BestDefence(map, true);
        System.out.print("La casella con il maggior valore di difesa di notte: ");
        BestDefence(map, false);
        System.out.print("La casella con il maggior valore di attacco di giorno: ");
        BestAttack(map, true);
        System.out.print("La casella con il maggior valore di attacco di notte: ");
        BestAttack(map, false);
        System.out.print("La casella con il maggior numero di pezzi dello stesso tipo: ");
        BestEqualPieces(map);
    }

    /*
        Metodo statico che crea la mappa e inserisce i personaggi dai file di input.
        Richiede come parametro una Map e restituisce una Map.
        In caso di eventuali errori gestisce autonomamente le eccezioni InputErrorException, FileNotFoundException, IOException.
    */

    static Map Input(String args[]) {
        Map map = null;
        try {
            if(args.length < 2) {
                throw new InputErrorException();
            }
            // Lettura e inserimento dei dati contenuti nel primo file: dimensioni mappa e tipologia di celle
            FileReader reader = new FileReader(args[0]);
            BufferedReader buffer = new BufferedReader(reader);
            String line;
            String[] firstLine = buffer.readLine().split(" ", 2); // Lettura delle dimensioni della mappa
            map = new Map(Integer.parseInt(firstLine[0]), Integer.parseInt(firstLine[1]));
            buffer.readLine();
            while((line = buffer.readLine()) != null) { // Lettura tipologia delle caselle
                map.addCell(line);
            }
            // Lettura e inserimento dei dati contenuti nel primo secondo file: personaggi
            reader = new FileReader(args[1]);
            buffer = new BufferedReader(reader);

            while((line = buffer.readLine()) != null) {
                String[] lineSplit = line.split(" ", 3);
                int x = Integer.parseInt(lineSplit[0]);
                int y = Integer.parseInt(lineSplit[1]);
                
                switch(lineSplit[2]) {
                    case "elfo":
                        map.addElfo(x, y);
                        break;
                    case "nano":
                        map.addNano(x, y);
                        break;
                    case "orco":
                        map.addOrco(x, y);
                        break;
                    default:
                        throw new InputErrorException();
                }
            }
            buffer.close();
            reader.close();
        } catch(FileNotFoundException e) {
            System.out.println("Errore: file non trovato");
            System.exit(1);
        } catch(IOException e) {
            System.out.println("Errore: errore generico di input");
            System.exit(1);
        } catch(InputErrorException e) {
            System.out.println("Errore: formato del file non valido");
            System.exit(1);
        }
        return map;
    }
    
    /*
        Metodo statico che stampa il numero di pezzi sulla mappa per ciascuna tipologia.
        Richiede come parametro una Map.
    */

    static void PiecesForType(Map m) {
        int elfi = 0;
        int nani = 0;
        int orchi = 0;
        for(int i = 0; i < m.getHeight(); i++) {
            for(int j = 0; j < m.getWidth(); j++) {
                elfi += m.getElfi(j, i);
                nani += m.getNani(j, i);
                orchi += m.getOrchi(j, i);
            }
        }
        System.out.println("Elfi: " + elfi);
        System.out.println("Nani: " + nani);
        System.out.println("Orchi: " + orchi);
    }

    /*
        Metodo statico che stampa la casella con il maggior valore di difesa in caso sia giorno o notte.
        Richiede come parametri una Map e un boolean che indica lo scenario temporale per il quale lo si 
        vuole calcolare.
    */

    static void BestDefence(Map m, boolean isDay) {
        int xBest = -1;
        int yBest = -1;
        double best = -1;
        for(int i = 0; i < m.getHeight(); i++) {
            for(int j = 0; j < m.getWidth(); j++) {
                double tmp = m.getOverallDefence(j, i, isDay);
                if(tmp > best) {
                    best = tmp;
                    xBest = j;
                    yBest = i;
                }
            }
        }
        System.out.println("(" + xBest + ", " + yBest + ") con valore: " + best);
    }

    /*
        Metodo statico che stampa la casella con il maggior valore di attacco in caso sia giorno o notte.
        Richiede come parametri una Map e un boolean che indica lo scenario temporale per il quale lo si 
        vuole calcolare.
    */

    static void BestAttack(Map m, boolean isDay) {
        int xBest = -1;
        int yBest = -1;
        double best = -1;
        for(int i = 0; i < m.getHeight(); i++) {
            for(int j = 0; j < m.getWidth(); j++) {
                double tmp = m.getOverallAttack(j, i, isDay);
                if(tmp > best) {
                    best = tmp;
                    xBest = j;
                    yBest = i;
                }
            }
        }
        System.out.println("(" + xBest + ", " + yBest + ") con valore: " + best);
    }

    /*
        Metodo statico che stampa la casella con il maggior numero di pezzi dello stesso tipo.
        Richiede come parametro una Map.
    */

    static void BestEqualPieces(Map m) {
        int xBest = 0;
        int yBest = 0;
        int elfiBest = 0;
        int naniBest = 0;
        int orchiBest = 0;
        int scoreBest = 0;
        int score = 0;
        for(int i = 0; i < m.getHeight(); i++) {
            for(int j = 0; j < m.getWidth(); j++) {
                if(m.getElfi(j, i) >= elfiBest) {
                    score = m.getElfi(j, i);
                }
                if(m.getNani(j, i) >= naniBest) {
                    score = m.getNani(j, i);
                }
                if(m.getOrchi(j, i) >= orchiBest) {
                    score = m.getOrchi(j, i);
                }
                if(score >= scoreBest) {
                    scoreBest = score;
                    xBest = j;
                    yBest = i;
                    elfiBest = m.getElfi(xBest, yBest);
                    naniBest = m.getNani(xBest, yBest);
                    orchiBest = m.getOrchi(xBest, yBest);
                }
                score = 0;
            }
        }
        System.out.println("(" + xBest + ", " + yBest + ") con " + elfiBest + " elfi, " + naniBest + " nani e " + orchiBest + " orchi");
    }
}