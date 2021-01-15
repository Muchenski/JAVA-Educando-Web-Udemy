import entities.Client;

public class Program {

	public static void main(String[] args) {

		Client c1 = new Client("Maria", "maria@gmail.com");
		Client c2 = new Client("Alex", "alex@gmail.com");
		Client c3 = new Client("Maria", "maria@hotmail.com");

		System.out.println(c1.hashCode()); // 74113781
		System.out.println(c2.hashCode()); // 2043485
		System.out.println(c3.hashCode()); // 74113781

		System.out.println(c1.equals(c2)); // false

		// Verdadeiro, pois nosso crit�rio de avalia��o est� apenas no nome.
		System.out.println(c1.equals(c3)); // true

		// '==' compara refer�ncia de mem�ria, em tipos sem tratamento especial.
		System.out.println(c1 == c3); // false

		// Para compara��es de igualdade de valores, damos prefer�ncia ao equals().
		// Para compararmos a igualdade de refer�ncia, damos prefer�ncia ao '=='.

		// O tipo String, quando n�o h� instancia��o, � tratado como tipo especial
		// ent�o podemos utilizar o == para comparar a igualdade de valores.
		String a = "teste";
		String b = "teste";
		System.out.println(a == b); // true

		// Por�m, se for�armos instancia��o, deixar� de ser tratada como tipo especial:
		String c = new String("teste");
		String d = new String("teste");
		System.out.println(c == d); // false

	}

}
