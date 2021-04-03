import java.io.*;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

public class Main{

    public static void main(String[] args) throws IOException{

        /* Para ler o arquivo existente precisamos criar um Objeto File. Informa-se 
        o caminho (path) dele no diretório, e cria um BufferedReader (método que
        permite leitura de arquivos). */
        File words = new File("src/files/words.txt");
        FileReader fr = new FileReader(words);
        BufferedReader br = new BufferedReader(fr);

        /* Para facilitar o trabalho na procura de palavras, normaliza-se todas
        as palavras do arquivo. Normalizar as palavras significa transformar tudo
        em letras minúsculas e sem acentos nas letras. */
        String line = " ";
        String normalizedWord;

        /* Aqui, cria-se uma lista que manterá todas as nossas palavras. */
        ArrayList<String> wordsArray = new ArrayList<>();
    
        /* Lê-se a primeira linha do arquivo, */
        line = br.readLine();
        
        /* E se essa linha não for nula (ou seja, não é o final do arquivo ainda),
        continuaremos a ler. */
        while(line != null){
            
            /* A palavra será normalizada a partir do método 'getNormalizedWord'. */
            normalizedWord = getNormalizedWord(line);
            wordsArray.add(normalizedWord);
            
            line = br.readLine();
        }

        //Fechamento do método que permite ler o arquivo.
        br.close();

        /* Método que permite leitura dentro do terminal. */
        Scanner read = new Scanner(System.in);

        System.out.println("JOGO MONTA PALAVRAS");
        System.out.println("\nRegras: \nCada letra tem uma pontuação. \nVocê vai digitar um conjunto de letras, "+
        "e nós te daremos uma palavra (com as letras que você nos deu) com a maior pontuação. \n\nEntendeu? Então vamos lá!");

        /* Variáveis de controle para caso o usuário quiser jogar novamente. */
        String repeatAwnser = " ";
        boolean repeat;

        String typedLetters;

        do {
            System.out.println("\nDigite as letras disponíveis nessa jogada: ");
            typedLetters = read.nextLine().toLowerCase();

            /* A variável 'typedLettersSorted' mantém em ordem alfabética
            as letras que o usuário digitou. */
            String typedLettersSorted = getSortedString(typedLetters);
            
            /* Para cada pesquisa do usuário, teremos uma lista com as palavras achadas, e uma 
            lista com as letras não usadas para formar a palavra diante das letras digitadas pelo
            usuário. */
            ArrayList<String> wordsFound = new ArrayList<>();
            ArrayList<String> lettersNotUsed = new ArrayList<>();

            /* Chamada do método que acha as palavras que podem ser formadas a partir das
            letras digitadas pelo usuário. */
            lettersNotUsed = getWordsFound(typedLettersSorted, wordsArray, wordsFound);

            /* CONTROLE. APAGAR DEPOIS. */
            System.out.println("Words found: ");
            for (String word : wordsFound){
                System.out.println(word);
            }

            /* Assim que foi realizado o método de achar as palavras, iremos conferir se:
            
            A lista de palavras achadas tem um tamanho igual a 0? */
            if (wordsFound.size() == 0){
                /* Se sim, nenhuma palavra foi achada. */
                System.out.println("\nNenhuma palavra encontrada :(");

            } 
            /* A lista de palavras achadas tem um tamanho igual a 1? */
            else if (wordsFound.size() == 1){
                /* Se sim, somente uma palavra foi achada. Iremos calcular quantos pontos essa
                palavra fez a partir do método 'getScoreOfWords' e retornar para o usuário. */
                byte score = getScoreOfWord(wordsFound.get(0));
                System.out.println("\n" + wordsFound.get(0).toUpperCase() + ", palavra de " + score + " pontos");

                char[] lettersLeft = lettersNotUsed.get(0).toCharArray();
                
                System.out.println("\nSobraram: ");
                for (char letter : lettersLeft){
                    System.out.print(letter);
                }

            } 
            /* A lista de palavras achadas tem um tamanho diferente de 0 e 1 (ou seja,
            mais de uma palavra foi achada)? */
            else {
                /* Se sim, iremos achar o index (baseado na lista de palavras achadas) da(s)
                palavra(s) com maior pontuação. */
                ArrayList<Byte> indexesOfWordsWithHighestScore = getWordsWithHighestScore(wordsFound);

                /* Se só tiver um index, significa que só tem uma palavra com a maior pontuação,
                então mostre ao usuátio. */
                if(indexesOfWordsWithHighestScore.size() == 1){
                    String theWord = wordsFound.get(indexesOfWordsWithHighestScore.get(0));
                    byte score = getScoreOfWord(theWord);
                    System.out.println("\n" + theWord.toUpperCase() + ", palavra de " + score + " pontos");

                    char[] lettersLeft = lettersNotUsed.get(indexesOfWordsWithHighestScore.get(0)).toCharArray();
                
                    System.out.println("\nSobraram: ");
                    for (char letter : lettersLeft){
                        System.out.print(letter);
                    }

                } 
                
                /* Se não, significa que existem duas palavras com a mesma pontuação 
                (a maior no caso).*/
                else {
                    /* Então iremos achar o index (baseado na lista de palavras achadas, e
                    nas palavras com maior pontuação) das palavras com menor length. */
                    ArrayList<Byte> indexesOfWordsWithLowestLength = getWordsWithLowestLength(wordsFound, indexesOfWordsWithHighestScore);
                    
                    /* Se só tiver um index, significa que só tem uma palavra com a menor
                    length, então mostre ao usuário. */
                    if(indexesOfWordsWithLowestLength.size() == 1){

                        String theWord = wordsFound.get(indexesOfWordsWithLowestLength.get(0));
                        byte score = getScoreOfWord(theWord);
                        System.out.println("\n" + theWord.toUpperCase() + ", palavra de " + score + " pontos");

                        char[] lettersLeft = lettersNotUsed.get(indexesOfWordsWithLowestLength.get(0)).toCharArray();
                
                        System.out.println("\nSobraram: ");
                        for (char letter : lettersLeft){
                            System.out.print(letter);
                        }
                    } 
                    
                    /* Se não, significa que existem duas palavras com a mesma length
                    (a menor no caso). */
                    else{
                        /* Finalmente, baseando-se na lista de palavras achadas, palavras com
                        maior pontuação e menor length, procuraremos a menor palavra 
                        alfabeticamente falando. */
                        ArrayList<String> indexesOfWords = getWordAlphabeticallySorted(wordsFound, indexesOfWordsWithLowestLength);
                        
                        String theWord = indexesOfWords.get(0);
                        byte score = getScoreOfWord(theWord);
                        
                        System.out.println("\n" + theWord.toUpperCase() + ", palavra de " + score + " pontos");

                        byte indexOfTheWord = 0;
                        for (int i = 0; i < wordsFound.size(); i++){
                            if (wordsFound.get(i).contains(theWord)) {
                                indexOfTheWord = (byte) i;
                            }
                        }
                        char[] lettersLeft = lettersNotUsed.get(indexOfTheWord).toCharArray();
                
                        System.out.println("\nSobraram: ");
                        for (char letter : lettersLeft){
                            System.out.print(letter);
                        }
                    }
                }
                
            }



            // System.out.println("\nPALAVRA, palavra de X pontos.");
            // System.out.println("\nSobraram: LETRAS");


            /* Código que controla se o usuário quer jogar novamente. */
            System.out.println("\n\nVocê deseja jogar outra rodada?");
            repeatAwnser = read.nextLine().toLowerCase();

            if (repeatAwnser.equals("y") || repeatAwnser.equals("yes") || 
                repeatAwnser.equals("s") || repeatAwnser.equals("sim")){
                repeat = true;
            } else{
                repeat = false;
            }
        } while(repeat == true);

        //Fechamento do método que permite ler.
        read.close();
    }

    public static ArrayList<Byte> getWordsWithHighestScore(ArrayList<String> wordsFound){
        
        /* Lista que manterá os pontos das palavras achadas. */
        ArrayList<Byte> scores = new ArrayList<>();

        /* Para cada palavra achada, vamos adicionar a pontuação na lista. */
        for (byte i = 0; i < wordsFound.size(); i++){
            scores.add(getScoreOfWord(wordsFound.get(i)));
        }

        /* Lista que manterá os indexes da(s) maior(es) pontuação(ções). */
        ArrayList<Byte> indexesOfHighestScores = new ArrayList<>();
        /* Independente de ter duas palavras com a mesma pontuação, vamos pegar 
        somente o valor da maior pontuação como referência. */
        byte maxIndex = Collections.max(scores);

        /* Para cada pontuação (ou para cada palavra), vamos pegar todas
        as pontuações máximas (todas no caso de ter duas máximas iguais): */
        for (byte i = 0; i < scores.size(); i++){
            /* Se a pontuação analisada for igual a pontuação máxima: */
            if (scores.get(i) == maxIndex){
                /* Adiciona-se o index da pontuação na lista de indexes. */
                indexesOfHighestScores.add(i);
            } 
            /* Se a pontuação analisada for maior que a pontuação máxima:  */
            else if (scores.get(i) > maxIndex){
                /* Limparemos a lista de indexes, uma vez que os valores guardados nela
                agora são menores que o máximo; */
                indexesOfHighestScores.clear();
                /* Adicionaremos o index da pontuação máxima atual. */
                indexesOfHighestScores.add(i);
                /* E por fim, atualiza-se o valor máximo da pontuação. */
                maxIndex = scores.get(i);
            }
        }

        return indexesOfHighestScores;
    } 

    public static ArrayList<Byte> getWordsWithLowestLength(ArrayList<String> wordsFound, ArrayList<Byte> indexesOfHighestScores){
        
        /* Lista que manterá as lengths das palavras com maior pontuação.*/
        ArrayList<Byte> lengthsOfHighestScores = new ArrayList<>();

        /* Para isso, precisamos procurar por essas lengths. */
        for (byte i = 0; i < indexesOfHighestScores.size(); i++){
            /* Para cada palavra com a maior pontuação, vamos adicionar
            sua length à lista. */
            byte lengthOfWord = (byte) (wordsFound.get(indexesOfHighestScores.get(i))).length();
            lengthsOfHighestScores.add(lengthOfWord);
        }

        /* Lista que manterá os indexes da(s) palavra(s) com menor(es) length. */
        ArrayList<Byte> indexesOfLowestLenghts = new ArrayList<>();

        /* Independente de ter duas palavras com a mesma length, vamos pegar 
        somente o valor da menor length como referência.  */
        byte minLength = Collections.min(lengthsOfHighestScores);
        
        /* Aqui, analizaremos os indexes das palavras com maiores pontuações. */
        for (byte i = 0; i < indexesOfHighestScores.size(); i++){

            /* Se a length da palavra analisada (que tem a maior pontuação)
            for igual a length mínima: */
            if (lengthsOfHighestScores.get(i) == minLength){
                /* Adiciona-se o index da palavra de maior pontuação, na lista de indexes
                de palavras com menores lengths. */
                indexesOfLowestLenghts.add(indexesOfHighestScores.get(i));
            } 
            /* Se a lenght da palavra analisada (que tem a maior pontuação)
            for menor que a length mínima: */
            else if (lengthsOfHighestScores.get(i) < minLength){
                /* Limparemos a lista de indexes com menores lengths, uma vez que
                os valores guardados nela agora são maiores que o mínimo. */
                indexesOfLowestLenghts.clear();
                /* Adicionaremos o index da maior pontuação atual. */
                indexesOfLowestLenghts.add(indexesOfHighestScores.get(i));
                /* E por fim, atualiza-se o valor mínimo da length. */
                minLength = lengthsOfHighestScores.get(i);
            }
        }

        return indexesOfLowestLenghts;
    }

    public static ArrayList<String> getWordAlphabeticallySorted(ArrayList<String> wordsFound, ArrayList<Byte> indexesOfLowestLenghts){
        
        /* Lista que manterá as palavras com maior pontuação e menor length. */
        ArrayList<String> wordsWithHighestScoreAndLowestLenght = new ArrayList<>();

        /* Para isso, precisamos percorrer a lista de indexes de palavras com
        menores lengths (e consequentemente maiores pontuações). */
        for (byte i = 0; i < indexesOfLowestLenghts.size(); i++){
            /* Para cada index, pegaremos a palavra correspondente e adicionaremos
            a lista. */
            String word = wordsFound.get(indexesOfLowestLenghts.get(i));
            wordsWithHighestScoreAndLowestLenght.add(word);
        }

        /* Agora temos uma lista filtrada com as palavras achadas pela pesquisa
        com a maior pontuação possível e menor length possível. Entretanto, se existem 
        mais de uma palavra nessa lista, as palavras tem mesma pontuação e mesmo length, 
        e para desempatar precisamos ver qual delas vem primeiro, alfabeticamente falando.
        Utiliza-se o método 'sort' para ordenar as palavras. */
        Collections.sort(wordsWithHighestScoreAndLowestLenght);

        /* Finalmente, retorna-se a palavra com maior pontuação, menor length, e 
        alfabeticamente ordenada. */
        return wordsWithHighestScoreAndLowestLenght;
    }

    public static byte getScoreOfWord(String word){
        byte score = 0;

        /* Transforma a palavra em um array de chars. */
        char[] wordInChars = word.toCharArray();

        /* Para cada char no array de chars, faça: */
        for (char c : wordInChars){
            /* Se o char for igual a uma das letras listadas na condição, a contagem de pontos soma 1. */
            if (c == 'a' || c == 'e' || c == 'i' || c == 'l' || c == 'n' || 
            c == 'o' || c == 'r' || c == 's' || c == 't' || c == 'u'){
                score+=1;
            } else if (c == 'd' || c == 'g'){ /* Soma 2. */
                score+=2;
            } else if (c == 'b' || c == 'c' || c == 'm' || c == 'p'){ /* Soma 3. */
                score+=3;
            } else if (c == 'f' || c == 'h' || c == 'v'){ /* Soma 5. */
                score+=5;
            } else if (c == 'j' || c == 'x'){ /* Soma 8. */
                score+=8;
            } else if (c == 'q' || c == 'z'){ /* Soma 13. */
                score+=13;
            }
        }

        return score;
    }

    public static ArrayList<String> getWordsFound(String typedLetters, ArrayList<String> words, 
    ArrayList<String> wordsFound){
        
        /* Lista que manterá as letras não usadas de uma palavra. */
        ArrayList<String> lettersNotUsed = new ArrayList<>();

        /* Cópia da lista que contém todas as palavras. */
        ArrayList<String> copyOfAllWords = new ArrayList<String>(words);

        /* Tranformando as letras digitadas pelo usuário em um array de chars. */
        char[] lettersInChars = typedLetters.toCharArray();

        /* Para cada letra no array de letras digitadas pelo usuário, faça: */
        for(char letter : lettersInChars) {
            /* Loop que percorre a cópia da lista de palavras. */
            for (int i = 0; i < copyOfAllWords.size(); i++) {
                /* Ordenando a palavra que está sendo analisada. */
                String wordSorted = getSortedString(copyOfAllWords.get(i));

                /* Se essa palavra não estiver em branco (somente estaços e não letras), ou vazia:  */
                if(!wordSorted.isBlank()){
                    /* Troca na palavra (que está na posição i da cópia da lista de palavras),
                    o primeiro char que é igual a letra que estamos analisando, por um espaço vazio.
                    Ou seja, significa que o char digitado pelo usuário foi achado na palavra. */
                    copyOfAllWords.set(i, wordSorted.replaceFirst(Character.toString(letter), ""));
                }
            }
        }

        /* Finalmente, agora procuraremos as palavras achadas. */
        for (int i = 0; i < copyOfAllWords.size(); i++){
            /* Variável que manterá todas as letras não utilizadas para formar a palavra. */
            String lettersNotUsedString = "";

            /* Se na cópia da lista de palavras, a primeira posição estiver vazia, 
            significa que todas as letras dessa palavra foram achadas nas letras 
            digitadas pelo usuário. */
            if(copyOfAllWords.get(i).isBlank()){

                /* Por isso, vamos adicionar essa palavra à lista de palavras achadas. */
                wordsFound.add(words.get(i));

                /* Entretanto, para achar as letras não utilzadas, percorreremos o array
                de de letras digitadas pelo usuário. */
                for (char letter : lettersInChars){
                    /* Se a palavra na posição indicada
                    !ATENÇÃO! Utilizamos essa palavra (da lista geral de palavras) pois entramos no if 
                    !condicional (ou seja, essa é uma palavra achada).
                    não tiver a letra analisada (isso por que o método 'indexOf' indica 
                    o index na qual essa letra se encontra. Se o index for -1, significa 
                    que a letra nãso consta na String), essa letra não foi utilizada.*/
                    if (words.get(i).indexOf(letter) == -1){
                        /* Concatena-se a letra não utilizada na String de letras não utilizadas
                        dessa palavra. */
                        lettersNotUsedString += letter;
                    }
                }

                /* Por fim, antes de percorrer a próxima palavra, adiciona-se a String que mantém
                as letras não utilizadas naquela palavra, a uma lista. */
                lettersNotUsed.add(lettersNotUsedString);
            }
            
        }

        return lettersNotUsed;
    }

    public static String getSortedString(String word){
        /* Transformando a palavra em um array de chars. */
        char[] wordInChars = word.toCharArray();
        /* Utiliza-se o método 'sort' da classe Arrays que automaticamente 
        ordena as letras alfabeticamente. */
        Arrays.sort(wordInChars);
        String sortedString = new String(wordInChars);
        return sortedString;
    }

    public static String getNormalizedWord(String word) {
        /* Chamamos a classe 'Normalizer' e seu método 'normalize', que normaliza a sequencia de
        chars. A string que será normalizada é a primeira a ser passada como parâmetro. Já o 
        segundo parâmetro é uma 'forma de normalização'. */
        String normalizedWord = Normalizer.normalize(word, Normalizer.Form.NFD); 

        /* Já a classe 'Pattern' é um padrão ou representação de uma expressão regular. 
        O método 'compile' compila a expressão em um padrão.*/
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

        /* Por fim, retorna-se o método 'matcher' que cria um combinador que irá
        combinar a string no padrão dado.*/
        return (pattern.matcher(normalizedWord).replaceAll("")).toLowerCase();
    }
}