package application;

import entities.Account;
import entities.BusinessAccount;
import entities.SavingsAccount;

public class Program {

	public static void main(String[] args) {

		/*
		 * UpCasting:
		 * 
		 * Casting da SubClasse para a SuperClasse. Uso comum: Polimorfismo.
		 * 
		 */
		
		// Exemplos de UpCasting:

		BusinessAccount bacc1 = new BusinessAccount(2000, "Henrique", 50000.0, 5000.0);
		// Atribuindo os dados da BusinessAccount bacc1, para um objeto inicializado em Account.
		Account acc1 = bacc1;
		
		// OBS:
		// Podemos inicializar vari�veis no tipo da SuperClasse e instanciar com construtores
		// da SubClasse.
		// Por�m, desta forma(UpCasting), ainda n�o podemos acessar os m�todos da SubClasse por 
		// estes objetos, at� que se realize um DownCasting.
		Account acc2 = new BusinessAccount(1003, "Bob", 0.0, 200.0);
		Account acc3 = new SavingsAccount(1004, "Anna", 0.0, 0.01);
		
		/////////////////////////////////////////////////////////////////////////////////////////////////
		
		/*
		 * DownCasting:
		 * 
		 * Casting da SuperClasse para a SubClasse. Palavra 'instanceOf' Uso comum:
		 * m�todos que recebem par�metros gen�ricos(como o equals).
		 * 
		 */
		
		// Exemplos de DownCasting:
		
		BusinessAccount bacc2 = (BusinessAccount)acc2;
		// Agora, com o DownCasting realizado, podemos realizar as opera��es
		// da SubClasse.
		bacc2.loan(100.0);
		
		// Isso resultar� em um erro, pois mesmo que acc3 tenha sido inicializada em Account
		// acc3 � uma inst�ncia de SavingsAccount().
		// BusinessAccount bacc3 = (BusinessAccount)acc3;
		
		// No final, o fator determinante para o DownCasting
		// s�o as classes em que as vari�veis foram inst�nciadas,
		// independentemente das classes em que foram inicializadas
		
		if(acc3 instanceof BusinessAccount) {
			BusinessAccount bacc3 = (BusinessAccount)acc3;
			bacc3.loan(10.0);
			System.out.println("Emprestimo");
		}
		
		if(acc3 instanceof SavingsAccount) {
			SavingsAccount sacc1 = (SavingsAccount)acc3;
			sacc1.updateBalance();
			System.out.println("Updated");
		}
		
		/*
         * N�o podemos fazer:
         * 
         * SuperClasse x = new SuperClasse();
         * SubClasse y = (SubClasse) x;
         * 
         * Nem podemos:
         * 
         * SuperClasse y = new SubclasseY();
         * SubClasseX x = (SubClasseX) y;
         * 
         * */
	}

}
