# Monta Palavras

## Sobre o problema
    Considere um jogo de formar palavras. Neste jogo, cada jogador recebe um conjunto de letras e deve decidir qual palavra formada com aquelas letras vai contabilizar a maior quantidade de pontos.

    Pense nas letras que são disponibilizadas para cada jogador como "pecinhas" de um jogo, ou seja, pode haver letras repetidas. Além disso, cada letra possui um valor, que ajuda a contabilizar mais pontos na palavra que o jogador formar.

    Para formar uma palavra, todas as letras que a compõem devem estar presentes no conjunto de entrada. Em contrapartida, nem todas as letras disponíveis precisam ser usadas. Por exemplo, se você possui as letras "ybttaaa", você pode formar a palavra "batata", deixando de fora a letra "y".

## Passos para a execução
O programa deverá ser executado via terminal. Implementar um programa que lide com uma jogada do jogo de Montar Palavras, sempre fazendo a melhor jogada possível. 
- Disponibilizar um campo de entrada no qual será informado quais letras
estão disponíveis para que a palavra seja formada.
- Mostrar qual é a palavra de maior pontuação, juntamente com qual
pontuação foi obtida.
- Mostrar quais letras não foram utilizadas para formar a melhor palavra.
- Tratar quaisquer caracteres especiais como letras não usadas ("pecinhas"
que sobraram).


## Regras
- O valor de cada letra é fixo e informado abaixo.
- O banco de palavras também é fixo e informado no arquivo de caminho 'src/files/words.txt'. Considere que não
existem palavras que não estejam no banco.
- O valor da palavra corresponde à soma dos valores de cada letra que a
compõem. O valor das letras que não foram utilizadas para formar a palavra
não é descontado no processo.
- Em caso de empate no valor de duas palavras, a palavra mais curta deverá
ser escolhida. Exemplo: "nada" (5 pontos) e "meu" (também 5 pontos) => a
palavra "meu" deverá ser escolhida
- Se ainda assim houver empate, a palavra que vem primeiro em uma
organização alfabética deve ser escolhida. Exemplo: "nada" (5 pontos) e
"lado" (também 5 pontos) => a palavra “lado” deverá ser escolhida.
- Desconsiderar acentos e diferenças entre letras maiúsculas e minúsculas.

## Valor das letras
- 1 ponto: E, A, I, O, N, R, T, L, S, U
- 2 pontos: D, G
- 3 pontos: B, C, M, P
- 5 pontos: F, H, V
- 8 pontos: J, X
- 13 pontos: Q, Z



## Palavras Achadas
Para conseguir achar todas as palavras possíveis, uma cópia da lista de todas as palavras é feita. A cada letra digitada pelo usuário, conferimos se a palavra analisada (todas as palavras serão analisadas) contém essa letra. Se sim, vamos apagar A PRIMEIRA OCORRÊNCIA dessa letra NA PALAVRA na cópia da lista (pois assim, a lista original de palavras fica intacta e podemos consulta-la depois). Se não, passamos para a próxima letra. Por fim, se uma palavra puder ser formada com as letras digitadas pelo usuário, ela não estará visível na cópia da lista original de palavras. 

Observe o exemplo:

        Letras digitadas pelo usuário: aobptra
         Palavras: balão, porta

        Quando a primeira letra das letras digitadas pelo usuário for analisada, analisaremos também
         as palavras: 

    Letra 'A' : balão (contém 'A', então exclua) -> b[]lão 
                porta (contém 'A', então exclua) -> port[]

    Letra 'O' : balão (contém 'O', então exclua) -> b[]lã[] 
                porta (contém 'O', então exclua) -> p[]rt[]

    Letra 'B' : balão (contém 'B', então exclua) -> [][]lã[] 
                porta -> p[]rt[]

    Letra 'P' : balão -> b[]lã[] 
                porta (contém 'P', então exclua) -> [][]rt[]

    Letra 'T' : balão -> b[]lã[] 
                porta (contém 'T', então exclua) -> [][]r[][]

    Letra 'R' : balão -> b[]lã[] 
                porta (contém 'R', então exclua) -> [][][][][]

    Letra 'A' : balão (contém 'A', então exclua) -> b[]l[][] 
                porta (contém 'O', então exclua) -> [][][][][]

    É possível observar que somente a palavra 'PORTA' pode ser formada com as letras disponíveis, uma vez que a letra 'L' da palavra 'BALÃO' não está presente.

Já para pegar as letras não utilizadas, comparamos as letras da palavra achada juntamente com as letras que o usuário digitou.

Entretanto, se mais de uma palavra for achada, precisamos comparar todas as palavras para ver qual será escolhida. Primeiro comparamos a pontuação (a maior vence), e se continuar com um número de palavras maior que 1 (ou seja, algumas palavras tem pontuações iguais), compararemos o número de caracteres que essas palavras tem. Se por algum caso, ainda houver um número de palavras maior que 1 (ou seja, ainda existem palavras com a mesma pontuação e mesmo número de caracteres), compararemos as palavras em ordem alfabética. A que vier primeiro, vence. 

Bom. Caso haja alguma dúvida, o código está inteiramente comentado, para melhor entendimento
 da implementação.


## ATENÇÃO 
    A funcionalidade de 'Bônus de posição foi implementada.

# Exemplo
    Digite as letras disponíveis nesta jogada: yibttaaa

    BATATA, palavra de 8 pontos
    Sobraram: I, Y
___
    Digite as letras disponíveis nesta jogada: folgauv
    Digite a posição bônus: 0

    FOLGA, palavra de 10 pontos
    Sobraram: U, V
___
    Digite as letras disponíveis nesta jogada: folgauv
    Digite a posição bônus: 2

    UVA, palavra de 12 pontos
    Sobraram: F, O, L, G







