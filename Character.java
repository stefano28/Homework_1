public class Character {
    private int attack;
    private int defence;

    public Character(int attack, int defence) {
        this.attack = attack;
        this.defence = defence;
    }

    public double getAttack() {
        return attack;
    }

    public double getDefence() {
        return defence;
    }
}