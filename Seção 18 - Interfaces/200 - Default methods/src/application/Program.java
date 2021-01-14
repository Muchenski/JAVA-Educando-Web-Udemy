package application;

import java.util.Locale;
import java.util.Scanner;

import services.InterestService;
import services.UsaInterestService;

public class Program {

	public static void main(String[] args) {
		/*
		 * A partir do java 8, interfaces podem conter m�todos concretos.
		 * 
		 * As inten��es s�o prover implementa��o padr�o para m�todos, afim de:
		 *
		 * Evitar repeti��o de implementa��o em toda classe que implemente a interface.
		 * 
		 * Evitar a necessidade de se criar classes abstratas para prover reuso da
		 * implementa��o.
		 * 
		 * Permitir que "interfaces funcionais" (que devem conter apenas um m�todo)
		 * possam prover outras opera��es padr�o reutiliz�veis.
		 */

		Locale.setDefault(Locale.US);
		Scanner sc = new Scanner(System.in);

		System.out.print("Amount: ");
		double amount = sc.nextDouble();
		System.out.print("Months: ");
		int months = sc.nextInt();

		InterestService is = new UsaInterestService(1.0);
		double payment = is.payment(months, amount);

		System.out.println("Payment after " + months + " months:");
		System.out.println(String.format("%.2f", payment));

		sc.close();
	}

}
