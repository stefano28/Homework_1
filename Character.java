public class Character {
    protected double attack;
    protected double defence;

    public Character(double attack, double defence) {
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