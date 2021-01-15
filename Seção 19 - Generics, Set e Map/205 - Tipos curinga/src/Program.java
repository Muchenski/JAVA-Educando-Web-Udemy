import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Program {

	public static void main(String[] args) {

		// Apesar de Object ser SuperClasse de Integer,
		// Uma Lista de Object n�o � SuperClasse de uma Lista de Integer.
		// Object � SuperClasse de cada elemento da lista, mas n�o da Lista como um
		// todo.

		// N�o ocorrer� erro:
		Object object;
		Integer integer = 10;
		object = integer;

		// Ocorrer� um erro:

		// List<Object> objects = new ArrayList<Object>();
		// List<Integer> integers = new ArrayList<Integer>();
		// objects = integers;

		/////////////////////////////////////////////////////////////

		// O SuperTipo de qualquer tipo de lista � List<?>:
		List<?> superList = new ArrayList<Object>();
		List<Integer> integers = new ArrayList<Integer>();
		superList = integers;

		printList(Arrays.asList(1, 2, 3, 4, 5));
		printList(Arrays.asList("h", "c", "a", "p"));
	}

	// M�todo que imprime dados de qualquer tipo de lista:
	public static void printList(List<?> list) {
		for (Object object : list) {
			System.out.print(object);
		}
		System.out.println();

		// OBS: Apesar de conseguirmos acessar os dados de uma lista curinga
		// n�o conseguimos setar valores nela.
		// list.add(5);
	}
}
