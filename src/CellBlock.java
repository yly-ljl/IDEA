public class CellBlock {
    private int age;

    public CellBlock() {}

    public CellBlock(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void ageIncrease(){
        this.age++;
    }
}