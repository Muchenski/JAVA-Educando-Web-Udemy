import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Program {

	public static void main(String[] args) {

		// Caracter�sticas
		// � Stream � uma solu��o para processar sequ�ncias de dados de forma:
		// � Declarativa (itera��o interna: escondida do programador)
		// � Parallel-friendly (imut�vel -> thread safe)
		// � Sem efeitos colaterais
		// � Sob demanda (lazy evaluation)
		//
		// � Acesso sequencial (n�o h� �ndices)
		// � Single-use: s� pode ser "usada" uma vez
		// � Pipeline: opera��es em streams retornam novas streams.
		// Ent�o � poss�vel criar uma cadeia de opera��es (fluxo de processamento).

		// Opera��es intermedi�rias e terminais
		// � O pipeline � composto por zero ou mais opera��es intermedi�rias e
		// uma terminal.
		// � Opera��o intermedi�ria:
		// � Produz uma nova streams (encadeamento)
		// � S� executa quando uma opera��o terminal � invocada (lazy evaluation)
		// � Opera��o terminal:
		// � Produz um objeto n�o-stream (cole��o ou outro)
		// � Determina o fim do processamento da stream

		// Opera��es intermedi�rias
		// � filter
		// � map
		// � flatmap
		// � peek
		// � distinct
		// � sorted
		// � skip
		// � limit (*)
		// * short-circuit

		// Opera��es terminais
		// � forEach
		// � forEachOrdered
		// � toArray
		// � reduce
		// � collect
		// � min
		// � max
		// � count
		// � anyMatch (*)
		// � allMatch (*)
		// � noneMatch (*)
		// � findFirst (*)
		// � findAny (*)
		// * short-circuit

		// Criar uma stream
		// � Basta chamar o m�todo stream() ou parallelStream() a partir
		// de qualquer objeto Collection.
		// https://docs.oracle.com/javase/10/docs/api/java/util/Collection.html
		// � Outras formas de se criar uma stream incluem:
		// � Stream.of
		// � Stream.ofNullable
		// � Stream.iterate

		List<Integer> list = Arrays.asList(3, 4, 5, 10, 7);
		Stream<Integer> st1 = list.stream();
		System.out.println(Arrays.toString(st1.toArray()));
		
		Stream<String> st2 = Stream.of("Maria", "Alex", "Bob");
		System.out.println(Arrays.toString(st2.toArray()));
		
		Stream<Integer> st3 = Stream.iterate(0, x -> x + 2);
		System.out.println(Arrays.toString(st3.limit(10).toArray()));

		Stream<Long> st4 = Stream.iterate(new long[]{ 0L, 1L }, p->new long[]{ p[1], p[0]+p[1] }).map(p -> p[0]);
		System.out.println(Arrays.toString(st4.limit(20).toArray()));
	}

}
