import java.util.Map;
import java.util.TreeMap;

public class Program {

	public static void main(String[] args) {

		// � Principais implementa��es:

		// � HashMap - mais r�pido (opera��es O(1) em tabela hash) e n�o ordenado

		// � TreeMap - mais lento (opera��es O(log(n)) em �rvore rubro-negra) e ordenado
		// pelo compareTo do objeto (ou Comparator)

		// � LinkedHashMap - velocidade intermedi�ria e elementos na ordem em que s�o
		// adicionados

		// � put(key, value), remove(key), containsKey(key), get(key)
		// � Baseado em equals e hashCode
		// � Se equals e hashCode n�o existir, � usada compara��o de ponteiros
		// � clear()
		// � size()
		// � keySet() - retorna um Set<K>
		// � values() - retornaa um Collection<V>

		// Como String implementa Comparable, podemos utilizar o TreeMap<K,V>.
		Map<String, String> cookies = new TreeMap<String, String>();

		// put(chave, valor);
		cookies.put("username", "Maria");
		cookies.put("email", "maria@gmail.com");
		cookies.put("phone", "4199241439");

		// remove(chave);
		cookies.remove("email");

		System.out.println("ALL COOKIES: ");
		for (String key : cookies.keySet()) {
			System.out.println(key + ": " + cookies.get(key));
		}
	}

}
