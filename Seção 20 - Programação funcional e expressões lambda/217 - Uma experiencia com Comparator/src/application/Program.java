package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import entities.Product;

public class Program {

	public static void main(String[] args) {

		// Problema: Queremos ordenar uma lista de produto.

		// Utilizando a interface Comparable<T> nossa classe n�o fica fechada para
		// altera��o:
		// Se o crit�rio de compara��o mudar, precisaremos alterar a classe Product.
		// Desta forma, estamos tendo um ponto de manuten��o a mais.

		// Solu��o:
		// � Podemos ent�o usar o default method "sort" da interface List:
		// default void sort(Comparator<? super E> c)

		// Comparator � uma interface funcional(s� possui um m�todo).

		List<Product> products = new ArrayList<Product>();

		products.addAll(Arrays.asList(new Product("Tv", 900.00), new Product("Notebook", 1200.00),
				new Product("Tablet", 450.00)));

		// Fun��o anonima:
		Comparator<Product> comp = (p1, p2) -> p1.getPrice().compareTo(p2.getPrice());

		products.sort(comp);

		for (Product product : products) {
			System.out.println(product.getName() + "," + product.getPrice());
		}
	}
}

// Outras maneiras de utilizar o Comparator:

//� Comparator objeto de classe separada
//� Comparator objeto de classe an�nima
//� Comparator objeto de express�o lambda com chaves
//� Comparator objeto de express�o lambda sem chaves
//� Comparator express�o lambda "direto no argumento"

