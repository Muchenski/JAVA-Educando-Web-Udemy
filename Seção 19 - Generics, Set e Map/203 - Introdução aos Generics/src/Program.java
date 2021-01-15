import java.util.Scanner;

import services.PrintService;

public class Program {
	public static void main(String[] args) {

		// ATEN��O:

		// N�o utilizar a classe 'Object' para tentar simular Generics<T>.
		// Pois, deste modo n�o temos o type-safety, podendo aumentar
		// muito a ocorr�ncia de erros, e causando preju�zos a performance
		// do c�digo(j� que iremos necessitar de v�rios castings.

		Scanner sc = new Scanner(System.in);

		PrintService<Integer> psI = new PrintService<Integer>();
		PrintService<String> psS = new PrintService<String>();

		System.out.print("How many values? ");
		int n = sc.nextInt();

		for (int i = 0; i < n; i++) {
			int value = sc.nextInt();
			psI.addValue(value);
		}

		psI.print();
		System.out.println("First: " + psI.first());

		////////////////////////////////////////////////////////////

		System.out.print("How many values? ");
		n = sc.nextInt();

		for (int i = 0; i < n; i++) {
			String value = sc.next();
			psS.addValue(value);
		}

		psS.print();
		System.out.println("First: " + psS.first());

		sc.close();
	}
}
