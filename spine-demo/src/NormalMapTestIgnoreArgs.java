import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

class NormalMapTestIgnoreArgs {
    public static void main(String[] args) throws Exception {
        new LwjglApplication(new com.esotericsoftware.spine.NormalMapTest(
                "spineboy/spineboy-old",
                "walk"
        ));
    }
}