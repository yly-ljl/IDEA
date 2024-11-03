public class CellBlock {
    private int age;
    private boolean IsAlive;

    public CellBlock() {}

    public CellBlock(int age, boolean IsAlive) {
        this.age = age;
        this.IsAlive = IsAlive;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean getIsAlive() {
        return IsAlive;
    }

    public void setAlive(boolean alive) {
        IsAlive = alive;
    }

    public void AgeIncrease(){
        this.age++;
    }

    public void Die(){
        this.IsAlive = false;
        this.age = 0;
    }

    public void Rebirth(int ReBirth_Age){
        this.IsAlive = true;
        this.age = ReBirth_Age;
    }

}