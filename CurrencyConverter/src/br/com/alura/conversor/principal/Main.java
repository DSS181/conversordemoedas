package br.com.alura.conversor.principal;
import br.com.alura.conversor.modelos.DataRequest;
import br.com.alura.conversor.modelos.LoadText;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {
    public static String formatSelected(String selection){
        if (selection == null){
            return null;
        }else{
            selection = selection.replace("-", "");
            selection = selection.trim();
            selection = selection.substring(0,3);
            return selection;
        }
    }
    public static String removeSelected(int keyPressed, String selection, String text){
        String s = keyPressed +" - ";
        text = text.replace(s+selection, "");
        return text.replace(s+selection, "");
    }
    public static void main(String[] args) {

        String selected = "";
        String selected2 = "";
        String welcome = "Bem vindo ao conversor de moedas (><)";
        String validOption = "Por favor, digite uma opção valida.\n";
        String typeAmount = "Digite o valor que deseja converter de %s para %s [0 - Voltar ] ->> ";

        boolean menu = true; //variavel para manter o programa rodando.
        boolean mainMenu = true; //menu principal,
        boolean subMenu = false; //segundo menu.
        int lastPress = 0; //armazena ultima opção valida, para reutilizar no menu anterior.
        boolean amountMenu = false; //menu para digitar o valor a ser convertido.

        LoadText msg = new LoadText(); //objeto para carregar arquivo de texto "moedas.txt".
        String str; //string para armazenar conteudo do arquivo moedas.txt

        // Verifica se str recebe string do arquivo de texto, caso seja null vai continuar tentando.
        do {
            try {
                str = msg.fileReader();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        } while (str == null);

        //StringBuilder sb = new StringBuilder(str); // não necessario por str ja ser uma string, mas utilizado para conhecimento.

        var split = str.length() / 2; //divide str em 2 partes.
        var text1 = str.substring(0, split);//recebe a parte 1
        text1 = text1.trim(); //limpa espaços não necessários.
        var text2 = str.substring(split); //recebe a parte 2
        text2 = text2.trim();

        List<String> currencies = new ArrayList<>(); //cria uma lista de moedas

        String[] newText = text1.split(":"); //separa a string por simbolo :
        newText = newText[1].split("#"); //separa a string por simbolo #, utilizado para separar apenas o que interessa na string.
        newText = newText[0].split("\\d+"); //separa o conteudo numerado no arquivo de texto ex.. 1 - BRL - Real Brasileiro, ...

        Map<String, Integer> loadedCurrencies = new HashMap<>(); //tentativa de utlizar função semelhante a for k, v in pairs da linguagem lua.

        for (int i = 0; i < newText.length - 1; i++) { //loop para adicionar as moedas do arquivo de texto sem o - Exit
            currencies.add(newText[i]);
            if (i != 0 ){
                loadedCurrencies.put(newText[i], i); //utilizado para verificar se a opção digitada está contida na lista evitando assim muitos if else if.
            }
        }

        while (menu) { //programa rodando se true

            if (mainMenu) {
                System.out.println(welcome+" - Desafio Alura\n");
                System.out.println(text1);
                String title;
                Scanner read = new Scanner(System.in); // scanner para verificar a opcção digitada.
                try{
                    int keyPressed = read.nextInt(); //faz a leitura se for digitada uma int
                    lastPress = keyPressed;
                    if (keyPressed == 0) { // encerra o app
                        mainMenu = false;
                        menu = false;
                    }
                    //verifica se digitou uma opção valida, se verdadeiro passa, utiliza valor digitado para buscar a opção dentro da lista
                    else if (loadedCurrencies.containsValue(keyPressed)) {
                        selected = currencies.get(keyPressed).substring(3).trim();//remove o digito, o espaço, o traco e selected recebe o restante da string
                        title = removeSelected(keyPressed, selected, text2); //remove opção selecionada do primeiro menu
                        title = title.trim();
                        System.out.printf((title) + "%n", selected); // printa o menu seguinte sem a opção previamente selecionada.
                        mainMenu = false;
                        subMenu = true;
                    }
                    else if (keyPressed >= (newText.length - 1)) { //remove o ´´- sair ´´ da lista
                        System.out.println(validOption); //retorna opção invalida.
                    }
                }
                catch (InputMismatchException e) { //caso digite qualquer char que não seja int
                    System.out.println(validOption);
                }
            }
            if(subMenu){

                Scanner read = new Scanner(System.in);
                try{
                    int keyPressed = read.nextInt();

                    if (keyPressed == 0) {
                        subMenu = false;
                        menu = false;
                    }
                    else if (keyPressed == 9) { // Voltar para menu principal
                        subMenu = false;
                        mainMenu = true;
                    }
                    else if (loadedCurrencies.containsValue(keyPressed)) {
                        selected2 = currencies.get(keyPressed).substring(3).trim(); //trata a segunda opção selecionada
                        subMenu = false;
                        amountMenu = true;
                    }
                }
                catch (InputMismatchException e) {
                    System.out.println(validOption);
                }
            }
            if (amountMenu){

                System.out.printf(typeAmount,selected,selected2); //printa as opções selecionadas e pede para digitar o valor a ser convertido

                Scanner read = new Scanner(System.in);
                try {
                    double keyPressed = read.nextDouble();
                    String result;
                    if(keyPressed == 0){
                        amountMenu = false;
                        subMenu = true;
                        String title = removeSelected(lastPress, selected, text2);
                        title = title.trim();
                        System.out.printf((title) + "%n", selected);
                    }
                    else {
                        DataRequest req = new DataRequest();
                        req.setCurrencyFrom(formatSelected(selected)); //trata as opções selecionadas para poder utilizar com a API escolhida, ou seja > apenas a sigla da moeda é necessario com > BRL por exemplo.
                        req.setCurrencyTo(formatSelected(selected2));
                        try {
                            result = req.convertCurrency(keyPressed); // faz a requisição junto a API exchangerate e retorna o resultado (resposta)
                        } catch (IOException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println(result); // printa o resultado da conversão
                    }
                } catch (InputMismatchException e) {
                    System.out.println(validOption);
                }
            }
        }
    }
}


