import java.util.ArrayList;
import java.util.List;

public class SieveOfEratosthenes {
    public static void solve(int n){
        List<Boolean> primeList = new ArrayList<>();
        for(int i = 0; i <= n; i++){
            primeList.add(true);
        }
        for(int i = 2; i*i <= n; i++){

            if(primeList.get(i)){
                for(int j = i*i; j <= n; j += i){
                    primeList.set(j, false);
                }

            }
        }
        for(int i = 2; i <= n; i++){
            if(primeList.get(i)){System.out.println(i);}
        }
    }

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        solve(1000000);
        long end = System.currentTimeMillis();
        System.out.println("Time elapsed: " + (end-start) + "ms");
    }
}
