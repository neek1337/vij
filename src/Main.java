import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static final String alphabet = "абвгдежзийклмнопрстуфхцчшщъыьэюя";
    public static int[] key;
    public static boolean founded = false;
    public static AtomicBoolean flag;
    public static HashMap<Integer, HashSet<String>> dic;
    public static int[] truekey;

    public static void main(String[] args) throws IOException, InterruptedException {
        flag = new AtomicBoolean(true);


        ExecutorService service = Executors.newFixedThreadPool(8);
        /*
        for (int i = 0; i < 8; i++) {
            service.submit(new Checker(flag, new HashSet<String>()));
        } */

       /* while (service.isTerminated()){
            service.awaitTermination(100, TimeUnit.MILLISECONDS);
        } */
        Scanner scanner = new Scanner(new File("message.txt"));
        FileWriter fileWriter = new FileWriter(new File("cryptedtext.txt"));
        String keyStr = scanner.nextLine();

        String[] keyAr = keyStr.split(" ");
        key = new int[keyAr.length];
        for (int i = 0; i < keyAr.length; i++) {
            key[i] = Integer.valueOf(keyAr[i]);
        }
        String mes = "";
        while (scanner.hasNextLine()) {
            mes += scanner.nextLine().toLowerCase() + " ";
        }
        ArrayList<Integer> spaces = new ArrayList<Integer>();
        StringBuilder stringBuilder1 = new StringBuilder(mes);
        for (int i = 0; i < mes.length(); i++) {
            if (alphabet.indexOf(mes.charAt(i)) == -1) {
                stringBuilder1.replace(i - spaces.size(), i - spaces.size() + 1, "");
                spaces.add(i);
            }
        }

        mes = checkLength(stringBuilder1.toString());


        String result = "";
        for (int i = 0; i < mes.length() / key.length; i++) {
            result += code(mes.substring(i * key.length, (i + 1) * key.length));
        }

        StringBuilder stringBuilder = new StringBuilder(result);
        for (Integer integ : spaces) {
            stringBuilder.insert(integ, " ");
        }
        String s = stringBuilder.toString();
        while (s.contains("  ")) {
            s = s.replaceAll("  ", " ");
        }
        fileWriter.write(s);
        fileWriter.write("\n");


        fileWriter.flush();
        fileWriter.close();
        ;

       /* dic = new HashMap<Integer, HashSet<String>>();
        int dicsiz = 100000;
        Scanner scanner1 = new Scanner(new File("C:\\Users\\Никита\\Downloads\\vij\\src\\areverse.txt"));
        int number = 1;
        while (scanner1.hasNext()) {
            int key = dicsiz / number;
            number++;
            if (!dic.containsKey(key)) {
                dic.put(key, new HashSet<String>());
            }
            dic.get(key).add(scanner1.next());
        }
        System.out.println(dic.size());
        Scanner scanner = new Scanner(new File("C:\\Users\\Никита\\Downloads\\vij\\src\\cryptedtext.txt"));
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNextLine()) {
            stringBuilder.append(scanner.nextLine());
            stringBuilder.append(" ");
        }
        ArrayList<Integer> spaces = new ArrayList<Integer>();
        for (int i = 0; i < stringBuilder.length(); i++) {
            if (stringBuilder.charAt(i) == ' ') {
                spaces.add(i);
            }
        }
        String textWOSpaces = stringBuilder.toString().toLowerCase().replaceAll(" ", "");
        Map<String, ArrayList<Integer>> map = positions(textWOSpaces);
        for (String s : map.keySet()) {
            System.out.print(s + ": ");
            for (Integer a : map.get(s)) {
                System.out.print(a + " ");
            }
            System.out.println();
        }
        HashMap<Integer, Integer> hashMap = sortByValue(kas(map));
        for (Integer s : hashMap.keySet()) {
            System.out.println(s + ": " + hashMap.get(s));
        }
        int distance = 0;
        for (Integer s : hashMap.keySet()) {
            distance = s;
            break;
        }
        System.out.println(distance);
        int[] maxs = analysis(textWOSpaces, distance);
        check(textWOSpaces, distance, maxs, spaces.get(0), service);
        for (int i = 0; i < key.length; i++) {
            System.out.print(key[i] + " ");
        }
        System.out.println();
        String result = "";
        for (int i = 0; i < textWOSpaces.length() / key.length; i++) {
            result += decode(textWOSpaces.substring(i * key.length, (i + 1) * key.length));
        }
        stringBuilder = new StringBuilder(result);
        for (Integer integ : spaces) {
            stringBuilder.insert(integ, " ");
        }

        System.out.println(stringBuilder);
        List<String> l = val(distance);   */
    }

    public static String code(String mes) {
        String result = "";
        for (int i = 0; i < key.length; i++) {
            result += alphabet.charAt((alphabet.indexOf(mes.charAt(i)) + key[i]) % alphabet.length());
        }
        return result;
    }

    public static String checkLength(String mes) {
        while (mes.length() % key.length != 0) {
            mes += " ";
        }
        return mes;
    }

    public static String decode(String mes) {
        String result = "";
        for (int i = 0; i < key.length; i++) {
            int index = alphabet.indexOf(mes.charAt(i)) - key[i];
            if (index < 0) {
                index += alphabet.length();
            }
            result += alphabet.charAt(index);
        }
        return result;
    }

    public static Map<String, ArrayList<Integer>> positions(String s) {
        Map<String, ArrayList<Integer>> map = new HashMap<String, ArrayList<Integer>>();
        for (int i = 0; i < s.length() - 3; i++) {
            String local = s.substring(i, i + 3);
            if (!map.keySet().contains(local) && s.substring(i + 3, s.length() - 1).contains(local)) {
                map.put(local, new ArrayList<Integer>());
                int index = 0;
                while (s.indexOf(local, index) != -1) {
                    index = s.indexOf(local, index);
                    map.get(local).add(index);
                    index++;
                }
            }
        }
        return map;
    }

    public static HashMap<Integer, Integer> kas(Map<String, ArrayList<Integer>> map) {
        HashMap<Integer, Integer> nods = new HashMap<Integer, Integer>();
        for (String s : map.keySet()) {
            if (map.get(s).size() > 2) {
                ArrayList<Integer> localAssumption = new ArrayList<Integer>();
                for (int i = 0; i < map.get(s).size() - 2; i++) {
                    int distance1 = map.get(s).get(i + 1) - map.get(s).get(i);
                    int distance2 = map.get(s).get(i + 2) - map.get(s).get(i + 1);
                    localAssumption.add(nod(distance1, distance2));
                }
                int local;
                if (localAssumption.size() > 1) {
                    local = nod(localAssumption.get(0), localAssumption.get(1));
                    for (int i = 2; i < localAssumption.size(); i++) {
                        local = nod(local, localAssumption.get(i));
                    }
                    if (!nods.keySet().contains(local)) {
                        nods.put(local, 1);
                    } else {
                        nods.put(local, nods.get(local) + 1);
                    }
                }
            }
        }
        return nods;
    }

    public static List<String> val(int distance) {
        ArrayList<String> list = new ArrayList<String>();
        char[] characters = new char[]{'о', 'е', 'а', 'и'};
        StringBuilder stringBuilder;
        int[] pos = new int[distance];
        int total = (int) Math.pow(characters.length, distance);
        for (int i = 0; i < total; i++) {
            stringBuilder = new StringBuilder();
            for (int x = 0; x < distance; x++) {
                if (pos[x] == characters.length) {
                    pos[x] = 0;
                    if (x + 1 < distance) {
                        pos[x + 1]++;
                    }
                }
                stringBuilder.append(characters[pos[x]]);
            }
            pos[0]++;
            list.add(stringBuilder.toString());
        }
        return list;
    }

    public static int[] analysis(String s, int distance) {
        key = new int[distance];

        int[] maxs = new int[distance];
        for (int i = 0; i < distance; i++) {
            int[] map = new int[alphabet.length()];
            int index;
            for (int j = 0; j < s.length() / distance; j++) {
                index = alphabet.indexOf(s.charAt(j * distance + i));
                map[index]++;
            }
            System.out.println("Номер позиции " + i);
            for (int j = 0; j < alphabet.length(); j++) {
                System.out.println(j + ":" + map[j]);
            }
            int max = map[0];
            int maxIndex = 0;
            for (int j = 1; j < alphabet.length(); j++) {
                if (map[j] > max) {
                    max = map[j];
                    maxIndex = j;
                }
            }
            maxs[i] = maxIndex;
        }
        return maxs;
    }

    public static void check(String s1, int distance, int[] maxs, int firstSpace, ExecutorService service) throws InterruptedException {
        List<String> list = val(distance);
        int index = 0;

        while (!founded) {
            String s = s1.substring(0, firstSpace);
            String local = list.get(index);
            index++;
            for (int i = 0; i < distance; i++) {
                key[i] = maxs[i] - alphabet.indexOf(local.charAt(i));
                if (key[i] < 0) {
                    key[i] += alphabet.length();
                }
            }
            String decoded = "";
            int tail = 0;
            while (s.length() > 0) {
                if (s.length() < distance) {
                    tail = distance - s.length();
                    s = checkLength(s);
                }
                decoded += decode(s.substring(0, distance));
                s = s.substring(distance);
            }
            decoded = decoded.substring(0, decoded.length() - tail);
            for (int i = 0; i < 8; i++) {
                service.submit(new Checker(flag, dic.get(i), decoded, key));
            }
            for (int i = 0; i < 8; i++) {
                service.submit(new Checker(flag, dic.get(i), decoded.substring(0, decoded.length() - 1), key));
            }
        }
        key = truekey;
    }

    public static int nod(int a, int b) {
        if (b == 0)
            return a;
        return nod(b, a % b);
    }

    public static HashMap<Integer, Integer> sortByValue(HashMap<Integer, Integer> unsortMap) {
        List<Integer> list = new LinkedList(unsortMap.entrySet());

        Collections.sort(list, new Comparator() {
            public int compare(Object o1, Object o2) {
                return -((Comparable) ((Map.Entry) (o1)).getValue())
                        .compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        HashMap<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
        for (Iterator it = list.iterator(); it.hasNext(); ) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put((Integer) entry.getKey(), (Integer) entry.getValue());
        }
        return sortedMap;
    }
}
