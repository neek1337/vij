import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 * User: Никита
 * Date: 30.03.15
 * Time: 18:51
 * To change this template use File | Settings | File Templates.
 */
public class Checker implements Runnable {

    volatile AtomicBoolean flag;
    HashSet<String> words;
    String target;
    int[] key;

    public Checker(AtomicBoolean flag, HashSet<String> words, String target, int[] key1) {
        this.flag = flag;
        this.words = words;
        this.target = target;
        this.key = new int[key1.length];
        for (int i = 0; i < key1.length; i++) {
            this.key[i]=key1[i];
        }
    }

    @Override
    public void run() {
        if (words.contains(target) && flag.get()) {
            flag.set(false);
            Main.founded = true;
            Main.truekey = key;

        }
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
