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
            getWordsFound(typedLettersSorted, wordsArray, wordsFound, lettersNotUsed);

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
                palavra fez a partir do método 'getPointsOfWords' e retornar para o usuário. */
                byte points = getPointsOfWord(wordsFound.get(0));
                System.out.println("\n" + wordsFound.get(0).toUpperCase() + ", palavra de " + points + " pontos");

            } 
            /* A lista de palavras achadas tem um tamanho diferente de 0 e 1 (ou seja,
            mais de uma palavra foi achada)? */
            else {
                /* Se sim, iremos pegar a palavra com mais pontos e retornar para o usuário. */
                getWordWithMostPoints(wordsFound);
            }



            // System.out.println("\nPALAVRA, palavra de X pontos.");
            // System.out.println("\nSobraram: LETRAS");


            /* Código que controla se o usuário quer jogar novamente. */
            System.out.println("\nVocê deseja jogar outra rodada?");
            repeatAwnser = read.nextLine().toLowerCase();

            if (repeatAwnser.equals("y") || repeatAwnser.equals("yes") || repeatAwnser.equals("s") || repeatAwnser.equals("sim")){
                repeat = true;
            } else{
                repeat = false;
            }
        } while(repeat == true);

        //Fechamento do método que permite ler.
        read.close();
    }

    public static void getWordWithMostPoints(ArrayList<String> wordsFound){
        ArrayList<Byte> points = new ArrayList<>();

        for (byte i = 0; i < wordsFound.size(); i++){
            points.set(i, getPointsOfWord(wordsFound.get(i)));
        }

        if (points.size() == 1){

        } else{
            ArrayList<Byte> indexes = new ArrayList<>();
            byte max = Collections.min(points);
    
            for (byte i = 0; i < points.size(); i++){
                if (points.get(i) == max){
                    
                    indexes.add(i);
                } else if (points.get(i) > max){
                    
                    indexes.clear();
                    indexes.add(i);
                    max = points.get(i);
                }
            }
        }

    } 

    public static byte getPointsOfWord(String word){
        byte points = 0;

        char[] wordInChars = word.toCharArray();

        for (char c : wordInChars){
            if (c == 'a' || c == 'e' || c == 'i' || c == 'l' || c == 'n' || 
            c == 'o' || c == 'r' || c == 's' || c == 't' || c == 'u'){
                points+=1;
            } else if (c == 'd' || c == 'g'){
                points+=2;
            } else if (c == 'b' || c == 'c' || c == 'm' || c == 'p'){
                points+=3;
            } else if (c == 'f' || c == 'h' || c == 'v'){
                points+=5;
            } else if (c == 'j' || c == 'x'){
                points+=8;
            } else if (c == 'q' || c == 'z'){
                points+=13;
            }
        }

        return points;
    }

    public static void getWordsFound(String typedLetters, ArrayList<String> words, 
    ArrayList<String> wordsFound, ArrayList<String> lettersNotUsed){
        // ArrayList<String> wordsFound = new ArrayList<>();
        //make a copy to work on

        ArrayList<String> copyOfAllWords = new ArrayList<String>(words);
        // Collections.copy(copy, words);
        
        char[] lettersInChars = typedLetters.toCharArray();

        for(char letter : lettersInChars) {
            for (int i = 0; i < copyOfAllWords.size(); i++) {

                String wordSorted = getSortedString(copyOfAllWords.get(i));

                if(!wordSorted.isBlank()){
                    copyOfAllWords.set(i, wordSorted.replaceFirst(Character.toString(letter), ""));
                }
            }
        }

        for (int i = 0; i < copyOfAllWords.size(); i++){
            if(copyOfAllWords.get(i).isBlank()){
                wordsFound.add(words.get(i));
            }     //find empty strings
        }

    }

    public static String getSortedString(String stringToSort){
        char[] lettersArray = stringToSort.toCharArray();
        Arrays.sort(lettersArray);
        String sortedString = new String(lettersArray);
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