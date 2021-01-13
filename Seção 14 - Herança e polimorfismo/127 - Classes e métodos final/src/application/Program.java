package application;

public class Program {

	public static void main(String[] args) {

		// A palavra 'final', aplicada a classes, evita que a classe seja herdada.
		// Quando aplicada a m�todos, evita que o m�todo seja sobreposto.

		// Neste exemplo:

		// Queremos evitar que BusinessAccount seja herdada.

		// Tamb�m queremos que o m�todo 'withdraw', da SavingsAccount, n�o possa
		// ser sobrescrito por alguma SubClasse dela.

		// Explica��es

		// Geralmente conv�m acrescentar 'final' em m�todos sobrepostos, pois
		// sobreposi��es m�ltiplas s�o uma porta de entrada para inconsist�ncias.

		// Atributos de um tipo de classe 'final' s�o analisados de maneira mais
		// veloz em tempo de execu��o.
	}

}
