package application;

public class Program {

	public static void main(String[] args) {
		
		String name = "Henrique Muchenski";
		
		// IndexOf() e lastIndexOf() � um m�todo case-sensitive
		// Caso estes n�o encontrem o que tentamos buscar, retornar�o -1.
		System.out.println("Primeira ocorr�ncia de 'Mu': " + name.indexOf("Mu"));
		System.out.println("Retorno de busca sem respostas: " + name.indexOf("x"));
		
		System.out.println("Primeira ocorr�ncia de 'Mu': " + name.lastIndexOf("i"));
		System.out.println("Retorno de busca sem respostas: " + name.lastIndexOf("x"));
	}

}
