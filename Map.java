public class Map {
    private Cell[][] map;
    private int width;
    private int height;
    /*
        Istanzio un oggetto Elfo, Nano e Orco per poter aver accesso ai loro 
        valori di attacco e difesa nei metodi calculateOverallAttack e calculateOverallDefence.
    */
    private Elfo elfo = new Elfo();
    private Nano nano = new Nano();
    private Orco orco = new Orco();
    private int xCounter; // Contatore per addCell delle ascisse
    private int yCounter; // Contatore per addCell delle ordinate

    public Map(int width, int height) throws InputErrorException {
        if(width <= 0 | height <= 0)
            throw new InputErrorException();
        this.width = width;
        this.height = height;
        map = new Cell[width][height];
        xCounter = yCounter = 0;
    }

    public void addCell(String type) throws InputErrorException {
        if(!(type.equals("montagna") | type.equals("bosco") | type.equals("pianura")))
            throw new InputErrorException();
        Cell cell = new Cell(type);
        if(xCounter > width - 1) {
            yCounter++;
            xCounter = 0;
            if(yCounter > height - 1)
                throw new InputErrorException();
        }
        map[xCounter][yCounter] = cell;
        xCounter++;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    /*
        Metodi per incrementare i contatori dei personaggi di una cella.
        Richiedono come parametro le coordinate della cella.
    */

    public void addElfo(int x, int y) throws InputErrorException {
        if(getCharacters(x, y) > 4)
            throw new InputErrorException();
        map[x][y].elfi++;
    }

    public void addNano(int x, int y) throws InputErrorException {
        if(getCharacters(x, y) > 4)
            throw new InputErrorException();
        map[x][y].nani++;
    }

    public void addOrco(int x, int y) throws InputErrorException {
        if(getCharacters(x, y) > 4)
            throw new InputErrorException();
        map[x][y].orchi++;
    }

    /*
        Metodi per ottenere i contatori dei personaggi di una cella.
        Richiedono come parametro le coordinate della cella.
    */

    public int getElfi(int x, int y) {
        return map[x][y].elfi;
    }

    public int getNani(int x, int y) {
        return map[x][y].nani;
    }

    public int getOrchi(int x, int y) {
        return map[x][y].orchi;
    }

    private int getCharacters(int x, int y) {
        return getElfi(x, y) + getNani(x, y) + getOrchi(x, y);
    }

    /*
        Metodi per il calcolo dei punteggi totali di una cella
    */

    public double getOverallAttack(int x, int y, boolean isDay) {
        map[x][y].calculateOverallAttack(isDay);
        return map[x][y].overallAttack;
    }

    public double getOverallDefence(int x, int y, boolean isDay) {
        map[x][y].calculateOverallDefence(isDay);
        return map[x][y].overallDefence;
    }

    // DEBUG

    /*
        Metodo print di debug
    */

    public void print() {
        for(int i = 0; i < height; i++) {
            for( int j = 0; j < width; j++) {
                System.out.print(map[j][i].type + " ");
            }
            System.out.println();
        }
    }

    private class Cell {
        String type; // Stringa che definisce il tipo di cella "montagna" "bosco" "pianura"
        int elfi; // contatore numero di elfi
        int nani; // contatore numero di nani
        int orchi; // contatore numero di orchi
        double overallAttack; // double che memorizza il punteggio totale di attacco
        double overallDefence; // double che memorizza il punteggio totale di difesa
        
        public Cell(String type) {
            elfi = nani = orchi = 0;
            this.type = type;
        }

        /*
            Metodi che calcolano il punteggio totale di attacco e difesa per la cella tenendo conto dei modificatori.
            Richiede come parametro un boolean che permette di decidere per quale scenario temporale lo si vuole
            calcolare.
        */

        public void calculateOverallAttack(boolean isDay) {
            overallAttack = elfi * elfo.getAttack() + nani * nano.getAttack() + orchi * orco.getAttack();
            if(nani > 0 && type.equals("montagna")) {
                overallAttack += nani * nano.getAttack();
            }
            if(orchi > 0 && isDay) {
                overallAttack -= orchi * orco.getAttack() / 2;
            }
            if(orchi > 0 && !isDay) {
                overallAttack += orchi * orco.getAttack() / 2;
            }
        }

        public void calculateOverallDefence(boolean isDay) {
            overallDefence = elfi * elfo.getDefence() + nani * nano.getDefence() + orchi * orco.getDefence();   
            if(elfi > 0 && type.equals("bosco")) {
                overallDefence += elfi * elfo.getDefence();
            }
            if(orchi > 0 && isDay) {
                overallDefence -= orchi * orco.getDefence() / 2;
            }
            if(orchi > 0 && !isDay) {
                overallDefence += orchi * orco.getDefence() / 2;
            }
        }
    }
}