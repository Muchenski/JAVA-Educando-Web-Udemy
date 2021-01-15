import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Program {

	public static void main(String[] args) {

		// Set<T> � uma interface.

		// Set<T> n�o admite repeti��es.
		// N�o possuem posi��es(podem possuir ordenamento).
		// Acesso, inser��o e remo��o de elementos s�o r�pidos

		// Principais implementa��es:

		// � HashSet - mais r�pido (opera��es O(1) em tabela hash) e n�o ordenado

		// � TreeSet - mais lento (opera��es O(log(n)) em �rvore rubro-negra) e ordenado
		// pelo compareTo do objeto (ou Comparator)

		// � LinkedHashSet - velocidade intermedi�ria e elementos na ordem em que s�o
		// adicionados

		/*
		 * Alguns m�todos importantes
		 * 
		 * � add(obj), remove(obj), contains(obj) � Baseado em equals e hashCode � Se
		 * equals e hashCode n�o existir, � usada compara��o de ponteiros.
		 * 
		 * � clear() � size() � removeIf(predicate)
		 * 
		 * � addAll(other) - uni�o: adiciona no conjunto os elementos do outro conjunto,
		 * sem repeti��o � retainAll(other) - interse��o: remove do conjunto os
		 * elementos n�o contitos em other � removeAll(other) - diferen�a: remove do
		 * conjunto os elementos contidos em other
		 */

		// HashSet - Desordenado, mais r�pido.
		Set<String> set = new HashSet<String>();
		set.add("TV");
		set.add("Notebook");
		set.add("Tablet");
		System.out.println(set.contains("Notebook"));
		for (String p : set) {
			System.out.println(p);
		}

		// TreeSet - Ordenado, mais lento.
		Set<Integer> a = new TreeSet<Integer>(Arrays.asList(0, 2, 4, 5, 6, 8, 10));
		Set<Integer> b = new TreeSet<Integer>(Arrays.asList(5, 6, 7, 8, 9, 10));
		// union
		Set<Integer> c = new TreeSet<Integer>(a);
		c.addAll(b);
		System.out.println(c);
		// intersection
		Set<Integer> d = new TreeSet<Integer>(a);
		d.retainAll(b);
		System.out.println(d);
		// difference
		Set<Integer> e = new TreeSet<Integer>(a);
		e.removeAll(b);
		System.out.println(e);

		// OBS:
		// Para utilizarmos o TreeSet<T>, T deve implementar a interface Comparable<T>.
	}
}
